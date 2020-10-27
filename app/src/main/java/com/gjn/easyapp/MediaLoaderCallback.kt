package com.gjn.easyapp

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.gjn.easyapp.easyutils.logV

class MediaLoaderCallback(private val context: Context) : LoaderManager.LoaderCallbacks<Cursor> {

    private val photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    var photoSortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
    var videoSortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

    private var loaderId = -1

    var callback: Callback? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        println("-----------onCreateLoader-----------")
        loaderId = id
        return when (id) {
            LOADER_PHOTO -> CursorLoader(
                context, photoUri, null,
                null, null, photoSortOrder
            )
            LOADER_VIDEO -> CursorLoader(
                context, videoUri, null,
                null, null, videoSortOrder
            )
            else -> CursorLoader(context)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        println("-----------onLoadFinished-----------")
        data?.run {
            val infos = mutableListOf<MediaInfo>()
            val files = mutableMapOf<String, MediaInfo>()
            if (count > 0) {
                moveToFirst()
                do {
                    val info: MediaInfo?
                    when (loaderId) {
                        LOADER_PHOTO -> {
                            info = generatePhotoInfo(this)
                            addData(info, infos, files)
                        }
                        LOADER_VIDEO -> {
                            info = generateVideoInfo(this, context.contentResolver)
                            addData(info, infos, files)
                        }
                        else -> {
                        }
                    }
                } while (moveToNext())

                //所有数据
                "$loaderId 总文件数量 ${infos.size}".logV()
                "$loaderId 总文件夹数量 ${files.size}".logV()
                callback?.complete(infos, files)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        println("-----------onLoaderReset-----------")
    }

    private fun generatePhotoInfo(cursor: Cursor): MediaInfo? {
        val path =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        val parent =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        val mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
        val width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH))
        val height =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT))
        val size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
        val orientation =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION))

        var name: String?
        val parentPath: String
        try {
            name = Uri.parse(path).lastPathSegment
            if (name == null) {
                name = path.substring(path.lastIndexOf("/") + 1)
            }
            parentPath = path.replace(name, "")
        } catch (e: Exception) {
            return null
        }

        return MediaInfo(name, path, parent, parentPath, mimeType, width, height, size).apply {
            this.orientation = orientation
            this.isVideo = false
        }
    }

    private fun generateVideoInfo(cursor: Cursor, contentResolver: ContentResolver): MediaInfo? {
        val path =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
        val parent =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
        val mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))
        val size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
        val width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH))
        val height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
        val resolution =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))

        val duration = try {
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
        } catch (e: Exception) {
            0
        }

        val rotation = try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(path)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
        } catch (e: Exception) {
            0
        }

        var name: String?
        val parentPath: String
        try {
            name = Uri.parse(path).lastPathSegment
            if (name == null) {
                name = path.substring(path.lastIndexOf("/") + 1)
            }
            parentPath = path.replace(name, "")
        } catch (e: Exception) {
            return null
        }

        val thumbCursor: Cursor? = contentResolver.query(
            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID),
            "${MediaStore.Video.Thumbnails.VIDEO_ID}=?",
            arrayOf(id.toString()), null
        )
        var thumbnailPath: String? = null
        if (thumbCursor != null) {
            if (thumbCursor.moveToFirst()) {
                thumbnailPath = thumbCursor.getString(
                    thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
                )
            }
            thumbCursor.close()
        }

        return MediaInfo(name, path, parent, parentPath, mimeType, width, height, size).apply {
            this.duration = duration
            this.resolution = resolution
            this.rotation = rotation
            this.thumbnailPath = thumbnailPath
            this.isVideo = true
        }
    }

    private fun addData(
        info: MediaInfo?,
        infos: MutableList<MediaInfo>,
        files: MutableMap<String, MediaInfo>
    ) {
        if (info?.parent != null) {
            infos.add(info)

            if (!files.containsKey(info.parent!!)) {
                files[info.parent!!] = info
            }

        }
    }

    interface Callback {
        fun complete(
            infos: MutableList<MediaInfo>,
            files: MutableMap<String, MediaInfo>
        )
    }

    companion object {
        const val LOADER_PHOTO = 1
        const val LOADER_VIDEO = 2
    }
}
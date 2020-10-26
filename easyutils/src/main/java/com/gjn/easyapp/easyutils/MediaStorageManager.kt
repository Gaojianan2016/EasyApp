package com.gjn.easyapp.easyutils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MediaStorageManager(var context: Context) {

    private val mediaMap: MutableMap<String, MutableList<MediaInfo>> = mutableMapOf()
    private val medias: MutableList<MediaInfo> = mutableListOf()
    private val dirs: MutableList<MediaInfo> = mutableListOf()
    private val dirNames: MutableList<String> = mutableListOf()
    private var isNotify = false

    var photoSortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
    var videoSortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
    var galleryName = "Gallery"
    var isFindPhoto = true
    var isFindVideo = true

    var onMediaNotifyListener: OnMediaNotifyListener? = null

    fun startScanMedia() {
        if (!isFindPhoto && !isFindVideo) {
            "No search set (isFindPhoto or isFindVideo).".logW(TAG)
            return
        }
        GlobalScope.launch {
            //数据开始扫描
            onMediaNotifyListener?.preLoad()

            scanVideo()

            scanPhoto()

            //刷新dirs的每个文件夹的数量
            mediaMap.forEach { (key, list) ->
                val size = list.size
                for (dir in dirs) {
                    if (dir.parent == key) {
                        dir.dirSize = size
                    }
                }
            }
            //添加一个全部数据的相册
            dirs.add(0, MediaInfo().apply {
                parent = galleryName
                dirSize = medias.size
                path = if (dirSize == 0) {
                    "/"
                } else {
                    medias[0].path
                }
            })

            //数据更新完毕
            onMediaNotifyListener?.complete(dirs)
        }
    }

    private fun scanVideo(): Boolean {
        if (!isFindVideo) return false
        try {
            var mediaInfo: MediaInfo?
            val cachedList: MutableList<MediaInfo> = mutableListOf()
            val cursor: Cursor?
            if (isFindVideo) {
                cursor = sqlQuery(videoUri, videoSortOrder)
                cursor?.run {
                    while (moveToNext()) {
                        mediaInfo = generateVideoInfo(cursor, context.contentResolver)
                        mediaInfo?.let { updateMap(it, cachedList) }
                    }
                    close()
                }
            }
            //后续未满足最小通知数量的数据
            notifyData(cachedList)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun scanPhoto(): Boolean {
        if (!isFindPhoto) return false
        try {
            var mediaInfo: MediaInfo?
            val cachedList: MutableList<MediaInfo> = mutableListOf()
            val cursor: Cursor?
            if (isFindPhoto) {
                cursor = sqlQuery(photoUri, photoSortOrder)
                cursor?.run {
                    while (moveToNext()) {
                        mediaInfo = generatePhotoInfo(cursor)
                        mediaInfo?.let { updateMap(it, cachedList) }
                    }
                    close()
                }
            }
            //后续未满足最小通知数量的数据
            notifyData(cachedList)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun updateMap(mediaInfo: MediaInfo, cachedList: MutableList<MediaInfo>) {
        if (mediaInfo.parent != null) {
            val key = mediaInfo.parent!!
            //更新dir列表
            if (!dirNames.contains(key)) {
                dirNames.add(key)
                dirs.add(mediaInfo)
            }
            //更新map数据
            val list: MutableList<MediaInfo> = mediaMap[key] ?: mutableListOf()
            list.add(mediaInfo)
            mediaMap[key] = list
            //更新缓存列表
            cachedList.add(mediaInfo)
            if (cachedList.size > NOTIFY_SIZE) {
                isNotify = true
            }
            if (isNotify) {
                isNotify = false
                notifyData(cachedList)
                cachedList.clear()
            }
        }
    }

    private fun notifyData(cachedList: MutableList<MediaInfo>) {
        medias.addAll(cachedList)
        //更新数据通知
        onMediaNotifyListener?.updateList(cachedList)
    }


    private fun sqlQuery(uri: Uri, sortOrder: String): Cursor? =
        context.contentResolver.query(
            uri, null, null, null, sortOrder
        )

    private fun generateVideoInfo(cursor: Cursor, contentResolver: ContentResolver): MediaInfo? {

        //基础信息
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
        val parent =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
        val mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))
        val size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
        val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
        val width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH))
        val height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
        val resolution =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))

        //旋转角度
        val rotation: Int
        rotation = try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(path)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
        } catch (e: Exception) {
            0
        }

        //文件名
        var name: String?
        val parentPath: String
        try {
            name = Uri.parse(path).lastPathSegment
            if (name == null) {
                name = path.substring(path.lastIndexOf("/") + 1)
            }
            parentPath = path.replace(name, "")
        } catch (e: java.lang.Exception) {
            return null
        }

        //拦截器

        //获取系统生成的封面
        val thumbCursor = contentResolver.query(
            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID),
            "${MediaStore.Video.Thumbnails.VIDEO_ID}=?",
            arrayOf(id.toString()), null
        )
        var thumbnailPath: String? = null
        thumbCursor?.run {
            if (moveToFirst()) {
                thumbnailPath = getString(getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA))
            }
            close()
        }

        return MediaInfo(name, path, parent, parentPath, mimeType, width, height, size).apply {
            this.duration = duration
            this.resolution = resolution
            this.rotation = rotation
            this.thumbnailPath = thumbnailPath
            this.isVideo = true
        }
    }

    private fun generatePhotoInfo(cursor: Cursor): MediaInfo? {
        //基本信息
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        val parent =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        val mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
        val width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH))
        val height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT))
        val size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
        val orientation =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION))

        //文件名
        var name: String?
        val parentPath: String
        try {
            name = Uri.parse(path).lastPathSegment
            if (name == null) {
                name = path.substring(path.lastIndexOf("/") + 1)
            }
            parentPath = path.replace(name, "")
        } catch (e: java.lang.Exception) {
            return null
        }

        //拦截器

        return MediaInfo(name, path, parent, parentPath, mimeType, width, height, size).apply {
            this.orientation = orientation
        }
    }

    companion object {
        const val TAG = "MediaStorageManager"

        const val SORT_ORDER = "_id DESC LIMIT 1"
        const val NOTIFY_SIZE = 10
        val photoUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val videoUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    }

    interface OnMediaNotifyListener {
        fun preLoad()

        fun updateList(cancelList: MutableList<MediaInfo>)

        fun complete(dirs: MutableList<MediaInfo>)
    }
}
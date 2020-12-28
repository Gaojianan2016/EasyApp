package com.gjn.easyapp.easyutils.media

import android.annotation.SuppressLint
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import com.gjn.easyapp.easyutils.launchMain
import com.gjn.easyapp.easyutils.scanFile
import com.gjn.easyapp.easyutils.urlObtainName

class MediaStorageManager(private val context: Context) {

    private val photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    private val mTask = MediaStorageTask()

    private var mConnection: MediaScannerConnection? = null
    private val videoObserver = MediaObserver(videoUri, TYPE_VIDEO)
    private val photoObserver = MediaObserver(photoUri, TYPE_PHOTO)

    val mMediaList: MutableList<MediaInfo> = mutableListOf()
    val mFileMap: MutableMap<String, MediaInfo> = mutableMapOf()

    var allFileName = "相册"
    var isFindPhoto = true
    var isFindVideo = true
    var photoSortOrder = "date_added DESC"
    var videoSortOrder = "date_added DESC"

    var scanCallback: ScanCallback? = null
    var changeCallback: ChangeCallback? = null
    var filterListener: FilterListener? = null

    fun startScan() {
        connectScanFileListener()
        registerMediaObserver()
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun stopScan() {
        disconnectScanFileListener()
        unregisterMediaObserver()
        mTask.cancel(false)
    }

    private fun registerMediaObserver() {
        context.contentResolver.registerContentObserver(
            videoUri,
            true, videoObserver
        )
        context.contentResolver.registerContentObserver(
            photoUri,
            true, photoObserver
        )
    }

    private fun unregisterMediaObserver() {
        context.contentResolver.unregisterContentObserver(videoObserver)
        context.contentResolver.unregisterContentObserver(photoObserver)
    }

    private fun connectScanFileListener(
        paths: Array<String> = arrayOf(Environment.getExternalStorageDirectory().toString()),
        mimeTypes: Array<String>? = null,
        client: OnScanCompletedListener? = OnScanCompletedListener { path, uri ->
            uri.scanFile(context)
        }
    ) {
        val proxy = MediaScannerProxy(paths, mimeTypes, client)
        mConnection = MediaScannerConnection(context, proxy)
        proxy.connection = mConnection
        mConnection?.connect()
    }

    private fun disconnectScanFileListener() {
        mConnection?.disconnect()
    }

    private fun generatePhotoInfo(cursor: Cursor): MediaInfo? {
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        val name = path.urlObtainName()
        val parent =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        val mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
        val width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH))
        val height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT))
        val size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
        val orientation =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION))
        val addData =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))

        if (name == null) return null
        val parentPath = path.replace(name, "")

        if (filterListener != null) {
            if (!filterListener!!.onFilterPhoto(name)) return null
        }

        return MediaInfo(name, path, parent, parentPath, mimeType, width, height, size, addData)
            .apply {
                this.orientation = orientation
                this.isVideo = false
            }
    }

    private fun generateVideoInfo(cursor: Cursor): MediaInfo? {
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
        val name = path.urlObtainName()
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
        val addData =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))

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

        if (name == null) return null
        val parentPath = path.replace(name, "")


        if (filterListener != null) {
            if (!filterListener!!.onFilterVideo(name)) return null
        }

        val thumbCursor: Cursor? = context.contentResolver.query(
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

        return MediaInfo(name, path, parent, parentPath, mimeType, width, height, size, addData)
            .apply {
                this.duration = duration
                this.resolution = resolution
                this.rotation = rotation
                this.thumbnailPath = thumbnailPath
                this.isVideo = true
            }
    }

    private fun addMedia(info: MediaInfo) {
        if (info.parent != null) {
            mMediaList.add(info)
            if (!mFileMap.containsKey(info.parent!!)) {
                info.dirSize = 1
                mFileMap[info.parent!!] = info
            } else {
                val size = mFileMap[info.parent!!]!!.dirSize
                mFileMap[info.parent!!]!!.dirSize = size + 1
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class MediaStorageTask : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            scanCallback?.preStart()
        }

        override fun onPostExecute(result: Void?) {
            var allSize = 0
            var first: MediaInfo? = null
            mFileMap.forEach { (_, v) ->
                allSize += v.dirSize
                if (first == null) {
                    first = v
                }
            }
            first?.let {
                it.dirSize = allSize
                mFileMap.put(allFileName, it)
            }
            scanCallback?.complete(mMediaList, mFileMap)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (isFindPhoto) {
                //获取照片
                generatePhoto()
            }
            if (isFindVideo) {
                //获取视频
                generateVideo()
            }
            //混合排序列表
            sortList()
            return null
        }

        private fun generatePhoto() {
            val cursor = context.contentResolver.query(
                photoUri, null,
                null, null, photoSortOrder
            )
            cursor?.run {
                moveToFirst()
                do {
                    val info = generatePhotoInfo(this)
                    info?.let { addMedia(it) }
                } while (moveToNext())
                close()
            }
        }

        private fun generateVideo() {
            val cursor = context.contentResolver.query(
                videoUri, null,
                null, null, videoSortOrder
            )
            cursor?.run {
                moveToFirst()
                do {
                    val info = generateVideoInfo(this)
                    info?.let { addMedia(it) }
                } while (moveToNext())
                close()
            }
        }

        private fun sortList() {
            mMediaList.sortWith(kotlin.Comparator { o1, o2 ->
                o2.addTime.compareTo(o1.addTime)
            })
        }
    }

    inner class MediaScannerProxy(
        private val paths: Array<String>,
        private val mimeTypes: Array<String>?,
        private val client: OnScanCompletedListener?
    ) : MediaScannerConnection.MediaScannerConnectionClient {

        var connection: MediaScannerConnection? = null
        private var mNextPath = 0

        override fun onMediaScannerConnected() {
            scanNextPath()
        }

        override fun onScanCompleted(path: String?, uri: Uri?) {
            client?.onScanCompleted(path, uri)
            scanNextPath()
        }

        private fun scanNextPath() {
            if (mNextPath >= paths.size) {
                connection?.disconnect()
                connection = null
                return
            }
            val mimeType: String? = mimeTypes?.get(mNextPath)
            connection?.scanFile(paths[mNextPath], mimeType)
            mNextPath++
        }
    }

    inner class MediaObserver(private val uri: Uri, private val type: Int) : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            launchMain {
                changeCallback?.onChange(
                    selfChange,
                    uri,
                    uri.toString() == "content://media/external"
                )
            }
            val cursor = context.contentResolver.query(
                this.uri, null, null,
                null, "_id DESC LIMIT 1"
            )
            cursor?.run {
                if (cursor.moveToNext()) {
                    val mediaInfo = when (type) {
                        TYPE_PHOTO -> generatePhotoInfo(cursor)
                        TYPE_VIDEO -> generateVideoInfo(cursor)
                        else -> null
                    }
                    launchMain {
                        changeCallback?.onChangeMediaInfo(mediaInfo)
                    }
                }
                close()
            }

        }
    }

    interface ScanCallback {
        fun preStart()

        fun complete(
            infoList: MutableList<MediaInfo>,
            fileList: MutableMap<String, MediaInfo>
        )
    }

    interface ChangeCallback {
        fun onChange(selfChange: Boolean, uri: Uri?, isMediaExternal: Boolean)

        fun onChangeMediaInfo(mediaInfo: MediaInfo?)
    }

    abstract class FilterListener {
        open fun onFilterVideo(fileName: String): Boolean = true

        open fun onFilterPhoto(fileName: String): Boolean = true
    }

    companion object {
        const val TYPE_PHOTO = 0
        const val TYPE_VIDEO = 1
    }
}
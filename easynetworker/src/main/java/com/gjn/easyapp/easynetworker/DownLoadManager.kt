package com.gjn.easyapp.easynetworker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.gjn.easyapp.easyutils.*
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DownLoadManager(private val activity: FragmentActivity) {

    var downLoadStatus = DOWNLOAD_PRE
        private set
    var onDownLoadListener: OnDownLoadListener? = null

    private val mOkHttpClient = OkHttpClient()
    private var mCall: Call? = null

    fun resetDownLoad() {
        downLoadStatus = DOWNLOAD_PRE
    }

    fun cancelDownLoad() {
        mCall?.cancel()
    }

    fun openFile(file: File?) {
        if (file == null) return
        if (!file.exists()) return
        //安卓8.0之后需要申请未知来源权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!activity.packageManager.canRequestPackageInstalls()) {
                activity.simpleActivityResult(
                    Intent(
                        Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:${activity.packageName}")
                    )
                ) { _, _ ->
                    openFile(file)
                }
                return
            }
        }
        FileUtils.openFile(activity, file)
    }

    fun downLoadFile(url: String, path: String, name: String? = url.urlObtainName()) {
        if (url.isEmpty() || path.isEmpty() || name.isNullOrEmpty()) return
        if (downLoadStatus == DOWNLOAD_SUCCESS) {
            openFile(File(path, name))
            return
        }
        if (downLoadStatus == DOWNLOAD_ING) {
            println("file id downloading")
            return
        }
        activity.requestWRPermission {
            //准备下载
            downLoadStatus = DOWNLOAD_ING
            mCall = mOkHttpClient.newCall(Request.Builder().url(url).build())
            mCall?.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    downLoadStatus = DOWNLOAD_FAIL
                    launchMain {
                        onDownLoadListener?.error(call, e)
                        onDownLoadListener?.fail(call)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val folder = File(path)
                    if (!folder.exists()) {
                        if (folder.mkdirs()) {
                            println("create folder $folder")
                        }
                    }
                    val file = File(path, name)
                    if (file.exists()) {
                        if (file.delete()) {
                            println("delete file $file")
                        }
                    }
                    if (file.createNewFile()) {
                        println("createNewFile $file")
                    }
                    if (downloadStream(call, response, file)) {
                        launchMain {
                            downLoadStatus = DOWNLOAD_SUCCESS
                            onDownLoadListener?.success(call, file)
                        }
                    } else {
                        launchMain {
                            downLoadStatus = DOWNLOAD_FAIL
                            onDownLoadListener?.fail(call)
                        }
                    }
                }
            })
        }
    }

    private fun downloadStream(call: Call, response: Response, file: File): Boolean {
        val length = response.header(LENGTH)?.toInt() ?: -1
        var iStream: InputStream? = null
        var fos: FileOutputStream? = null
        val buffer = ByteArray(streamByte(length))
        var len = 0
        var readStream = 0
        launchMain {
            onDownLoadListener?.start(call, file, file.name, length)
        }
        try {
            iStream = response.body!!.byteStream()
            fos = FileOutputStream(file)
            while (iStream.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
                readStream += len
                launchMain {
                    onDownLoadListener?.downLoading(call, readStream, length)
                }
            }
            fos.flush()
            launchMain {
                onDownLoadListener?.end(call)
            }
            return true
        } catch (e: Exception) {
            if (file.exists()) {
                if (file.delete()) {
                    println("file error delete $file")
                }
            }
            launchMain {
                onDownLoadListener?.error(call, e)
                onDownLoadListener?.fail(call)
            }
        } finally {
            try {
                iStream?.close()
                fos?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        launchMain {
            onDownLoadListener?.end(call)
        }
        return false
    }

    private fun streamByte(length: Int) =
        when {
            length > 1024 * 1024 * 1024 -> 1024 * 1024 * 50
            length > 1024 * 1024 * 512 -> 1024 * 1024 * 30
            length > 1024 * 1024 * 100 -> 1024 * 1024 * 10
            length > 1024 * 1024 * 10 -> 1024 * 1024
            length > 1024 * 1024 -> 1024 * 512
            else -> 1024
        }

    companion object {
        const val DOWNLOAD_PRE = 0x111
        const val DOWNLOAD_ING = 0x222
        const val DOWNLOAD_SUCCESS = 0x333
        const val DOWNLOAD_FAIL = 0x444

        private const val LENGTH = "Content-Length"
        private const val TYPE = "Content-Type"
    }
}

abstract class SimpleDownLoadListener : OnDownLoadListener {
    override fun start(call: Call, file: File, name: String, length: Int) {
    }

    override fun downLoading(call: Call, readStream: Int, totalStream: Int) {
    }

    override fun success(call: Call, file: File) {
    }

    override fun fail(call: Call) {
    }

    override fun error(call: Call, tr: Throwable) {
    }

    override fun end(call: Call) {
    }
}

interface OnDownLoadListener {
    fun start(call: Call, file: File, name: String, length: Int)

    fun downLoading(call: Call, readStream: Int, totalStream: Int)

    fun success(call: Call, file: File)

    fun fail(call: Call)

    fun error(call: Call, tr: Throwable)

    fun end(call: Call)
}
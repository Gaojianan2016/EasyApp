package com.gjn.easyapp.easynetworker

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.gjn.easyapp.easyutils.HttpsUtils
import com.gjn.easyapp.easyutils.getUrlLastName
import com.gjn.easyapp.easyutils.launchApplicationMain
import com.gjn.easyapp.easyutils.openFile
import com.gjn.easyapp.easyutils.packageNameUri
import com.gjn.easyapp.easyutils.quickActivityResult
import com.gjn.easyapp.easyutils.requestWRPermission
import com.gjn.easyapp.easyutils.tryClose
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

class DownLoadManager(
    private val activity: FragmentActivity,
    var getFileSizeBlock: (Response) -> Int = {
        it.header(LENGTH)?.toInt() ?: -1
    },
    var downloadStartBlock: ((file: File, name: String, length: Int) -> Unit)? = null,
    var downloadingBlock: ((writeLength: Int, length: Int) -> Unit)? = null,
    var downloadSuccessBlock: ((file: File) -> Unit)? = null,
    var downloadFailBlock: ((code: Int, throwable: Throwable) -> Unit)? = null,
    var downloadCompleteBlock: (() -> Unit)? = null,
) {

    var mOkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .sslSocketFactory(HttpsUtils.createSSLSocketFactory()!!, HttpsUtils.mX509TrustManager)
        .hostnameVerifier(HttpsUtils.mHostnameVerifier)
        .addInterceptor(RetrofitManager.LoggingAndCustomRequestInterceptor())
        .build()

    var mStatus = DOWNLOAD_PRE
        private set
    var mCall: Call? = null
        private set

    //停止下载 会改变下载状态
    fun stopDownLoad() {
        mStatus = DOWNLOAD_PRE
        cancelDownLoad()
    }

    //取消下载 不会改变下载状态
    fun cancelDownLoad() {
        mCall?.cancel()
    }

    fun openFile(file: File?) {
        if (file == null) return
        if (!file.exists()) return
        //如果是apk 安卓8.0之后需要申请未知来源权限
        if (file.path.endsWith(".apk") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!activity.packageManager.canRequestPackageInstalls()) {
                activity.quickActivityResult(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, activity.packageNameUri)) { _, _ ->
                    openFile(file)
                }
                return
            }
        }
        activity.openFile(file)
    }

    fun downLoadFile(url: String, path: String, name: String? = url.getUrlLastName()) {
        if (url.isEmpty() || path.isEmpty() || name.isNullOrEmpty()) {
            //下载内容为空
            downloadFailBlock?.invoke(CODE_DOWNLOAD_EMPTY, NullPointerException("url or path or name is null"))
            return
        }
        if (mStatus == DOWNLOAD_SUCCESS) {
            //已下载成功
            val file = File(path, name)
            if (file.exists()) {
                //文件存在
                downloadSuccessBlock?.invoke(File(path, name))
            } else {
                //文件不存在
                mStatus = DOWNLOAD_PRE
                downLoadFile(url, path, name)
            }
            return
        }
        //下载中
        if (mStatus == DOWNLOAD_ING) return
        activity.requestWRPermission {
            //准备下载
            mStatus = DOWNLOAD_ING
            mCall = mOkHttpClient.newCall(Request.Builder().url(url).build())
            mCall?.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mStatus = DOWNLOAD_FAIL
                    launchApplicationMain {
                        downloadFailBlock?.invoke(CODE_DOWNLOAD_ERROR, e)
                        downloadCompleteBlock?.invoke()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val folder = File(path)
                    if (!folder.exists()) if (folder.mkdirs()) println("create folder $folder")
                    val file = File(path, name)
                    if (file.exists()) if (file.delete()) println("delete file $file")
                    if (file.createNewFile()) println("createNewFile $file")
                    if (downloadStream(response, file)) {
                        launchApplicationMain {
                            mStatus = DOWNLOAD_SUCCESS
                            downloadSuccessBlock?.invoke(file)
                            downloadCompleteBlock?.invoke()
                        }
                    } else {
                        launchApplicationMain {
                            mStatus = DOWNLOAD_FAIL
                            downloadCompleteBlock?.invoke()
                        }
                    }
                }
            })
        }
    }

    private fun downloadStream(response: Response, file: File): Boolean {
        val length = getFileSizeBlock.invoke(response)
        var iStream: InputStream? = null
        var fos: FileOutputStream? = null
        val buffer = ByteArray(streamByte(length))
        var len: Int
        var writeLength = 0
        launchApplicationMain {
            //准备开始写入
            downloadStartBlock?.invoke(file, file.name, length)
        }
        try {
            iStream = response.body!!.byteStream()
            fos = FileOutputStream(file)
            while (iStream.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
                writeLength += len
                launchApplicationMain {
                    downloadingBlock?.invoke(writeLength, length)
                }
            }
            fos.flush()
            return true
        } catch (e: Exception) {
            if (file.exists()) if (file.delete()) println("file error delete $file")
            launchApplicationMain {
                downloadFailBlock?.invoke(CODE_DOWNLOAD_WRITE_ERROR, e)
            }
        } finally {
            iStream?.tryClose()
            fos?.tryClose()
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

        const val CODE_DOWNLOAD_EMPTY = 0x1000
        const val CODE_DOWNLOAD_ERROR = 0x1001
        const val CODE_DOWNLOAD_WRITE_ERROR = 0x1002

        private const val LENGTH = "Content-Length"
        private const val TYPE = "Content-Type"
    }
}

abstract class SimpleDownLoadListener : OnDownLoadListener {
    override fun downLoadStatus(status: Int, tip: String) {
    }

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

    fun downLoadStatus(status: Int, tip: String)

    fun start(call: Call, file: File, name: String, length: Int)

    fun downLoading(call: Call, readStream: Int, totalStream: Int)

    fun success(call: Call, file: File)

    fun fail(call: Call)

    fun error(call: Call, tr: Throwable)

    fun end(call: Call)
}
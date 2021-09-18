package com.gjn.easyapp.easyutils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.io.Serializable

/**
 * Intent 是否可用
 * */
fun Context.intentIsAvailable(intent: Intent) =
    packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0

/**
 * filePath获取安装app Intent
 * */
fun Context.getInstallAppIntent(path: String): Intent? {
    if (path.isEmpty()) return null
    return getInstallAppIntent(path.file())
}

/**
 * file获取安装app Intent
 * */
fun Context.getInstallAppIntent(file: File): Intent? {
    if (!file.exists()) return null
    return getLocalFileUri(file).getInstallAppIntent()
}

/**
 * uri获取安装app Intent
 * */
fun Uri.getInstallAppIntent(): Intent? {
    try {
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(this@getInstallAppIntent, "application/vnd.android.package-archive")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
        }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * 包名获取卸载app Intent
 * */
fun String.getUninstallAppIntent() =
    Intent(Intent.ACTION_DELETE).apply { data = toPackageNameUri() }
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

/**
 * 包名获取app入口 Intent
 * */
fun Context.getLaunchAppIntent(pkg: String): Intent? {
    val launcherActivity = getLauncherActivity(pkg)
    if (launcherActivity.isEmpty()) return null
    return Intent().apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        setClassName(pkg, launcherActivity)
    }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

const val QQ_PACKAGE_NAME = "com.tencent.mobileqq"
const val WECHAT_PACKAGE_NAME = "com.tencent.mm"

fun Context.openWeChat() {
    openApp(WECHAT_PACKAGE_NAME)
}

fun Context.openQQ() {
    openApp(QQ_PACKAGE_NAME)
}

fun Context.callPhone(phone: String) {
    startActivity(Intent(Intent.ACTION_DIAL).apply {
        data = "tel:${phone}".uri()
    })
}

fun FragmentActivity.takePictures(folderPath: String, block: (Int, Intent?) -> Unit): String {
    val fileName = "$folderPath${System.currentTimeMillis()}.png"
    val folder = folderPath.file()
    if (!folder.exists()) {
        folder.mkdirs()
    }
    simpleActivityResult(Intent().also {
        it.action = MediaStore.ACTION_IMAGE_CAPTURE
        it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        it.putExtra(MediaStore.EXTRA_OUTPUT, getLocalFileUri(fileName.file()))
    }) { code, data ->
        block.invoke(code, data)
        fileName.file().notifyMediaFile(this)
    }
    return fileName
}

fun Context.openBrowser(url: String?) {
    if (url.isNullOrEmpty()) return
    startActivity(Intent(Intent.ACTION_VIEW).also {
        it.data = Uri.parse(url)
    })
}

fun Context.clipData(str: String) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText(null, str))
}

fun Intent.put(map: Map<String, Any?>): Intent {
    map.forEach { (k, v) ->
        when (v) {
            is Int -> putExtra(k, v)
            is Float -> putExtra(k, v)
            is Double -> putExtra(k, v)
            is Long -> putExtra(k, v)
            is String -> putExtra(k, v)
            is Boolean -> putExtra(k, v)
            is Byte -> putExtra(k, v)
            is Char -> putExtra(k, v)
            is Short -> putExtra(k, v)
            is CharSequence -> putExtra(k, v)
            is Parcelable -> putExtra(k, v)
            is Serializable -> putExtra(k, v)
            is Bundle -> putExtra(k, v)
            is Array<*> -> putExtra(k, v)
            is ArrayList<*> -> putExtra(k, v)
        }
    }
    return this
}
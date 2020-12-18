package com.gjn.easyapp.easyutils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

const val QQ_PACKAGE_NAME = "com.tencent.mobileqq"
const val WECHAT_PACKAGE_NAME = "com.tencent.mm"

fun Context.openWeChat() {
    openApp(WECHAT_PACKAGE_NAME)
}

fun Context.openQQ() {
    openApp(QQ_PACKAGE_NAME)
}

fun Context.openApp(packageName: String) {
    if (isInstalled(packageName)) startActivity(
        Intent(packageManager.getLaunchIntentForPackage(packageName))
    )
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
        it.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getFileUri(this, fileName.file()))
    }) { code, data ->
        block.invoke(code, data)
        fileName.file().scanFile(this)
    }
    return fileName
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
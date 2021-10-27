package com.gjn.easyapp.easyutils

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.io.Serializable

/**
 * Intent 是否可用
 * */
fun Context.intentIsAvailable(intent: Intent) =
    packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0

/**
 * 包名获取组件 Intent
 * */
fun String.getComponentIntent(
    className: String,
    bundle: Bundle? = null,
    isNewTask: Boolean = false
): Intent? {
    if (isEmpty() || className.isEmpty()) return null
    val intent = Intent().apply {
        if (bundle != null) putExtras(bundle)
        component = ComponentName(this@getComponentIntent, className)
    }
    return if (isNewTask) intent.addNewTaskFlag() else intent
}

const val QQ_PACKAGE_NAME = "com.tencent.mobileqq"
const val WECHAT_PACKAGE_NAME = "com.tencent.mm"

fun Context.openWeChat() {
    openApp(WECHAT_PACKAGE_NAME)
}

fun Context.openQQ() {
    openApp(QQ_PACKAGE_NAME)
}

/**
 * 分享text
 * */
fun Context.shareText(content: CharSequence) {
    if (content.isEmpty()) return
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, content)
    }
    startActivity(Intent.createChooser(intent, "").addNewTaskFlag())
}

/**
 * 分享图片
 * */
fun Context.shareImage(vararg paths: String) {
    if (paths.isEmpty()) return
    val files = mutableListOf<File>()
    paths.forEach {
        files.add(it.file())
    }
    shareTextImage(null, *files.toTypedArray())
}

/**
 * 分享文字图片
 * */
fun Context.shareTextImage(content: CharSequence?, vararg files: File) {
    if (files.isEmpty()) return
    val uris = java.util.ArrayList<Uri>()
    for (file in files) {
        if (file.exists()) {
            uris.add(getLocalFileUri(file))
        }
    }
    val intent = if (files.size == 1) {
        Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_STREAM, uris[0])
        }
    } else {
        //todo 多图片分享 错误
        // Key android.intent.extra.TEXT expected ArrayList<CharSequence> but value was a java.lang.String.
        Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_TEXT, content)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        }
    }
    startActivity(Intent.createChooser(intent, "").addNewTaskFlag())
}

/**
 * 拨打电话 无需权限
 * */
fun Context.dialPhone(phone: String) {
    if (phone.isEmpty()) return
    val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".uri())
    startActivity(intent.addNewTaskFlag())
}

/**
 * 发送短信
 * */
fun Context.sendSms(phone: String, content: String) {
    if (phone.isEmpty() || content.isEmpty()) return
    val intent = Intent(Intent.ACTION_SENDTO, "smsto:$phone".uri()).apply {
        putExtra("sms_body", content)
    }
    startActivity(intent.addNewTaskFlag())
}

/**
 * 打开浏览器
 * */
fun Context.openBrowser(url: String) {
    if (url.isEmpty()) return
    val intent = Intent(Intent.ACTION_VIEW, url.uri())
    startActivity(intent.addNewTaskFlag())
}

/**
 * 快速摄影
 * */
fun FragmentActivity.quickPhotography(file: File, block: (Int, Intent?) -> Unit): String {
    if (!file.createParentDir()) return file.fileName()
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        putExtra(MediaStore.EXTRA_OUTPUT, getLocalFileUri(file))
    }
    quickActivityResult(intent) { code, data ->
        block.invoke(code, data)
        file.notifyMediaFile(this)
    }
    return file.fileName()
}

///////////////////////////////////////
///    intent 快捷方式
///////////////////////////////////////

inline fun <reified T> Context.intentOf(vararg pairs: Pair<String, *>) =
    intentOf<T>(bundleOf(*pairs))

inline fun <reified T> Context.intentOf(bundle: Bundle): Intent =
    Intent(this, T::class.java).apply { putExtras(bundle) }

inline fun <reified T> Fragment.intentOf(vararg pairs: Pair<String, *>) =
    intentOf<T>(bundleOf(*pairs))

inline fun <reified T> Fragment.intentOf(bundle: Bundle): Intent =
    Intent(this.context, T::class.java).apply { putExtras(bundle) }

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

fun Intent.addNewTaskFlag() = addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
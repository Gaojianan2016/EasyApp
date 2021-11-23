package com.gjn.easyapp.easyutils

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import java.io.File

import java.util.ArrayList as JList

const val QQ_PACKAGE_NAME = "com.tencent.mobileqq"
const val WECHAT_PACKAGE_NAME = "com.tencent.mm"

fun Context.openWeChat() {
    openApp(WECHAT_PACKAGE_NAME)
}

fun Context.openQQ() {
    openApp(QQ_PACKAGE_NAME)
}

///////////////////////////////////////
///    intent系统调用
///////////////////////////////////////

fun Context.dial(phone: String) {
    if (phone.isEmpty()) return
    startActivity(Intent(Intent.ACTION_DIAL, "tel:$phone".uri).addNewTaskFlag())
}

fun Context.sendSMS(phone: String, content: String) {
    if (phone.isEmpty() || content.isEmpty()) return
    startActivity(
        Intent(Intent.ACTION_SENDTO, "smsto:$phone".uri)
            .apply { putExtra("sms_body", content) }
            .addNewTaskFlag()
    )
}

fun Context.email(email: String, subject: String = "", content: String = "") {
    if (email.isEmpty()) return
    startActivity(Intent(Intent.ACTION_SENDTO, "mailto:".uri)
        .apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            if (subject.isNotEmpty()) putExtra(Intent.EXTRA_SUBJECT, subject)
            if (content.isNotEmpty()) putExtra(Intent.EXTRA_TEXT, content)
        }
        .addNewTaskFlag()
    )
}

fun Context.browser(url: String) {
    if (url.isEmpty()) return
    startActivity(Intent(Intent.ACTION_VIEW, url.uri).addNewTaskFlag())
}

/**
 * 分享text
 * */
fun Context.shareText(content: CharSequence) {
    if (content.isEmpty()) return
    startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, content)
            }, null
        ).addNewTaskFlag()
    )
}

/**
 * 分享图片
 * */
fun Context.shareImage(vararg paths: String) {
    if (paths.isEmpty()) return
    val files = mutableListOf<File>().apply {
        paths.forEach {
            add(it.toFile())
        }
    }
    shareTextImage(files = files.toTypedArray())
}

/**
 * 分享文字图片
 * */
fun Context.shareTextImage(content: CharSequence? = null, vararg files: File) {
    if (files.isEmpty()) return
    val uris = JList<Uri>()
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
        Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_TEXT, arrayOf(content))
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        }
    }
    startActivity(Intent.createChooser(intent, null).addNewTaskFlag())
}

/**
 * 快速摄影
 * */
fun FragmentActivity.quickShoot(file: File, block: (Int, Intent?) -> Unit = { _, _ -> }): String {
    if (!file.createParentDir()) return file.name
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        putExtra(MediaStore.EXTRA_OUTPUT, getLocalFileUri(file))
    }
    quickActivityResult(intent) { code, data ->
        block.invoke(code, data)
        file.notifyMediaFile(this)
    }
    return file.name
}


///////////////////////////////////////
///    intent便捷生成方式
///////////////////////////////////////

inline fun <reified T> Context.intentOf(vararg pairs: Pair<String, *>) =
    intentOf<T>(bundleOf(*pairs))

inline fun <reified T> Context.intentOf(bundle: Bundle): Intent =
    Intent(this, T::class.java).apply { putExtras(bundle) }

fun Intent.addNewTaskFlag() = addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

fun Intent.addSingleTopFlag() = addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)


///////////////////////////////////////
///    intent其他操作
///////////////////////////////////////

/**
 * Intent 是否可用
 * */
fun Context.intentIsAvailable(intent: Intent): Boolean =
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
package com.gjn.easyapp.easyutils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

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

fun Context.clipData(str: String) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText(null, str))
}
package com.gjn.easyapp.easyutils

import android.content.*

fun Context.openWeChat() {
    startActivity(Intent(Intent.ACTION_VIEW).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
    })
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

fun Context.assetsStr(fileName: String): String {
    try {
        val stream = assets.open(fileName)
        val size = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        return String(buffer)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}
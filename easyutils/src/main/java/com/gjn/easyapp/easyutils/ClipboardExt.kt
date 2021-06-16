package com.gjn.easyapp.easyutils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * 获取剪切板管理器
 * */
fun Context.getClipboardManager() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

/**
 * 剪切Text
 * */
fun Context.copyClipboardText(label: CharSequence? = packageName, text: CharSequence) {
    getClipboardManager().setPrimaryClip(ClipData.newPlainText(label, text))
}

/**
 * 清空剪切板
 * */
fun Context.clearClipboard() {
    copyClipboardText(null, "")
}

/**
 * 获取剪切板标签
 * */
fun Context.getClipboardLabel() = getClipboardManager().primaryClipDescription?.label ?: ""

/**
 * 获取剪切板Text
 * */
fun Context.getClipboardText(): CharSequence {
    getClipboardManager().primaryClip?.let {
        if (it.itemCount > 0) {
            val text = it.getItemAt(0).coerceToText(this)
            if (text != null) {
                return text
            }
        }
    }
    return ""
}

/**
 * 添加剪切板改变监听
 * */
fun Context.addClipboardChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener) {
    getClipboardManager().addPrimaryClipChangedListener(listener)
}

/**
 * 移除剪切板改变监听
 * */
fun Context.removeClipboardChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener) {
    getClipboardManager().removePrimaryClipChangedListener(listener)
}
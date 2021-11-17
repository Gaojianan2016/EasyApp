package com.gjn.easyapp.easyutils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * 剪切Text
 * */
fun Context.copyClipboardText(text: CharSequence, label: CharSequence? = packageName) {
    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
}

/**
 * 清空剪切板
 * */
fun Context.clearClipboard() {
    copyClipboardText("",null)
}

/**
 * 获取剪切板标签
 * */
fun Context.getClipboardLabel() = clipboardManager.primaryClipDescription?.label ?: ""

/**
 * 获取剪切板Text
 * */
fun Context.getClipboardText(): CharSequence {
    clipboardManager.primaryClip?.let {
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
    clipboardManager.addPrimaryClipChangedListener(listener)
}

/**
 * 移除剪切板改变监听
 * */
fun Context.removeClipboardChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener) {
    clipboardManager.removePrimaryClipChangedListener(listener)
}
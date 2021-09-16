package com.gjn.easyapp.easyutils

import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

/**
 * file写入字符串
 * @param content 内容
 * @param append  是否拼接
 * */
fun File.writeString(
    content: String,
    append: Boolean = false,
    charset: Charset = Charsets.UTF_8
): Boolean {
    if (content.isEmpty()) return false
    if (!createOrExistsFile()) {
        println("create ${fileName()} failed.")
        return false
    }
    try {
        if (append) appendText(content, charset) else writeText(content, charset)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * file写入输入流
 * @param stream 流
 * @param append 是否拼接
 * */
fun File.writeInputStream(stream: InputStream?, append: Boolean = false): Boolean {
    if (stream == null) return false
    if (!createOrExistsFile()) {
        println("create ${fileName()} failed.")
        return false
    }
    try {
        if (append) appendBytes(stream.readBytes()) else writeBytes(stream.readBytes())
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}
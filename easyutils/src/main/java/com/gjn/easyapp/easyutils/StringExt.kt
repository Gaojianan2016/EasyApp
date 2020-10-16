package com.gjn.easyapp.easyutils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat

fun String.toMd5(): String {
    try {
        //获取摘要器 MessageDigest
        val messageDigest = MessageDigest.getInstance("MD5")
        //通过摘要器对字符串的二进制字节数组进行hash计算
        val digest = messageDigest.digest(toByteArray())
        val stringBuilder = StringBuilder()
        for (byte in digest) {
            //循环每个字符 将计算结果转化为正整数
            val digestInt = byte.toInt() and 0xff
            //将10进制转化为较短的16进制
            val hex = Integer.toHexString(digestInt)
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hex.length < 2) stringBuilder.append(0)
            //将循环结果添加到缓冲区
            stringBuilder.append(hex)
        }
        return stringBuilder.toString()
    } catch (e: NoSuchAlgorithmException) {
        println("MD5 算法不存在")
    }
    return this
}

fun Double.decimalFormat(): String = this.decimalFormat(2)

fun Double.decimalFormat(len: Int): String {
    val pattern = StringBuilder("0.")
    for (i in 0 until len) {
        pattern.append("0")
    }
    return DecimalFormat(pattern.toString()).format(this)
}

fun Long.dataFormat(): String = dataFormat("yyyy-MM-dd HH:mm:ss")

fun Long.dataFormat(format: String): String = SimpleDateFormat(format).format(this)

fun Char.isChineseChar(): Boolean {
    val ub = Character.UnicodeBlock.of(this)
    return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
}

fun String.hasChinese(): Boolean {
    for (c in this) {
        if (c.isChineseChar()) {
            return true
        }
    }
    return false
}

fun String.isChinese(): Boolean {
    for (c in this) {
        if (!c.isChineseChar()) {
            return false
        }
    }
    return true
}

fun Long.byteToStr(): String = when {
    this >= 1024 * 1024 * 1024 -> "${(this / 1024 / 1024 / 1024.toDouble()).decimalFormat()}GB"
    this >= 1024 * 1024 -> "${(this / 1024 / 1024.toDouble()).decimalFormat()}MB"
    this >= 1024 -> "${(this / 1024.toDouble()).decimalFormat()}KB"
    else -> "${(this.toDouble()).decimalFormat()}B"
}




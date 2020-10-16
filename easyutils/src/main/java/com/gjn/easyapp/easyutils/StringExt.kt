package com.gjn.easyapp.easyutils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.abs

const val MINUTE = 60.toLong()
const val HOUR = 60 * 60.toLong()
const val DAY = 60 * 60 * 24.toLong()
const val WEEK = 60 * 60 * 24 * 7.toLong()
const val MONTH = 60 * 60 * 24 * 30.toLong()
const val YEAR = 60 * 60 * 24 * 365.toLong()

const val KB = 1024.toLong()
const val MB = 1024 * 1024.toLong()
const val GB = 1024 * 1024 * 1024.toLong()
const val TB = 1024 * 1024 * 1024 * 1024.toLong()

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

@JvmOverloads
fun Double.decimalFormat(prefix: String = "", suffix: String? = null, len: Int = 2): String {
    val pattern = StringBuilder("${prefix}0.")
    for (i in 0..len) {
        pattern.append("0")
    }
    if (suffix != null) {
        pattern.append(suffix)
    }
    return DecimalFormat(pattern.toString()).format(this)
}

@JvmOverloads
fun Long.dataFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(format).format(this)

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
    this >= GB -> (this / 1024 / 1024 / 1024.toDouble()).decimalFormat(suffix = "GB")
    this >= MB -> (this / 1024 / 1024.toDouble()).decimalFormat(suffix = "MB")
    this >= KB -> (this / 1024.toDouble()).decimalFormat(suffix = "KB")
    else -> (this.toDouble()).decimalFormat(suffix = "B")
}

fun String.hidePhone(): String = hideSubstring(3, 4)

fun String.hideName(): String = hideSubstring(1, 1)

fun String.hideSubstring(start: Int, end: Int): String {
    if (length < 3) return this
    val result = StringBuilder()
    val head = substring(0, start)
    val foot = substring(length - end)
    val surplus = length - start - end
    result.append(head)
    for (i in 0..surplus) {
        result.append("*")
    }
    result.append(foot)
    return result.toString()
}

fun Long.elapsedTime(): String {
    val second = abs(this) / 1000
    return when {
        second >= YEAR -> "${second / YEAR}年前"
        second >= MONTH -> "${second / MONTH}个月前"
        second >= WEEK -> "${second / WEEK}周前"
        second >= DAY * 2 -> "两天前"
        second >= DAY -> "一天前"
        second >= HOUR -> "${second / HOUR}小时前"
        second >= MINUTE -> "${second / MINUTE}分钟前"
        second > 5 -> "${second % 60}秒前"
        else -> "刚刚"
    }
}

@JvmOverloads
fun Long.toSecondFormat(isChinese: Boolean = false): String {
    val second = this / 1000
    return when {
        second > HOUR -> if (isChinese) String.format(
            "%02d时%02d分%02d秒",
            second / HOUR,
            second / MINUTE % MINUTE,
            second % MINUTE
        ) else String.format(
            "%02d:%02d:%02d",
            second / HOUR,
            second / MINUTE % MINUTE,
            second % MINUTE
        )
        second > MINUTE -> if (isChinese) String.format(
            "%02d分%02d秒",
            second / MINUTE,
            second % MINUTE
        ) else String.format("%02d:%02d", second / MINUTE, second % MINUTE)
        second > 0 -> if (isChinese) String.format(
            "00分%02d秒",
            second % MINUTE
        ) else String.format("00:%02d", second % MINUTE)
        else -> if (isChinese) "00分00秒" else "00:00"
    }
}



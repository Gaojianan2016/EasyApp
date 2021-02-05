package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
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

/**
 * MD5加密
 * */
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
        e.printStackTrace()
    }
    return this
}

fun String.toSpannableStringBuilder(): SpannableStringBuilder = SpannableStringBuilder(this)

/**
 * 格式转译字符  \ -> \\
 * */
fun String.escapeSpecialWord(): String {
    if (isEmpty()) return this
    var result = this
    val specials = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
    for (special in specials) {
        if (result.contains(special)) {
            result = this.replace(special, "\\$special")
        }
    }
    return result
}

/**
 * 强制取小数点后几位 0.110 0.25 0.10 1.00
 * */
@JvmOverloads
fun Double.decimalFormat(prefix: String? = null, suffix: String? = null, len: Int = 2): String {
    val pattern = StringBuilder()
    if (!prefix.isNullOrEmpty()) pattern.append(prefix)
    pattern.append("0.")
    for (i in 0 until len) {
        pattern.append("0")
    }
    if (!suffix.isNullOrEmpty()) pattern.append(suffix)
    return DecimalFormat(pattern.toString()).format(this)
}

@SuppressLint("SimpleDateFormat")
@JvmOverloads
fun Long.dataFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(format).format(this)

fun String.hasChinese(): Boolean {
    if (this.isEmpty()) return false
    for (c in this) {
        if (c.isChineseChar()) return true
    }
    return false
}

fun String.isChinese(): Boolean {
    if (this.isEmpty()) return false
    for (c in this) {
        if (!c.isChineseChar()) return false
    }
    return true
}

fun Char.isChineseChar(): Boolean {
    val ub = Character.UnicodeBlock.of(this)
    return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
}

fun String.hasEmoji(): Boolean {
    if (this.isEmpty()) return false
    for (c in this) {
        if (c.isEmojiChar()) return true
    }
    return false
}

fun Char.isEmojiChar(): Boolean = !(this.toInt() == 0x0
        || this.toInt() == 0x9
        || this.toInt() == 0xA
        || this.toInt() == 0xD
        || this.toInt() in 0x20..0xD7FF
        || this.toInt() in 0xE000..0xFFFD)

/**
 * 字节转gb mb kb字符串 0.5T 1.20G 60.00M 798.35K 666b
 * */
fun Long.byteToStr(): String = when {
    this >= TB -> (this / 1024 / 1024 / 1024 / 1024.toDouble()).decimalFormat(suffix = "T")
    this >= GB -> (this / 1024 / 1024 / 1024.toDouble()).decimalFormat(suffix = "G")
    this >= MB -> (this / 1024 / 1024.toDouble()).decimalFormat(suffix = "M")
    this >= KB -> (this / 1024.toDouble()).decimalFormat(suffix = "K")
    else -> (this.toDouble()).decimalFormat(suffix = "b", len = 0)
}

fun String.hidePhone(): String = hideSubstring(3, 4)

fun String.hideName(): String = hideSubstring(1, 1)

/**
 * 隐藏中间字段 177****1234  张*良 张*
 * */
fun String.hideSubstring(start: Int, end: Int): String {
    if (length < 2) return this
    if (length == 2) return "${substring(0, 1)}*"
    val result = StringBuilder()
    val head = substring(0, start)
    val foot = substring(length - end)
    val surplus = length - start - end
    result.append(head)
    for (i in 0 until surplus) {
        result.append("*")
    }
    result.append(foot)
    return result.toString()
}

/**
 * 秒转时间格式 01:50:00 or 16:18 中文版 01时50分00秒 or 16分18秒
 * */
@JvmOverloads
fun Int.toSecondFormat(isChinese: Boolean = false): String =
    (this * 1000).toLong().toSecondFormat(isChinese)

/**
 * 毫秒转时间格式 01:50:00 or 16:18 中文版 01时50分00秒 or 16分18秒
 * */
@JvmOverloads
fun Long.toSecondFormat(isChinese: Boolean = false): String {
    val second = this / 1000
    return when {
        second > HOUR -> if (isChinese) String.format(
            "%02d时%02d分%02d秒",
            second / HOUR, second / MINUTE % MINUTE, second % MINUTE
        ) else String.format(
            "%02d:%02d:%02d",
            second / HOUR, second / MINUTE % MINUTE, second % MINUTE
        )
        second > MINUTE -> if (isChinese) String.format(
            "%02d分%02d秒", second / MINUTE, second % MINUTE
        )
        else String.format("%02d:%02d", second / MINUTE, second % MINUTE)
        second > 0 -> if (isChinese) String.format("00分%02d秒", second % MINUTE)
        else String.format("00:%02d", second % MINUTE)
        else -> if (isChinese) "00分00秒" else "00:00"
    }
}

/**
 * 毫秒时差显示
 * */
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

fun String.urlObtainName(): String =
    uri().lastPathSegment ?: substring(lastIndexOf('/') + 1)

fun String?.maxLengthMore(len: Int, more: String = "..."): String? =
    if (this.isNullOrEmpty() || this.length < len) {
        this
    } else {
        this.substring(0, len) + "..."
    }

@SuppressLint("SimpleDateFormat")
fun String.timestampToDate(dateFormat: String = "yyyy-MM-dd") =
    SimpleDateFormat(dateFormat).parse(this)

fun String.timestampToLong(dateFormat: String = "yyyy-MM-dd") =
    timestampToDate(this)?.time

object StringUtils {

    @JvmOverloads
    fun elapsedTime(now: Long = System.currentTimeMillis(), time: Long): String {
        val year = GregorianCalendar().get(Calendar.YEAR)
        val newYear = createGregorianCalendar(year).timeInMillis
        val msec = now - time
        val second = abs(msec) / 1000
        return when {
            //1分钟误差
            msec < -MINUTE -> "刚刚"
            second > MONTH ->
                if (time < newYear) {
                    //超过一年
                    time.dataFormat("yyyy-MM-dd")
                } else {
                    //超过一月
                    time.dataFormat("MM-dd")
                }
            second >= WEEK -> "${second / WEEK}周前"
            second >= DAY * 2 -> "${second / DAY}天前"
            second >= DAY -> "昨天"
            second >= HOUR -> "${second / HOUR}小时前"
            second >= MINUTE -> "${second / MINUTE}分钟前"
            second >= 3 -> "${second % MINUTE}秒前"
            second > 0 -> "刚刚"
            else -> time.dataFormat("yyyy-MM-dd")
        }
    }

    @JvmOverloads
    fun createGregorianCalendar(
        year: Int,
        month: Int = 0,
        date: Int = 1,
        hourOfDay: Int = 0,
        minute: Int = 0,
        second: Int = 0
    ): GregorianCalendar = GregorianCalendar().apply {
        set(year, month, date, hourOfDay, minute, second)
    }

    fun matcherColorSpan(
        spannable: SpannableStringBuilder,
        color: Int,
        vararg keywords: String
    ): SpannableStringBuilder {
        val keyword = arrayOfNulls<String>(keywords.size)
        //复制数组
        System.arraycopy(keywords, 0, keyword, 0, keywords.size)
        val text = spannable.toString()
        for (i in keyword.indices) {
            //处理通配符
            keyword[i] = keyword[i]!!.escapeSpecialWord()
            //忽略字母大小写
            val reg = "(?i)${keyword[i]}"
            val matcher = Pattern.compile(reg).matcher(text)
            while (matcher.find()) {
                val span = ForegroundColorSpan(color)
                spannable.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_MARK_MARK)
            }
        }
        return spannable
    }

    @JvmOverloads
    fun matcherDrawableSpan(
        prefix: String,
        text: String,
        start: Int = 0,
        end: Int = prefix.length,
        drawable: Drawable? = null,
        imageSpan: ImageSpan? = null
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(prefix + text)
        if (drawable == null && imageSpan == null) return spannable
        val image = drawable ?: imageSpan!!.drawable
        val span = imageSpan ?: ImageSpan(drawable!!)
        //设置图片矩形
        image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
        return spannable.apply {
            setSpan(span, start, end, ImageSpan.ALIGN_BASELINE)
        }
    }
}
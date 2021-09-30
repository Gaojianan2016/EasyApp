package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.abs

/**
 * 获取字符串
 * */
fun Context.string(
    @StringRes resId: Int,
    vararg formatArgs: Any
) = try {
    getString(resId, *formatArgs)
} catch (e: Exception) {
    e.printStackTrace()
    resId.toString()
}

/**
 * 获取字符串数组
 * */
fun Context.stringArray(@ArrayRes resId: Int): Array<String> =
    try {
        resources.getStringArray(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        arrayOf(resId.toString())
    }

/**
 * 转为MD5
 * */
fun String.toMd5() = encryptMD5ToString()

/**
 * 转义特殊词 e.g. [\ -> \\, $ -> \$, ....]
 * */
fun String.escapeSpecialWord(): String {
    if (isEmpty()) return this
    var result = this
    val specials = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
    for (special in specials) {
        if (result.contains(special)) {
            result = replace(special, "\\$special")
        }
    }
    return result
}

///////////////////////////
//// number string
///////////////////////////

private val SAFE_DECIMAL_FORMAT = object : ThreadLocal<DecimalFormat>() {
    override fun initialValue(): DecimalFormat {
        return NumberFormat.getInstance() as DecimalFormat
    }
}

private fun safeDecimalFormat() = SAFE_DECIMAL_FORMAT.get()

/**
 * 格式化小数点
 * */
fun Float.format(
    fractionDigits: Int = 2,
    prefix: String = "",
    suffix: String = "",
    minIntegerDigits: Int = 1,
    isGrouping: Boolean = false,
    isHalfUp: Boolean = false
) = toDouble().format(fractionDigits, prefix, suffix, minIntegerDigits, isGrouping, isHalfUp)

/**
 * 格式化小数点
 * */
fun Double.format(
    fractionDigits: Int = 2,
    prefix: String = "",
    suffix: String = "",
    minIntegerDigits: Int = 1,
    isGrouping: Boolean = false,
    isHalfUp: Boolean = false
): String {
    val df = safeDecimalFormat()?.apply {
        isGroupingUsed = isGrouping
        roundingMode = if (isHalfUp) RoundingMode.HALF_UP else RoundingMode.DOWN
        minimumIntegerDigits = minIntegerDigits
        minimumFractionDigits = fractionDigits
        maximumFractionDigits = fractionDigits
    }
    return prefix + (df?.format(this) ?: "") + suffix
}

///////////////////////////
//// time string
///////////////////////////

private val SAFE_DATE_FORMAT = object : ThreadLocal<MutableMap<String, SimpleDateFormat>>() {
    override fun initialValue(): MutableMap<String, SimpleDateFormat> {
        return mutableMapOf()
    }
}

@SuppressLint("SimpleDateFormat")
private fun safeDateFormat(pattern: String): SimpleDateFormat {
    val map = SAFE_DATE_FORMAT.get() ?: return SimpleDateFormat(pattern)
    var dateFormat: SimpleDateFormat? = map[pattern]
    if (dateFormat == null) {
        dateFormat = SimpleDateFormat(pattern)
        map[pattern] = dateFormat
    }
    return dateFormat
}

/**
 * 时间戳转日期格式
 * */
fun Long.toDateFormat(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    safeDateFormat(pattern).format(this)

/**
 * 时间戳转日期
 * */
fun Long.toDate() = Date(this)

/**
 * 日期格式转时间戳
 * */
fun String.toTimeMillis(pattern: String = "yyyy-MM-dd HH:mm:ss"): Long =
    toDate(pattern)?.time ?: -1L

/**
 * 日期格式转日期
 * */
fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? =
    try {
        safeDateFormat(pattern).parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

/**
 * 日期转日期格式
 * */
fun Date.toDateFormat(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    safeDateFormat(pattern).format(this)

/**
 * 时间差
 * @param timeUnit [MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]
 * */
fun Long.timeMillisDifference(time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Long {
    val result = abs(this - time)
    return when (timeUnit) {
        TimeUnit.DAYS -> result / UnitObj.TIME_DAY / 1000
        TimeUnit.HOURS -> result / UnitObj.TIME_HOUR / 1000
        TimeUnit.MINUTES -> result / UnitObj.TIME_MINUTE / 1000
        TimeUnit.SECONDS -> result / 1000
        else -> result
    }
}

/**
 * 是否包含中文字符串
 * */
fun String.containsChinese(): Boolean {
    if (isEmpty()) return false
    for (c in this) {
        if (c.isChinese()) return true
    }
    return false
}

/**
 * 是否全是中文字符串
 * */
fun String.isChinese(): Boolean {
    if (isEmpty()) return false
    for (c in this) {
        if (!c.isChinese()) return false
    }
    return true
}

/**
 * 是否是中文字符
 * */
fun Char.isChinese(): Boolean {
    val ub = Character.UnicodeBlock.of(this)
    return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
}

/**
 * 是否包含emoji字符
 * */
fun String.containsEmoji(): Boolean {
    if (isEmpty()) return false
    for (c in this) {
        if (c.isEmoji()) return true
    }
    return false
}

/**
 * 是否是emoji字符
 * */
fun Char.isEmoji() = !(toInt() == 0x0 || toInt() == 0x9 ||
        toInt() == 0xA || toInt() == 0xD || toInt() in 0x20..0xD7FF
        || toInt() in 0xE000..0xFFFD)

/**
 * 字节转gb mb kb字符串 0.5Tb 1.20Gb 60.00Mb 798.35Kb 666b
 * */
fun Long.byteToStr(): String = when {
    this >= UnitObj.SIZE_TB -> (this / UnitObj.SIZE_TB.toDouble()).format(suffix = "Tb")
    this >= UnitObj.SIZE_GB -> (this / UnitObj.SIZE_GB.toDouble()).format(suffix = "Gb")
    this >= UnitObj.SIZE_MB -> (this / UnitObj.SIZE_MB.toDouble()).format(suffix = "Mb")
    this >= UnitObj.SIZE_KB -> (this / UnitObj.SIZE_KB.toDouble()).format(suffix = "Kb")
    else -> (toDouble()).format(0, suffix = "b")
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
fun Int.toSecondFormat(isChinese: Boolean = false): String =
    (this * 1000).toLong().toSecondFormat(isChinese)

/**
 * 毫秒转时间格式 01:50:00 or 16:18 中文版 01时50分00秒 or 16分18秒
 * */
fun Long.toSecondFormat(isChinese: Boolean = false): String {
    val second = this / 1000
    return when {
        second > UnitObj.TIME_HOUR -> if (isChinese) String.format(
            "%02d时%02d分%02d秒",
            second / UnitObj.TIME_HOUR,
            second / UnitObj.TIME_MINUTE % UnitObj.TIME_MINUTE,
            second % UnitObj.TIME_MINUTE
        ) else String.format(
            "%02d:%02d:%02d",
            second / UnitObj.TIME_HOUR,
            second / UnitObj.TIME_MINUTE % UnitObj.TIME_MINUTE,
            second % UnitObj.TIME_MINUTE
        )
        second > UnitObj.TIME_MINUTE -> if (isChinese) String.format(
            "%02d分%02d秒", second / UnitObj.TIME_MINUTE, second % UnitObj.TIME_MINUTE
        )
        else String.format("%02d:%02d", second / UnitObj.TIME_MINUTE, second % UnitObj.TIME_MINUTE)
        second > 0 -> if (isChinese) String.format("00分%02d秒", second % UnitObj.TIME_MINUTE)
        else String.format("00:%02d", second % UnitObj.TIME_MINUTE)
        else -> if (isChinese) "00分00秒" else "00:00"
    }
}

/**
 * 毫秒时差显示
 * */
fun Long.elapsedTime(): String {
    val second = abs(this) / 1000
    return when {
        second >= UnitObj.TIME_YEAR -> "${second / UnitObj.TIME_YEAR}年前"
        second >= UnitObj.TIME_MONTH -> "${second / UnitObj.TIME_MONTH}个月前"
        second >= UnitObj.TIME_WEEK -> "${second / UnitObj.TIME_WEEK}周前"
        second >= UnitObj.TIME_DAY * 2 -> "两天前"
        second >= UnitObj.TIME_DAY -> "一天前"
        second >= UnitObj.TIME_HOUR -> "${second / UnitObj.TIME_HOUR}小时前"
        second >= UnitObj.TIME_MINUTE -> "${second / UnitObj.TIME_MINUTE}分钟前"
        second > 5 -> "${second % 60}秒前"
        else -> "刚刚"
    }
}

fun String.urlObtainName(): String =
    uri().lastPathSegment ?: substring(lastIndexOf('/') + 1)

fun String?.maxLengthMore(len: Int, more: String = "..."): String? =
    if (isNullOrEmpty() || length < len) {
        this
    } else {
        substring(0, len) + "..."
    }

object StringUtils {

    fun elapsedTime(now: Long = System.currentTimeMillis(), time: Long): String {
        val year = GregorianCalendar().get(Calendar.YEAR)
        val newYear = createGregorianCalendar(year).timeInMillis
        val msec = now - time
        val second = abs(msec) / 1000
        return when {
            //1分钟误差
            msec < -UnitObj.TIME_MINUTE -> "刚刚"
            second > UnitObj.TIME_MONTH ->
                if (time < newYear) {
                    //超过一年
                    time.toDateFormat("yyyy-MM-dd")
                } else {
                    //超过一月
                    time.toDateFormat("MM-dd")
                }
            second >= UnitObj.TIME_WEEK -> "${second / UnitObj.TIME_WEEK}周前"
            second >= UnitObj.TIME_DAY * 2 -> "${second / UnitObj.TIME_DAY}天前"
            second >= UnitObj.TIME_DAY -> "昨天"
            second >= UnitObj.TIME_HOUR -> "${second / UnitObj.TIME_HOUR}小时前"
            second >= UnitObj.TIME_MINUTE -> "${second / UnitObj.TIME_MINUTE}分钟前"
            second >= 3 -> "${second % UnitObj.TIME_MINUTE}秒前"
            second > 0 -> "刚刚"
            else -> time.toDateFormat("yyyy-MM-dd")
        }
    }

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
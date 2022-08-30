package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import androidx.annotation.IntRange
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

private val SAFE_DATE_FORMAT = object : ThreadLocal<MutableMap<String, SimpleDateFormat>>() {
    override fun initialValue(): MutableMap<String, SimpleDateFormat> {
        return mutableMapOf()
    }
}

@SuppressLint("SimpleDateFormat")
private fun safeDateFormat(pattern: String, locale: Locale): SimpleDateFormat {
    val map = SAFE_DATE_FORMAT.get() ?: return SimpleDateFormat(pattern)
    val key = pattern + locale.country
    var dateFormat: SimpleDateFormat? = map[key]
    if (dateFormat == null) {
        dateFormat = SimpleDateFormat(pattern, locale)
        map[key] = dateFormat
    }
    return dateFormat
}

/**
 * 时间戳转日期格式
 * 常用如下
 * [yyyy 年, MM 月, dd 日, HH 24小时, hh 12小时, mm 分钟, ss 秒, SSS 毫秒]
 * [EEEE 星期几, a 上午/下午(和hh配合)]
 * */
fun Long.toDateFormat(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): String =
    safeDateFormat(pattern, locale).format(this)

/**
 * 时间戳转日期
 * */
fun Long.toDate() = Date(this)

/**
 * 时间戳转周
 * */
fun Long.toWeek(pattern: String = "EEEE", locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat(pattern, locale).format(this)

/**
 * 日期格式转时间戳
 * */
fun String.toTimeMillis(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): Long =
    toDate(pattern, locale)?.time ?: -1L

/**
 * 日期格式转日期
 * */
fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): Date? =
    try {
        safeDateFormat(pattern, locale).parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

/**
 * 日期转日期格式
 * */
fun Date.toDateFormat(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): String =
    safeDateFormat(pattern, locale).format(this)

/**
 * 时间差
 * @param timeUnit [MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]
 * */
fun timeDifferenceMillis(
    time1: Long,
    time2: Long,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Long {
    val result = abs(time1 - time2)
    return when (timeUnit) {
        TimeUnit.DAYS -> result / UnitObj.TIME_DAY_MILLIS
        TimeUnit.HOURS -> result / UnitObj.TIME_HOUR_MILLIS
        TimeUnit.MINUTES -> result / UnitObj.TIME_MINUTE_MILLIS
        TimeUnit.SECONDS -> result / UnitObj.TIME_SECONDS_MILLIS
        else -> result
    }
}

/**
 * 时间差
 * @param timeUnit [MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]
 * */
fun timeDifferenceMillis(
    time1: String,
    time2: String,
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    locale: Locale = Locale.getDefault()
) = timeDifferenceMillis(time1.toTimeMillis(pattern, locale), time2.toTimeMillis(pattern, locale), timeUnit)

/**
 * 时间差
 * @param timeUnit [MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]
 * */
fun timeDifferenceMillis(
    time1: Date,
    time2: Date,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
) = timeDifferenceMillis(time1.time, time2.time, timeUnit)

/**
 * 时间差字符串 xx天xx时xx分xx秒
 * */
fun timeDifferenceString(
    time1: Date,
    time2: Date
) = timeDifferenceMillis(time1, time2).toTimeString(true)

/**
 * 时间差字符串 xx天xx时xx分xx秒
 * */
fun timeDifferenceString(
    time1: String,
    time2: String,
    pattern: String = "yyyy-MM-dd HH:mm:ss"
) = timeDifferenceMillis(time1, time2, pattern).toTimeString(true)

/**
 * 时间差字符串 xx天xx时xx分xx秒
 * */
fun timeDifferenceString(
    time1: Long,
    time2: Long
) = timeDifferenceMillis(time1, time2).toTimeString(true)

/**
 * 时间戳转时间字符串 xx天xx时xx分xx秒
 * */
fun Long.toTimeString(
    isChinese: Boolean = false
): String {
    val unitStr = if (isChinese) arrayOf("天", "时", "分", "秒") else arrayOf(":", ":", ":", ":")
    val unitTime = arrayOf(
        UnitObj.TIME_DAY_MILLIS, UnitObj.TIME_HOUR_MILLIS,
        UnitObj.TIME_MINUTE_MILLIS, UnitObj.TIME_SECONDS_MILLIS
    )
    var millis = this
    val sb = StringBuilder()
    for (i in 0..3) {
        if (millis >= unitTime[i]) {
            val temp = millis / unitTime[i]
            millis -= temp * unitTime[i]
            sb.append(temp).append(unitStr[i])
        }
    }
    return sb.toString()
}

/**
 * 时间戳跟当前时差
 * */
fun Long.nowTimeDifference(): String {
    val now = System.currentTimeMillis()
    val span = now - this
    val todayMillis = getTodayMillis()
    return when {
        span < 0 -> String.format("%tF", this)
        span <= UnitObj.TIME_SECONDS_MILLIS -> "刚刚"
        span < UnitObj.TIME_MINUTE_MILLIS -> String.format(
            Locale.getDefault(),
            "%d秒前",
            span / UnitObj.TIME_SECONDS_MILLIS
        )
        span < UnitObj.TIME_HOUR_MILLIS -> String.format(
            Locale.getDefault(),
            "%d分钟前",
            span / UnitObj.TIME_MINUTE_MILLIS
        )
        this >= todayMillis -> String.format("今天%tR", this)
        this >= todayMillis - UnitObj.TIME_DAY_MILLIS -> String.format("昨天%tR", this)
        else -> String.format("%tF", this)
    }
}

/**
 * 是否是当天
 * */
fun Long.isToday(): Boolean {
    val todayMillis = getTodayMillis()
    return this > todayMillis || this - todayMillis < UnitObj.TIME_DAY_MILLIS
}

/**
 * 获取当前时间字符串
 * */
fun getNowTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()) =
    System.currentTimeMillis().toDateFormat(pattern, locale)

/**
 * 获取当天某个时间时间戳
 * */
fun getTodayMillis(
    @IntRange(from = 0, to = 23) hourOfDay: Int = 0,
    @IntRange(from = 0, to = 60) minute: Int = 0,
    @IntRange(from = 0, to = 60) second: Int = 0,
    @IntRange(from = 0) millisecond: Int = 0
) = Calendar.getInstance().also {
    it.set(Calendar.HOUR_OF_DAY, hourOfDay)
    it.set(Calendar.MINUTE, minute)
    it.set(Calendar.SECOND, second)
    it.set(Calendar.MILLISECOND, millisecond)
}.timeInMillis

/**
 * 创建阳历
 * */
fun createGregorianCalendar(
    @IntRange(from = 1970) year: Int,
    @IntRange(from = 1, to = 12) month: Int = 1,
    @IntRange(from = 1, to = 31) date: Int = 1,
    @IntRange(from = 0, to = 23) hourOfDay: Int = 0,
    @IntRange(from = 0, to = 60) minute: Int = 0,
    @IntRange(from = 0, to = 60) second: Int = 0
): GregorianCalendar = GregorianCalendar().apply {
    set(year, month - 1, date, hourOfDay, minute, second)
}
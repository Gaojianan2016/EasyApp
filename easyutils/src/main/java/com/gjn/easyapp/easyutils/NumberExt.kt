package com.gjn.easyapp.easyutils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat

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
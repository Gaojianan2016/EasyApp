package com.gjn.easyapp.easyutils

import android.content.Context
import android.util.TypedValue

fun Float.px2Dimension(context: Context, unit: Int): Float{
    if (this == 0f) return 0f
    return TypedValue.applyDimension(unit, this, context.resources.displayMetrics)
}

fun Float.dp2px(context: Context): Float {
    if (this == 0f) return 0f
    val scale = context.resources.displayMetrics.density
    return this * scale + 0.5f
}

fun Float.sp2px(context: Context): Float {
    if (this == 0f) return 0f
    val fontScale = context.resources.displayMetrics.scaledDensity
    return this * fontScale + 0.5f
}

fun Float.pt2px(context: Context): Int {
    if (this == 0f) return 0
    val xdpi = context.resources.displayMetrics.xdpi
    return (this * xdpi / 72f + 0.5).toInt()
}

fun Float.px2dp(context: Context): Float {
    if (this == 0f) return 0f
    val scale = context.resources.displayMetrics.density
    return this / scale + 0.5f
}

fun Float.px2sp(context: Context): Float {
    if (this == 0f) return 0f
    val fontScale = context.resources.displayMetrics.scaledDensity
    return this / fontScale + 0.5f
}

fun Float.px2pt(context: Context): Int {
    if (this == 0f) return 0
    val xdpi = context.resources.displayMetrics.xdpi
    return (this * 72 / xdpi + 0.5).toInt()
}

fun Int.dp2px(context: Context) = this.toFloat().dp2px(context)

fun Int.sp2px(context: Context) = this.toFloat().sp2px(context)

fun Int.pt2px(context: Context) = this.toFloat().pt2px(context)

fun Int.px2dp(context: Context) = this.toFloat().px2dp(context)

fun Int.px2sp(context: Context) = this.toFloat().px2sp(context)

fun Int.px2pt(context: Context) = this.toFloat().px2pt(context)

fun Float.dp(context: Context) = this.px2dp(context)

fun Int.dp(context: Context) = this.px2dp(context)

fun Float.sp(context: Context) = this.px2sp(context)

fun Int.sp(context: Context) = this.px2sp(context)

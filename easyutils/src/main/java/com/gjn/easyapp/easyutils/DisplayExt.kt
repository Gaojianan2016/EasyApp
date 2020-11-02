package com.gjn.easyapp.easyutils

import android.content.Context

fun Float.px2dp(context: Context): Float {
    if (this == 0f) return 0f
    val scale = context.resources.displayMetrics.density
    return this / scale + 0.5f
}

fun Int.px2dp(context: Context): Float {
    if (this == 0) return 0f
    val scale = context.resources.displayMetrics.density
    return this / scale + 0.5f
}

fun Float.px2sp(context: Context): Float {
    if (this == 0f) return 0f
    val fontScale = context.resources.displayMetrics.scaledDensity
    return this / fontScale + 0.5f
}

fun Int.px2sp(context: Context): Float {
    if (this == 0) return 0f
    val fontScale = context.resources.displayMetrics.scaledDensity
    return this / fontScale + 0.5f
}

fun Float.dp(context: Context): Float {
    if (this == 0f) return 0f
    val scale = context.resources.displayMetrics.density
    return this * scale + 0.5f
}

fun Int.dp(context: Context): Float {
    if (this == 0) return 0f
    val scale = context.resources.displayMetrics.density
    return this * scale + 0.5f
}

fun Float.sp(context: Context): Float {
    if (this == 0f) return 0f
    val fontScale = context.resources.displayMetrics.scaledDensity
    return this * fontScale + 0.5f
}

fun Int.sp(context: Context): Float {
    if (this == 0) return 0f
    val fontScale = context.resources.displayMetrics.scaledDensity
    return this * fontScale + 0.5f
}

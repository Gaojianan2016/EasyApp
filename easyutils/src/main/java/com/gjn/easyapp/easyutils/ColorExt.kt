package com.gjn.easyapp.easyutils

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat


/**
 * 获取color resource
 * */
fun Context.getColorRes(@ColorRes id: Int) = ActivityCompat.getColor(this, id)

/**
 * 改变颜色组件透明度 0x0 ~ 0xFF
 * 公式 (color & component) | (alpha << offset)
 * */
fun Int.changeColorAlpha(
    component: Int,
    @IntRange(from = 0x0, to = 0xFF) alpha: Int,
    offset: Int
) =
    (this and component) or (alpha shl offset)

/**
 * 改变颜色组件透明度 0.0 ~ 1.0
 * 公式 (color & component) | ((int) (alpha * 255f + 0.5f) << offset)
 * */
fun Int.changeColorAlpha(
    component: Int,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float,
    offset: Int
) =
    (this and component) or ((alpha * 255f + 0.5f).toInt() shl offset)

/**
 * 改变原色透明度 0x0 ~ 0xFF
 * */
fun Int.changeColorAlpha(@IntRange(from = 0x0, to = 0xFF) alpha: Int) =
    changeColorAlpha(0x00FFFFFF, alpha, 24)

/**
 * 改变原色透明度 0.0 ~ 1.0
 * */
fun Int.changeColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) =
    changeColorAlpha(0x00FFFFFF, alpha, 24)

/**
 * 改变红色透明度 0x0 ~ 0xFF
 * */
fun Int.changeRedColorAlpha(@IntRange(from = 0x0, to = 0xFF) alpha: Int) =
    changeColorAlpha(0xFF00FFFF.toInt(), alpha, 16)

/**
 * 改变红色透明度 0.0 ~ 1.0
 * */
fun Int.changeRedColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) =
    changeColorAlpha(0xFF00FFFF.toInt(), alpha, 16)

/**
 * 改变绿色透明度 0x0 ~ 0xFF
 * */
fun Int.changeGreenColorAlpha(@IntRange(from = 0x0, to = 0xFF) alpha: Int) =
    changeColorAlpha(0xFFFF00FF.toInt(), alpha, 8)

/**
 * 改变绿色透明度 0.0 ~ 1.0
 * */
fun Int.changeGreenColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) =
    changeColorAlpha(0xFFFF00FF.toInt(), alpha, 8)

/**
 * 改变蓝色透明度 0x0 ~ 0xFF
 * */
fun Int.changeBlueColorAlpha(@IntRange(from = 0x0, to = 0xFF) alpha: Int) =
    changeColorAlpha(0xFFFFFF00.toInt(), alpha, 0)

/**
 * 改变蓝色透明度 0.0 ~ 1.0
 * */
fun Int.changeBlueColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) =
    changeColorAlpha(0xFFFFFF00.toInt(), alpha, 0)

/**
 * 解析颜色 Int to String 无透明度
 * */
fun Int.parseRgbColor(): String {
    val colorInt = this and 0x00FFFFFF
    var color = colorInt.toHexString()
    while (color.length < 6) {
        color = "0$color"
    }
    return "#$color"
}

/**
 * 解析颜色 Int to String 有透明度
 * */
fun Int.parseArgbColor(): String {
    var color = toHexString()
    while (color.length < 6) {
        color = "0$color"
    }
    while (color.length < 8) {
        color = "f$color"
    }
    return "#$color"
}

/**
 * 随即颜色
 * supportAlpha 是否支持透明度
 * */
fun randomColor(supportAlpha: Boolean = true): Int {
    val high = if (supportAlpha) (Math.random() * 0x100).toInt() shl 24 else 0xFF000000.toInt()
    return high or (Math.random() * 0x1000000).toInt()
}

/**
 * 是否是浅色
 * red * 0.299 + green * 0.587 + blue * 0.114 >= 127.5 浅色公式
 * */
fun Int.isLightColor() =
    0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this) >= 127.5
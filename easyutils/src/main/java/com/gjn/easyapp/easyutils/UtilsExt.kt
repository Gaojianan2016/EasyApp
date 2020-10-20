package com.gjn.easyapp.easyutils

import android.net.Uri
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun String.uri(): Uri = Uri.parse(this)

fun Int.intervalOpen(min: Int, max: Int): Int = min.coerceAtLeast(this.coerceAtMost(max))

fun Float.intervalOpen(min: Float, max: Float): Float = min.coerceAtLeast(this.coerceAtMost(max))

fun Double.intervalOpen(min: Double, max: Double): Double =
    min.coerceAtLeast(this.coerceAtMost(max))

fun Long.intervalOpen(min: Long, max: Long): Long = min.coerceAtLeast(this.coerceAtMost(max))

/**
 * 计算地球坐标两点距离
 * return 结果单位M
 * */
fun coordinateDistance(
    longitude1: Double,
    latitude1: Double,
    longitude2: Double,
    latitude2: Double
): Double {
    val r = 6378137 //地球半径
    val lat1 = Math.PI / 180 * latitude1
    val lat2 = Math.PI / 180 * latitude2

    val a = lat1 - lat2
    val b = Math.PI / 180 * (longitude1 - longitude2)

    val sa = sin(a / 2)
    val sb = sin(b / 2)

    return asin(sqrt(sa * sa + cos(lat1) * cos(lat2) * sb * sb)) * r * 2
}
package com.gjn.easyapp.easyutils

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

//区间相关
fun Int.intervalOpen(min: Int, max: Int) = min.coerceAtLeast(this.coerceAtMost(max))

fun Float.intervalOpen(min: Float, max: Float) = min.coerceAtLeast(this.coerceAtMost(max))

fun Double.intervalOpen(min: Double, max: Double) = min.coerceAtLeast(this.coerceAtMost(max))

fun Long.intervalOpen(min: Long, max: Long) = min.coerceAtLeast(this.coerceAtMost(max))

//非零处理
fun Int?.orZero(): Int = this ?: 0

fun Float?.orZero(): Float = this ?: 0f

fun Double?.orZero(): Double = this ?: 0.0

fun Long?.orZero(): Long = this ?: 0L

fun Boolean?.isTrue() = this == true

//生成 随机数
fun randomNumber(min: Int, max: Int): Int = (Math.random() * max + min).toInt()

fun randomNumber(min: Float, max: Float): Float = (Math.random() * max + min).toFloat()

fun randomNumber(min: Double, max: Double): Double = Math.random() * max + min

fun randomNumber(min: Long, max: Long): Long = (Math.random() * max + min).toLong()

fun randomNumber(max: Int) = randomNumber(0, max)

fun randomNumber(max: Float) = randomNumber(0f, max)

fun randomNumber(max: Double) = randomNumber(0.0, max)

fun randomNumber(max: Long) = randomNumber(0L, max)

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

/**
 * 将map中数据为空的字段剔除掉
 * */
fun <K, V> Map<K, V>?.toHashMapOrEmptyMap(): HashMap<K, V> {
    if (this == null) return HashMap()
    val hashMap = HashMap<K, V>()
    forEach { (k, v) ->
        if (v != null) hashMap[k] = v
    }
    return hashMap
}

/**
 * 将list中数据为空的字段剔除掉
 * */
fun <T> List<T?>?.toMutableListOrEmptyList(): MutableList<T> {
    if (this == null) return mutableListOf()
    val list = mutableListOf<T>()
    this.forEach {
        if (it != null) list.add(it)
    }
    return list
}

/**
 * 单选position
 * */
fun singleChoicePosition(cur: Int, change: Int, default: Int = -1) =
    if (cur == change) default else change

/**
 * 单选字符串
 * */
fun singleChoiceString(cur: String, change: String, default: String = "") =
    if (cur == change) default else change
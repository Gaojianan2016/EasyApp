package com.gjn.easyapp.easyutils

import android.app.Application
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.io.File
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun String.uri(): Uri = Uri.parse(this)

fun File.uri(): Uri = Uri.fromFile(this)

fun Int.intervalOpen(min: Int, max: Int): Int = min.coerceAtLeast(this.coerceAtMost(max))

fun Float.intervalOpen(min: Float, max: Float): Float = min.coerceAtLeast(this.coerceAtMost(max))

fun Double.intervalOpen(min: Double, max: Double): Double =
    min.coerceAtLeast(this.coerceAtMost(max))

fun Long.intervalOpen(min: Long, max: Long): Long = min.coerceAtLeast(this.coerceAtMost(max))

fun String.parseColor(): Int = Color.parseColor(this)

fun Int?.isNotNullOrZero(): Boolean = !isNullOrZero()

fun Float?.isNotNullOrZero(): Boolean = !isNullOrZero()

fun Double?.isNotNullOrZero(): Boolean = !isNullOrZero()

fun Long?.isNotNullOrZero(): Boolean = !isNullOrZero()

fun Int?.isNullOrZero(): Boolean = this == null || this == 0

fun Float?.isNullOrZero(): Boolean = this == null || this == 0f

fun Double?.isNullOrZero(): Boolean = this == null || this == 0.0

fun Long?.isNullOrZero(): Boolean = this == null || this == 0L

/**
 * 创建ViewModel对象
 * */
fun <T : ViewModel> Class<T>.createViewModel(
    owner: ViewModelStoreOwner
): T = ViewModelProvider(
    owner, ViewModelProvider.NewInstanceFactory()
).get(this)

/**
 * 创建AndroidViewModel对象
 * */
fun <T : ViewModel> Class<T>.createAndroidViewModel(
    owner: ViewModelStoreOwner,
    application: Application
): T = ViewModelProvider(
    owner, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
).get(this)


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
fun <K, V> Map<K, V>?.toHashMap(): HashMap<K, V> {
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
fun singleChoicePosition(cur: Int, change: Int, default: Int = -1): Int =
    if (cur == change) default else change
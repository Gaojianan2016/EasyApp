package com.gjn.easyapp.easyutils


/**
 * 是否是非空集合
 * */
inline fun <T> Collection<T>?.isNotEmptyCollection() = orEmpty().isNotEmpty()

/**
 * 列表需要大于1
 * */
inline fun <T> Collection<T>.isList(): Boolean = isNotEmpty() && size > 1

/**
 * 最多不超过设置数量
 * */
inline fun <T> Collection<T>.isLimitSize(size: Int = 10): Boolean = isNotEmpty() && this.size <= size

/**
 * 分割字符串成列表
 * */
fun String.split2List(split: String): List<String> {
    val list = mutableListOf<String>()
    split(split).forEach { if (it.isNotEmpty()) list.add(it) }
    return list
}
package com.gjn.easyapp.easyutils


/**
 * 是否是非空集合
 * */
fun <E> Collection<E>?.isNotEmptyCollection() = orEmpty().isNotEmpty()

/**
 * 列表需要大于1
 * */
fun <E> Collection<E>.isList(): Boolean = isNotEmpty() && size > 1

/**
 * 最多不超过设置数量
 * */
fun <E> Collection<E>.isLimitSize(size: Int = 10): Boolean = isNotEmpty() && this.size <= size

/**
 * 重设list数组
 * */
fun <E> MutableList<E>.setAll(list: List<E>): Boolean {
    clear()
    return addAll(list)
}

/**
 * 分割字符串成列表
 * */
fun String.split2List(split: String): List<String> {
    val list = mutableListOf<String>()
    split(split).forEach { if (it.isNotEmpty()) list.add(it) }
    return list
}
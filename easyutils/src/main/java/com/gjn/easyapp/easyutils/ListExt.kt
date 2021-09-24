package com.gjn.easyapp.easyutils


/**
 * 列表需要大于1
 * */
fun Collection<*>.isList() = isNotEmpty() && size > 1

/**
 * 是否小于规定大小
 * */
fun Collection<*>.insufficientSize(size: Int = 10) = isNotEmpty() && this.size < size

/**
 * 分割字符串成列表
 * */
fun String.split2List(split: String): List<String> {
    val list = mutableListOf<String>()
    split(split).forEach { if (it.isNotEmpty()) list.add(it) }
    return list
}
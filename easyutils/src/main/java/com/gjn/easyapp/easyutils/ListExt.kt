package com.gjn.easyapp.easyutils


//集合大小是超过1的list
fun Collection<*>?.isList(): Boolean = !(this.isNullOrEmpty() || this.size <= 1)

//集合大小不足size的list
fun Collection<*>?.insufficientSize(size: Int = 10): Boolean =
    this.isNullOrEmpty() || this.size < size

//字符串split数组并且去除空字段
fun String.splitToList(split: String): List<String> {
    val list = mutableListOf<String>()
    this.split(split).forEach {
        if (it.isNotEmpty()) {
            list.add(it)
        }
    }
    return list
}
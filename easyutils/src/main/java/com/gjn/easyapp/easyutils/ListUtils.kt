package com.gjn.easyapp.easyutils

object ListUtils {

    fun isEmpty(list: Collection<*>?): Boolean = list.isNullOrEmpty()

    fun isNotEmpty(list: Collection<*>?): Boolean = !isEmpty(list)

    fun isList(list: Collection<*>?): Boolean = list != null && list.size > 1

    fun toList(text: String, split: String): List<String> = text.split(split)
}
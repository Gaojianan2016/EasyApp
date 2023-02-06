package com.gjn.easyapp.easyutils

/**
 * 使用gson复制对象
 * */
@Suppress("UNCHECKED_CAST")
fun <T> Any.cloneAny(clazz: Class<*>): T {
    val gson = getGson()
    val json = gson.toJson(this)
    return gson.fromJson(json, clazz) as T
}
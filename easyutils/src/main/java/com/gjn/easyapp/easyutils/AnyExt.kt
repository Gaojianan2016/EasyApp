package com.gjn.easyapp.easyutils

import com.google.gson.Gson

/**
 * 使用gson复制对象
 * */
fun <T> Any.cloneAny(clazz: Class<*>): T {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, clazz) as T
}
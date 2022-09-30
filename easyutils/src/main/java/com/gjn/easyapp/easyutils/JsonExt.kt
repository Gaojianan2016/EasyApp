package com.gjn.easyapp.easyutils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

/**
 * json 格式化
 * */
fun String?.formatJson(
    tab: String = "",
    cleanRegex: Regex = "[\t\r\n]".toRegex()
): String {
    if (isNullOrEmpty()) return ""
    val result = StringBuilder()
    val rawJson = replace(cleanRegex, "")
    val ln = '\n'
    var line = 0
    for (element in rawJson) {
        when (element) {
            //换行 并且加一级tab
            '{', '[' -> {
                result.append(element).append(ln).append(tab)
                line++
                result.addTab(line)
            }
            //换行 并且减一级tab
            '}', ']' -> {
                result.append(ln).append(tab)
                line--
                result.addTab(line)
                result.append(element)
            }
            //换行 并且同级tab
            ',' -> {
                result.append(element).append(ln).append(tab)
                result.addTab(line)
            }
            else -> result.append(element)
        }
    }
    return result.toString()
}

private fun StringBuilder.addTab(line: Int) {
    for (i in 0 until line) {
        append("  ")
    }
}

fun String?.isJsonStr(): Boolean {
    if (isNullOrEmpty()) return false
    return try {
        if (trim().startsWith("[") && trim().endsWith("]")) {
            JSONArray(this)
            true
        } else {
            JSONObject(this)
            true
        }
    } catch (e: Exception) {
        false
    }
}

/**
 * Any转json
 * */
fun Any?.toGsonJson(): String = Gson().toJson(this)

/**
 * Gson 解析字符串 返回对象
 * */
fun <T> String.fromGsonJson(clazz: Class<T>): T = Gson().fromJson(this, clazz)

/**
 * Gson 解析字符串 返回数组
 * */
fun <T> String.fromGsonJsonList(clazz: Class<T>): List<T> {
    val gson = Gson()
    val list = mutableListOf<T>()
    val jsonList = gson.fromJson<List<JsonObject>>(this, object : TypeToken<List<JsonObject>>() {}.type)
    jsonList.forEach {
        list.add(gson.fromJson(it, clazz))
    }
    return list
}
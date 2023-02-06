package com.gjn.easyapp.easyutils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.io.Reader
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

private const val KEY_DEFAULT_GSON = "DEFAULT_GSON"
private const val KEY_DELEGATE_GSON = "DELEGATE_GSON"

private val GSON_MAP = ConcurrentHashMap<String, Gson>()

/**
 * 设置代理gson
 * */
fun setDelegateGson(gson: Gson?) {
    if (gson == null) return
    putGsonMap(KEY_DELEGATE_GSON, gson)
}

/**
 * 设置gsonMap数据
 * */
fun putGsonMap(key: String, gson: Gson?) {
    if (key.isEmpty() || gson == null) return
    GSON_MAP[key] = gson
}

/**
 * 获取设置过的gson
 * */
fun getGson(key: String = ""): Gson {
    val gson = when {
        GSON_MAP[key] != null -> GSON_MAP[key]
        GSON_MAP[KEY_DELEGATE_GSON] != null -> GSON_MAP[KEY_DELEGATE_GSON]
        GSON_MAP[KEY_DEFAULT_GSON] != null -> GSON_MAP[KEY_DEFAULT_GSON]
        else -> null
    }
    return if (gson == null) {
        val defaultGson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()
        GSON_MAP[KEY_DEFAULT_GSON] = defaultGson
        defaultGson
    } else {
        gson
    }
}

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
fun Any?.toGsonJson(gson: Gson = getGson()): String = gson.toJson(this)

/**
 * Any转json
 * */
fun Any?.toGsonJson(typeOfSrc: Type, gson: Gson = getGson()): String = gson.toJson(this, typeOfSrc)

/**
 * Gson 解析字符串 返回对象
 * */
fun <T> String.fromGsonJson(classOfT: Class<T>, gson: Gson = getGson()): T = gson.fromJson(this, classOfT)

fun <T> String.fromGsonJson(typeOfT: Type, gson: Gson = getGson()): T = gson.fromJson(this, typeOfT)

fun <T> Reader.fromGsonJson(classOfT: Class<T>, gson: Gson = getGson()): T = gson.fromJson(this, classOfT)

fun <T> Reader.fromGsonJson(typeOfT: Type, gson: Gson = getGson()): T = gson.fromJson(this, typeOfT)

fun <T> String.fromGsonJson(baseClass: Class<*>, tClass: Class<*>): T = fromGsonJson(convertType(baseClass, tClass))

/**
 * 转换参数化Type
 * */
fun convertType(rawType: Type, vararg typeArguments: Type) = TypeToken.getParameterized(rawType, *typeArguments).type

fun Type.convertListType() = convertType(List::class.java, this)

fun Type.convertSetType() = convertType(Set::class.java, this)

fun Type.convertMapType(valueType: Type) = convertType(Map::class.java, this, valueType)

fun Type.convertArrayType() = TypeToken.getArray(this).type

/**
 * Gson 解析字符串 返回List
 * */
inline fun <reified T> String.fromGsonJsonList(gson: Gson = getGson()): List<T> = fromGsonJson(T::class.java.convertListType(), gson)

/**
 * Gson 解析字符串 返回Set
 * */
inline fun <reified T> String.fromGsonJsonSet(gson: Gson = getGson()): Set<T> = fromGsonJson(T::class.java.convertSetType(), gson)

/**
 * Gson 解析字符串 返回Map
 * */
inline fun <reified K, reified V> String.fromGsonJsonMap(gson: Gson = getGson()): Map<K, V> =
    fromGsonJson(K::class.java.convertMapType(V::class.java), gson)

/**
 * Gson 解析字符串 返回Array
 * */
inline fun <reified T> String.fromGsonJsonArray(gson: Gson = getGson()): Array<T> = fromGsonJson(T::class.java.convertArrayType(), gson)

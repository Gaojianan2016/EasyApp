package com.gjn.easyapp.easylogger

import com.gjn.easyapp.easyutils.*
import com.google.gson.Gson
import org.json.JSONObject

object LogUtil {

    private const val LOG_TAG = "easyLog"

    private const val LEVEL_V = 2
    private const val LEVEL_D = 3
    private const val LEVEL_I = 4
    private const val LEVEL_W = 5
    private const val LEVEL_E = 6
    private const val LEVEL_WTF = 7

    var isShowLogLevel = 1

    private fun println(level: Int, tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        if (isShowLogLevel > level) return
        when (level) {
            LEVEL_D -> logD(msg, tag, tr)
            LEVEL_I -> logI(msg, tag, tr)
            LEVEL_W -> logW(msg, tag, tr)
            LEVEL_E -> logE(msg, tag, tr)
            LEVEL_WTF -> logWTF(msg, tag, tr)
            else -> logV(msg, tag, tr)
        }
    }

    fun d(tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        println(LEVEL_D, tag, msg, tr)
    }

    fun v(tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        println(LEVEL_V, tag, msg, tr)
    }

    fun i(tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        println(LEVEL_I, tag, msg, tr)
    }

    fun w(tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        println(LEVEL_W, tag, msg, tr)
    }

    fun e(tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        println(LEVEL_E, tag, msg, tr)
    }

    fun wtf(tag: String = LOG_TAG, msg: String?, tr: Throwable? = null) {
        println(LEVEL_WTF, tag, msg, tr)
    }

    fun json(tag: String = LOG_TAG, any: Any?) {
        if (any == null) {
            e(tag, "json is null")
            return
        }
        val json = when (any) {
            is String -> any
            is JSONObject -> any.toString()
            else -> Gson().toJson(any)
        }
        d(tag, json.formatJson())
    }
}
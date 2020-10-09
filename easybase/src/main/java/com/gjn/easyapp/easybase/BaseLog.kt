package com.gjn.easyapp.easybase

import com.gjn.easyapp.easylogger.LogUtil

object BaseLog {

    var isDebug = false

    @JvmOverloads
    fun d(tag: String? = "baseLog", msg: String?, tr: Throwable? = null) {
        if (isDebug) LogUtil.d(tag, msg, tr)
    }

    @JvmOverloads
    fun v(tag: String? = "baseLog", msg: String?, tr: Throwable? = null) {
        if (isDebug) LogUtil.v(tag, msg, tr)
    }

    @JvmOverloads
    fun i(tag: String? = "baseLog", msg: String?, tr: Throwable? = null) {
        if (isDebug) LogUtil.i(tag, msg, tr)
    }

    @JvmOverloads
    fun w(tag: String? = "baseLog", msg: String?, tr: Throwable? = null) {
        if (isDebug) LogUtil.w(tag, msg, tr)
    }

    @JvmOverloads
    fun e(tag: String? = "baseLog", msg: String?, tr: Throwable? = null) {
        if (isDebug) LogUtil.e(tag, msg, tr)
    }

    @JvmOverloads
    fun wtf(tag: String? = "baseLog", msg: String?, tr: Throwable? = null) {
        if (isDebug) LogUtil.wtf(tag, msg, tr)
    }

    @JvmOverloads
    fun json(tag: String? = "baseLog", any: Any?) {
        if (isDebug) LogUtil.json(tag, any)
    }
}
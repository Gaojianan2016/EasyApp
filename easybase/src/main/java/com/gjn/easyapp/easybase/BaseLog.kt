package com.gjn.easyapp.easybase

import com.gjn.easyapp.easylogger.LogUtil

object BaseLog {

    private const val TAG = "BaseLog"
    var isDebug = false

    @JvmOverloads
    fun d(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.d(tag, msg, tr)
    }

    @JvmOverloads
    fun v(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.v(tag, msg, tr)
    }

    @JvmOverloads
    fun i(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.i(tag, msg, tr)
    }

    @JvmOverloads
    fun w(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.w(tag, msg, tr)
    }

    @JvmOverloads
    fun e(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.e(tag, msg, tr)
    }

    @JvmOverloads
    fun wtf(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.wtf(tag, msg, tr)
    }

    @JvmOverloads
    fun json(tag: String = TAG, any: Any?) {
        if (isDebug) LogUtil.json(tag, any)
    }
}
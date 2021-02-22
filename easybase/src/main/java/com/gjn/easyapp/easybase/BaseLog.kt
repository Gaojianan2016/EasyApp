package com.gjn.easyapp.easybase

import com.gjn.easyapp.easylogger.LogUtil

object BaseLog {

    private const val TAG = "BaseLog"
    var isDebug = false

    fun d(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.d(tag, msg, tr)
    }

    fun v(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.v(tag, msg, tr)
    }

    fun i(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.i(tag, msg, tr)
    }

    fun w(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.w(tag, msg, tr)
    }

    fun e(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.e(tag, msg, tr)
    }

    fun wtf(msg: String?, tag: String = TAG, tr: Throwable? = null) {
        if (isDebug) LogUtil.wtf(tag, msg, tr)
    }

    fun json(tag: String = TAG, any: Any?) {
        if (isDebug) LogUtil.json(tag, any)
    }
}
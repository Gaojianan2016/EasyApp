package com.gjn.easyapp.easyutils

import android.util.Log

private const val LOG_TAG = "LogExt"

fun logV(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    Log.v(tag, msg, tr)
}

fun logD(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    Log.d(tag, msg, tr)
}

fun logI(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    Log.i(tag, msg, tr)
}

fun logW(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    Log.w(tag, msg, tr)
}

fun logE(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    Log.e(tag, msg, tr)
}

fun logWTF(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    Log.wtf(tag, msg, tr)
}
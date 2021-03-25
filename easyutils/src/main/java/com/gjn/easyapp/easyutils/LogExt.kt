package com.gjn.easyapp.easyutils

import android.util.Log

fun String.logV(tag: String = "LogExt", tr: Throwable? = null) {
    Log.v(tag, this, tr)
}

fun String.logD(tag: String = "LogExt", tr: Throwable? = null) {
    Log.d(tag, this, tr)
}

fun String.logI(tag: String = "LogExt", tr: Throwable? = null) {
    Log.i(tag, this, tr)
}

fun String.logW(tag: String = "LogExt", tr: Throwable? = null) {
    Log.w(tag, this, tr)
}

fun String.logE(tag: String = "LogExt", tr: Throwable? = null) {
    Log.e(tag, this, tr)
}

fun String.logWTF(tag: String = "LogExt", tr: Throwable? = null) {
    Log.wtf(tag, this, tr)
}
package com.gjn.easyapp.easyutils

import android.util.Log

@JvmOverloads
fun String.logV(tag: String = "LogExt", tr: Throwable? = null) {
    Log.v(tag, this, tr)
}

@JvmOverloads
fun String.logD(tag: String = "LogExt", tr: Throwable? = null) {
    Log.d(tag, this, tr)
}

@JvmOverloads
fun String.logI(tag: String = "LogExt", tr: Throwable? = null) {
    Log.i(tag, this, tr)
}

@JvmOverloads
fun String.logW(tag: String = "LogExt", tr: Throwable? = null) {
    Log.w(tag, this, tr)
}

@JvmOverloads
fun String.logE(tag: String = "LogExt", tr: Throwable? = null) {
    Log.e(tag, this, tr)
}

@JvmOverloads
fun String.logWTF(tag: String = "LogExt", tr: Throwable? = null) {
    Log.wtf(tag, this, tr)
}
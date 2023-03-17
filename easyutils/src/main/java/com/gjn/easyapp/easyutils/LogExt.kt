package com.gjn.easyapp.easyutils

import android.util.Log

private const val LOG_TAG = "LogExt"
private const val MAX_SPLIT_LOG_LENGTH = 1280

fun logV(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    logMsg(msg, tr, {
        Log.v(tag, it, tr)
    }, {
        Log.v(tag, it)
    })
}

fun logD(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    logMsg(msg, tr, {
        Log.d(tag, it, tr)
    }, {
        Log.d(tag, it)
    })
}

fun logI(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    logMsg(msg, tr, {
        Log.i(tag, it, tr)
    }, {
        Log.i(tag, it)
    })
}

fun logW(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    logMsg(msg, tr, {
        Log.w(tag, it, tr)
    }, {
        Log.w(tag, it)
    })
}

fun logE(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    logMsg(msg, tr, {
        Log.e(tag, it, tr)
    }, {
        Log.e(tag, it)
    })
}

fun logWTF(msg: String?, tag: String = LOG_TAG, tr: Throwable? = null) {
    logMsg(msg, tr, {
        Log.wtf(tag, it, tr)
    }, {
        Log.wtf(tag, it)
    })
}

/**
 * 可以打印超长log的println
 * */
fun printlnSuper(msg: String?) {
    logMsg(msg) {
        println(it)
    }
}

private fun logMsg(msg: String?, tr: Throwable? = null, throwableBlock: ((String?) -> Unit)? = null, logBlock: (String) -> Unit) {
    val logs = logMsg(msg)
    if (logs.isEmpty()) {
        if (tr == null && msg != null) {
            logBlock.invoke(msg)
        } else {
            throwableBlock?.invoke(msg)
        }
    } else if (logs.size == 1) {
        throwableBlock?.invoke(tr?.message)
        logBlock.invoke(logs[0])
    } else {
        throwableBlock?.invoke(tr?.message)
        logBlock.invoke(logs[0])
        for (i in 1 until logs.size) {
            logBlock.invoke(logs[i])
        }
    }
}

private fun logMsg(msg: String?): List<String> {
    if (msg.isNullOrEmpty()) return emptyList()
    val splitLogs = mutableListOf<String>()
    val splitSize = msg.length / MAX_SPLIT_LOG_LENGTH
    var index = 0
    for (i in 0 until splitSize) {
        splitLogs.add(msg.substring(index, index + MAX_SPLIT_LOG_LENGTH))
        index += MAX_SPLIT_LOG_LENGTH
    }
    if (index < msg.length) {
        splitLogs.add(msg.substring(index))
    }
    return splitLogs
}
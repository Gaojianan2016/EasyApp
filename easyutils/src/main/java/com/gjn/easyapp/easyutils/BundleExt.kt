package com.gjn.easyapp.easyutils

import android.os.Bundle

/**
 * 将 Bundle.get(key) 转换为 Bundle[key]
 * */
inline operator fun <reified T> Bundle?.get(key: String): T? =
    this?.get(key).let { if (it is T) it else null }
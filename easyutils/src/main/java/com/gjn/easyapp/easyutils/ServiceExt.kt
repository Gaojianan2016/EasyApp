package com.gjn.easyapp.easyutils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build


/**
 * 获取全部运行的服务
 * */
@Deprecated("7.0之后只能获取自身app的服务")
fun Context.getAllRunningServiceNames(): Set<String> {
    val names = mutableSetOf<String>()
    activityManager.getRunningServices(0x7FFFFFFF).forEach {
        names.add(it.service.className)
    }
    return names
}

/**
 * 服务是否在运行
 * */
fun Context.isServiceRunning(clazz: Class<out Service>) = isServiceRunning(clazz.name)

/**
 * 服务是否在运行
 * */
fun Context.isServiceRunning(className: String): Boolean {
    if (className.isEmpty()) return false
    return getAllRunningServiceNames().contains(className)
}

////////////////////////////
//// startService
////////////////////////////
inline fun <reified T : Service> Context.startService(
    vararg pairs: Pair<String, *>
) {
    try {
        val intent = intentOf<T>(*pairs).apply {
            this.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

////////////////////////////
//// stopService
////////////////////////////
inline fun <reified T : Service> Context.stopService(): Boolean =
    try {
        stopService(intentOf<T>())
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

////////////////////////////
//// bindService
////////////////////////////
inline fun <reified T : Service> Context.bindService(
    connection: ServiceConnection,
    flags: Int
) {
    try {
        bindService(intentOf<T>(), connection, flags)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
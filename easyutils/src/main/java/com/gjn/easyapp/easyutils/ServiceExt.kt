package com.gjn.easyapp.easyutils

import android.app.ActivityManager
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

fun String.startService(context: Context?) {
    Intent(context, toClass()).startService(context)
}

fun Class<out Service>.startService(context: Context?) {
    Intent(context, this).startService(context)
}

fun Intent.startService(context: Context?) {
    if (context == null) return
    try {
        this.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            println("startForegroundService ")
            context.startForegroundService(this)
        }
        else {
            println("startService ")
            context.startService(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

////////////////////////////
//// stopService
////////////////////////////

fun String.stopService(context: Context?) {
    Intent(context, toClass()).stopService(context)
}

fun Class<out Service>.stopService(context: Context?) {
    Intent(context, this).stopService(context)
}

fun Intent.stopService(context: Context?): Boolean {
    if (context == null) return false
    return try {
        context.stopService(this)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

////////////////////////////
//// bindService
////////////////////////////

fun String.bindService(context: Context?, connection: ServiceConnection, flags: Int) {
    Intent(context, toClass()).bindService(context, connection, flags)
}

fun Class<out Service>.bindService(context: Context?, connection: ServiceConnection, flags: Int) {
    Intent(context, this).bindService(context, connection, flags)
}

fun Intent.bindService(context: Context?, connection: ServiceConnection, flags: Int) {
    if (context == null) return
    try {
        context.bindService(this, connection, flags)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
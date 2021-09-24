package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

/**
 * 获取App meta-data
 * */
fun Context.getAppMetaData(key: String): String {
    if (key.isEmpty()) return ""
    try {
        val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        return ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取Activity meta-data
 * */
fun Activity.getMetaData(key: String) = getActivityMetaData(this::class.java, key)

/**
 * 获取Activity meta-data
 * */
fun Context.getActivityMetaData(clazz: Class<out Activity>, key: String): String {
    if (key.isEmpty()) return ""
    val componentName = ComponentName(this, clazz)
    try {
        val ai = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
        return ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取Service meta-data
 * */
fun Service.getMetaData(key: String) = getServiceMetaData(this::class.java, key)

/**
 * 获取Service meta-data
 * */
fun Context.getServiceMetaData(clazz: Class<out Service>, key: String): String {
    if (key.isEmpty()) return ""
    val componentName = ComponentName(this, clazz)
    try {
        val ai = packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA)
        return ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取BroadcastReceiver meta-data
 * */
fun BroadcastReceiver.getMetaData(context: Context, key: String) =
    context.getReceiverMetaData(this::class.java, key)

/**
 * 获取BroadcastReceiver meta-data
 * */
fun Context.getReceiverMetaData(clazz: Class<out BroadcastReceiver>, key: String): String {
    if (key.isEmpty()) return ""
    val componentName = ComponentName(this, clazz)
    try {
        val ai = packageManager.getReceiverInfo(componentName, PackageManager.GET_META_DATA)
        return ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

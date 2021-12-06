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
    return try {
        val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 获取Activity meta-data
 * */
fun Activity.getMetaData(key: String) = getActivityMetaData(javaClass, key)

/**
 * 获取Activity meta-data
 * */
fun Context.getActivityMetaData(clazz: Class<out Activity>, key: String): String {
    if (key.isEmpty()) return ""
    return try {
        val ai = packageManager.getActivityInfo(ComponentName(this, clazz), PackageManager.GET_META_DATA)
        ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 获取Service meta-data
 * */
fun Service.getMetaData(key: String) = getServiceMetaData(javaClass, key)

/**
 * 获取Service meta-data
 * */
fun Context.getServiceMetaData(clazz: Class<out Service>, key: String): String {
    if (key.isEmpty()) return ""
    return try {
        val ai = packageManager.getServiceInfo(ComponentName(this, clazz), PackageManager.GET_META_DATA)
        ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 获取BroadcastReceiver meta-data
 * */
fun BroadcastReceiver.getMetaData(context: Context, key: String) =
    context.getReceiverMetaData(javaClass, key)

/**
 * 获取BroadcastReceiver meta-data
 * */
fun Context.getReceiverMetaData(clazz: Class<out BroadcastReceiver>, key: String): String {
    if (key.isEmpty()) return ""
    return try {
        val ai = packageManager.getReceiverInfo(ComponentName(this, clazz), PackageManager.GET_META_DATA)
        return ai.metaData.get(key) as String
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

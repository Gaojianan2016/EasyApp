package com.gjn.easyapp.easyutils

import android.Manifest.permission
import android.content.Context
import android.os.Vibrator
import androidx.annotation.RequiresPermission

inline fun Context.vibrator() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

/**
 * 设备开始震动
 * 需要权限 <uses-permission android:name="android.permission.VIBRATE" />
 * */
@RequiresPermission(permission.VIBRATE)
fun Context.startVibrate(milliseconds: Long = 3000) {
    vibrator().vibrate(milliseconds)
}

/**
 * 设备取消震动
 * 需要权限 <uses-permission android:name="android.permission.VIBRATE" />
 * */
@RequiresPermission(permission.VIBRATE)
fun Context.cancelVibrate() {
    vibrator().cancel()
}

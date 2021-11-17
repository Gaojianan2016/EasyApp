package com.gjn.easyapp.easyutils

import android.Manifest.permission.WRITE_SETTINGS
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Surface
import android.view.WindowManager
import androidx.annotation.RequiresPermission

/**
 * 屏幕宽度
 * */
fun Context.screenWidth() = resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 * */
fun Context.screenHeight() = resources.displayMetrics.heightPixels

/**
 * app宽度
 * */
fun Context.appScreenWidth() = Point().apply {
    windowManager.defaultDisplay.getSize(this)
}.x

/**
 * app高度
 * */
fun Context.appScreenHeight() = Point().apply {
    windowManager.defaultDisplay.getSize(this)
}.y

/**
 * 获取屏幕density
 * */
fun getScreenDensity() = Resources.getSystem().displayMetrics.density

/**
 * 获取屏幕densityDpi
 * */
fun getScreenDensityDpi() = Resources.getSystem().displayMetrics.densityDpi

/**
 * Activity是否全屏
 * */
fun Activity.isFullScreen() =
    (window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN

/**
 * Activity全屏
 * 只能在创建视图之前完成
 * */
fun Activity.fullScreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

/**
 * Activity清除全屏
 * 只能在创建视图之前完成
 * */
fun Activity.clearFullScreen() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

/**
 * Activity横屏
 * */
fun Activity.landscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * Activity竖屏
 * */
fun Activity.portrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * 是否横屏
 * */
fun Context.isLandscape() =
    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * 是否竖屏
 * */
fun Context.isPortrait() =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * 屏幕旋转度数
 * */
fun Activity.screenRotation() =
    when (windowManager.defaultDisplay.rotation) {
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        else -> 0
    }

/**
 * 截屏
 * */
fun Activity.screenShot(hasStatusBar: Boolean = true): Bitmap? {
    val bmp = decorViewGroup().toBitmap()
    val dm = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }
    return if (hasStatusBar) Bitmap.createBitmap(
        bmp,
        0,
        0,
        dm.widthPixels,
        dm.heightPixels
    ) else Bitmap.createBitmap(
        bmp,
        0,
        statusBarHeight(),
        dm.widthPixels,
        dm.heightPixels - statusBarHeight()
    )
}

/**
 * 是否锁屏
 * */
fun Context.isScreenLock() = keyguardManager.isKeyguardLocked

/**
 * 设置锁屏时间
 * */
@RequiresPermission(WRITE_SETTINGS)
fun Context.setScreenLockTime(duration: Int) {
    Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, duration)
}

/**
 * 获取锁屏时间
 * */
fun Context.getScreenLockTime() =
    try {
        Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
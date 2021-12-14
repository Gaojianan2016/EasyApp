package com.gjn.easyapp.easyutils

import android.Manifest.permission.WRITE_SETTINGS
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.Surface
import androidx.annotation.RequiresPermission
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

/**
 * 屏幕宽度
 * */
inline val Context.screenWidth: Int get() = resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 * */
inline val Context.screenHeight: Int get() = resources.displayMetrics.heightPixels

/**
 * app宽度
 * */
inline val Context.appScreenWidth: Int
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.width()
        } else {
            Point().apply {
                windowManager.defaultDisplay.getSize(this)
            }.x
        }

/**
 * app高度
 * */
inline val Context.appScreenHeight: Int
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.height()
        } else {
            Point().apply {
                windowManager.defaultDisplay.getSize(this)
            }.y
        }

/**
 * 屏幕density
 * */
inline val screenDensity: Float get() = Resources.getSystem().displayMetrics.density

/**
 * 屏幕densityDpi
 * */
inline val screenDensityDpi: Int get() = Resources.getSystem().displayMetrics.densityDpi

/**
 * 横屏
 * */
inline var Fragment.isLandscape: Boolean
    get() = activity?.isLandscape == true
    set(value) {
        activity?.isLandscape = value
    }

inline var Activity.isLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    set(value) {
        requestedOrientation = if (value) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

/**
 * 竖屏
 * */
inline var Fragment.isPortrait: Boolean
    get() = activity?.isPortrait == true
    set(value) {
        activity?.isPortrait = value
    }

inline var Activity.isPortrait: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    set(value) {
        requestedOrientation = if (value) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

/**
 * 屏幕旋转度数
 * */
inline val Activity.screenRotation: Int
    get() =
        when (display?.rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

/**
 * Activity是否全屏
 * */
inline var Activity.isFullScreen: Boolean
    get() = rootWindowInsetsCompat?.isVisible(WindowInsetsCompat.Type.systemBars()) == true
    set(value) {
        windowInsetsControllerCompat?.run {
            val systemBars = WindowInsetsCompat.Type.systemBars()
            if (value) show(systemBars) else hide(systemBars)
        }
    }

/**
 * 截屏
 * */
fun Activity.screenShot(hasStatusBar: Boolean = true): Bitmap? {
    val bmp = decorViewGroup.toBitmap()
    val x = 0
    val y = if (hasStatusBar) 0 else statusBarHeight
    val width = screenWidth
    val height = if (hasStatusBar) statusBarHeight else screenHeight - statusBarHeight
    return Bitmap.createBitmap(bmp, x, y, width, height)
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
        -1
    }
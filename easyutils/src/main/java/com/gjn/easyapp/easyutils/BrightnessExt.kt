package com.gjn.easyapp.easyutils

import android.Manifest.permission.WRITE_SETTINGS
import android.content.Context
import android.provider.Settings.System.*
import android.view.Window
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission

/**
 * 是否开启自动亮度
 * */
fun Context.isAutoBrightnessEnabled() =
    getInt(contentResolver, SCREEN_BRIGHTNESS_MODE) == SCREEN_BRIGHTNESS_MODE_AUTOMATIC

/**
 * 屏幕亮度 0-255
 * */
inline val Context.screenBrightness: Int get() = getInt(contentResolver, SCREEN_BRIGHTNESS)

/**
 * 获取屏幕最大亮度
 * */
inline val Context.screenMaxBrightness: Int
    get() = try {
        val resId = getSystemIntIdentifier("config_screenBrightnessSettingMaximum")
        if (resId != 0) resources.getInteger(resId) else 255
    } catch (e: Exception) {
        255
    }

/**
 * 获取window亮度
 * */
inline val Window.windowBrightness: Float get() = attributes.screenBrightness * 255

fun Window.setWindowBrightness(@IntRange(from = 0, to = 255) brightness: Int) {
    setWindowBrightnessRatio(brightness / 255f)
}

fun Window.setWindowBrightnessRatio(@FloatRange(from = 0.0, to = 1.0) ratio: Float) {
    val params = attributes
    attributes = params.apply { screenBrightness = ratio }
}

/**
 * 该权限系统app级别才可以使用
 * 需要权限 <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * */
@RequiresPermission(WRITE_SETTINGS)
fun Context.setAutoBrightnessEnabled(enabled: Boolean) =
    try {
        putInt(
            contentResolver, SCREEN_BRIGHTNESS_MODE,
            if (enabled) SCREEN_BRIGHTNESS_MODE_AUTOMATIC else SCREEN_BRIGHTNESS_MODE_MANUAL
        )
    } catch (e: Exception) {
        false
    }

/**
 * 该权限系统app级别才可以使用
 * 需要权限 <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * */
@RequiresPermission(WRITE_SETTINGS)
fun Context.setBrightness(brightness: Int) =
    try {
        val result = putInt(contentResolver, SCREEN_BRIGHTNESS, brightness)
        contentResolver.notifyChange(getUriFor(SCREEN_BRIGHTNESS), null)
        result
    } catch (e: Exception) {
        false
    }
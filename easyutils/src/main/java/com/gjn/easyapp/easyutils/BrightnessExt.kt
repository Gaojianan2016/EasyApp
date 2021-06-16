package com.gjn.easyapp.easyutils

import android.content.Context
import android.provider.Settings
import android.view.Window
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 * 是否开启自动亮度
 * */
fun Context.isAutoBrightnessEnabled() =
    Settings.System.getInt(
        contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE
    ) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC

/**
 * 该权限系统app级别才可以使用
 * 必须获取权限 <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * */
fun Context.setAutoBrightnessEnabled(enabled: Boolean) =
    try {
        Settings.System.putInt(
            contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
            if (enabled) Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC else Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
    } catch (e: Exception) {
        false
    }


/**
 * 屏幕亮度 0-255
 * */
fun Context.getBrightness() =
    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)

/**
 * 获取屏幕最大亮度
 * */
fun Context.getMaxBrightness() =
    try {
        val resId = getAndroidIdentifierInteger("config_screenBrightnessSettingMaximum")
        if (resId != 0) resources.getInteger(resId) else 255
    } catch (e: Exception) {
        255
    }

/**
 * 该权限系统app级别才可以使用
 * 必须获取权限 <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * */
fun Context.setBrightness(brightness: Int) =
    try {
        val result =
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
        contentResolver.notifyChange(
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), null
        )
        result
    } catch (e: Exception) {
        false
    }

fun Window.getWindowBrightness() = attributes.screenBrightness * 255

fun Window.setWindowBrightness(@IntRange(from = 0, to = 255) brightness: Int) {
    setWindowBrightnessRatio(brightness / 255f)
}

fun Window.setWindowBrightnessRatio(@FloatRange(from = 0.0, to = 1.0) ratio: Float) {
    val params = attributes
    attributes = params.apply {
        screenBrightness = ratio
    }
}
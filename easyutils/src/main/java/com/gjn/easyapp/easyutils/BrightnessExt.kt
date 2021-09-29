package com.gjn.easyapp.easyutils

import android.Manifest.permission.WRITE_SETTINGS
import android.content.Context
import android.provider.Settings
import android.provider.Settings.System.*
import android.view.Window
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission

private fun Context.settingsGetInt(name: String) = getInt(contentResolver, name)

private fun Context.settingsPutInt(name: String, value: Int) = putInt(contentResolver, name, value)

/**
 * 是否开启自动亮度
 * */
fun Context.isAutoBrightnessEnabled() =
    settingsGetInt(SCREEN_BRIGHTNESS_MODE) == SCREEN_BRIGHTNESS_MODE_AUTOMATIC

/**
 * 屏幕亮度 0-255
 * */
fun Context.getBrightness() = settingsGetInt(Settings.System.SCREEN_BRIGHTNESS)

/**
 * 获取屏幕最大亮度
 * */
fun Context.getMaxBrightness() =
    try {
        val resId = getSystemIntIdentifier("config_screenBrightnessSettingMaximum")
        if (resId != 0) resources.getInteger(resId) else 255
    } catch (e: Exception) {
        255
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

/**
 * 该权限系统app级别才可以使用
 * 需要权限 <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * */
@RequiresPermission(WRITE_SETTINGS)
fun Context.setAutoBrightnessEnabled(enabled: Boolean) =
    try {
        settingsPutInt(
            SCREEN_BRIGHTNESS_MODE,
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
        val result =
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
        contentResolver.notifyChange(
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), null
        )
        result
    } catch (e: Exception) {
        false
    }
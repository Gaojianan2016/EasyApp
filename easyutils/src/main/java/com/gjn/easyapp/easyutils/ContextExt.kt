package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


//////////////////////////////////
///// Fast ContextManager
//////////////////////////////////

inline val Context.activityManager: ActivityManager
    get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

inline val Context.clipboardManager: ClipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

inline val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.telephonyManager: TelephonyManager
    get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

inline val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

inline val Context.keyguardManager: KeyguardManager
    get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

inline val Context.audioManager: AudioManager
    get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager


/**
 * 制作跳转页面 自定义转场动画
 * */
fun Context.makeCustomAnimationBundle(
    @AnimRes enterResId: Int,
    @AnimRes exitResId: Int
): Bundle? = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()

/**
 * 制作跳转页面 View过渡动画
 * */
fun Context.makeSceneTransitionAnimationBundle(
    vararg sharedElements: View
): Bundle?{
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null
    val activity = when(this){
        is Activity -> this
        is Fragment -> activity
        else -> null
    } ?: return null
    val pairs = arrayOfNulls<androidx.core.util.Pair<View, String>>(sharedElements.size)
    sharedElements.forEachIndexed { index, view ->
        pairs[index] = androidx.core.util.Pair.create(view, view.transitionName)
    }
    return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs).toBundle()
}

//fun Context.hasNavigationBar(): Boolean {
//    var result = false
//    val resId = getSystemBoolIdentifier("config_showNavigationBar")
//    if (resId > 0) result = resources.getBoolean(resId)
//    try {
//        //判断是否修改过底边栏
//        when ("android.os.SystemProperties".toClass().invokeMethod(
//            "get",
//            parameterTypes = arrayOf(String::class.java),
//            args = arrayOf("qemu.hw.mainkeys")
//        ) as String) {
//            "0" -> result = true
//            "1" -> result = false
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return result
//}

/**
 * 判断权限
 * */
fun Context.checkPermission(permission: String): Boolean =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
    else ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED

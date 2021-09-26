package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * 5.0之后启动页面 跳转动画Bundle创建
 * */
fun Context?.createOptionsBundle(
    @AnimRes enterAnim: Int?,
    @AnimRes exitAnim: Int?
): Bundle? {
    if (this == null) return null
    return ActivityOptionsCompat.makeCustomAnimation(this, enterAnim ?: -1, exitAnim ?: -1)
        .toBundle()
}

/**
 * 5.0之后启动页面 转场动画Bundle创建
 * */
fun Context?.createOptionsBundle(vararg sharedElements: View): Bundle? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null
    val activity = when (this) {
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

fun Context.hasNavigationBar(): Boolean {
    var result = false
    val resId = ResourcesUtils.getInternalBoolean("config_showNavigationBar")
    if (resId > 0) result = resources.getBoolean(resId)
    try {
        //判断是否修改过底边栏
        when ("android.os.SystemProperties".toClass().invokeMethod(
            "get",
            parameterTypes = arrayOf(String::class.java),
            args = arrayOf("qemu.hw.mainkeys")
        ) as String) {
            "0" -> result = true
            "1" -> result = false
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

/**
 * 展示吐司
 * */
fun Context.showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

/**
 * 展示吐司
 * */
fun Context.showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

/**
 * 判断权限
 * */
fun Context.checkPermission(permission: String) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
    else ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

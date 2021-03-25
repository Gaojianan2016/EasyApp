package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment

fun Class<out Activity>.startActivity(
    context: Context?,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    enterAnim: Int? = null, exitAnim: Int? = null,
    vararg sharedElements: View
) = context?.startActivity(this, extras, extrasMap, enterAnim, exitAnim, *sharedElements)

fun Class<out Activity>.startActivity(
    context: Context?,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    options: Bundle? = null
) = context?.startActivity(this, extras, extrasMap, options)

fun Context?.startActivity(
    clz: Class<out Activity>,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    enterAnim: Int? = null, exitAnim: Int? = null,
    vararg sharedElements: View
) {
    if (enterAnim == null || exitAnim == null) {
        startActivity(clz, extras, extrasMap, createOptionsBundle(this, *sharedElements))
        return
    }
    startActivity(clz, extras, extrasMap, createOptionsBundle(this, enterAnim, exitAnim))
}

fun Context?.startActivity(
    clz: Class<out Activity>,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    options: Bundle? = null
) = Intent(this, clz).startActivity(this, extras, extrasMap, options)

fun Intent.startActivity(
    context: Context?,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    options: Bundle? = null
) {
    val activity = when (context) {
        is Activity -> context
        is Fragment -> context.activity
        else -> null
    } ?: return
    if (extras == null) {
        this.put(extrasMap)
    } else {
        this.putExtras(extras)
    }
    activity.startActivity(this, options)
}

fun Class<out Activity>.startActivityForResult(
    context: Context?, requestCode: Int,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    enterAnim: Int? = null, exitAnim: Int? = null,
    vararg sharedElements: View
) = context?.startActivityForResult(
    this, requestCode,
    extras, extrasMap,
    enterAnim, exitAnim,
    *sharedElements
)

fun Class<out Activity>.startActivityForResult(
    context: Context?, requestCode: Int,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    options: Bundle? = null
) = context?.startActivityForResult(this, requestCode, extras, extrasMap, options)

fun Context?.startActivityForResult(
    clz: Class<out Activity>, requestCode: Int,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    enterAnim: Int? = null, exitAnim: Int? = null,
    vararg sharedElements: View
) {
    if (enterAnim == null || exitAnim == null) {
        startActivityForResult(
            clz, requestCode,
            extras, extrasMap,
            createOptionsBundle(this, *sharedElements)
        )
        return
    }
    startActivityForResult(
        clz, requestCode,
        extras, extrasMap,
        createOptionsBundle(this, enterAnim, exitAnim)
    )
}

fun Context?.startActivityForResult(
    clz: Class<out Activity>, requestCode: Int,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    options: Bundle? = null
) = Intent(this, clz).startActivityForResult(this, requestCode, extras, extrasMap, options)

fun Intent.startActivityForResult(
    context: Context?, requestCode: Int,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    options: Bundle? = null
) {
    if (extras == null) {
        this.put(extrasMap)
    } else {
        this.putExtras(extras)
    }
    when (context) {
        is Activity -> context.startActivityForResult(this, requestCode, options)
        is Fragment -> context.startActivityForResult(this, requestCode, options)
    }
}

/**
 * 判断是否开启软键盘
 * 判断屏幕可见区域是否被占用了2/3
 * */
fun Activity.isKeyboardShowing(): Boolean {
    //获取当前屏幕内容的高度
    val screenHeight = window.decorView.height
    //获取View可见区域的bottom
    val rect = Rect()
    //DecorView即为activity的顶级view
    window.decorView.getWindowVisibleDisplayFrame(rect)
    //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
    //选取screenHeight*2/3进行判断
    return screenHeight * 2 / 3 > rect.bottom
}

/**
 * 5.0之后启动页面 跳转动画Bundle创建
 * */
private fun createOptionsBundle(context: Context?, enterAnim: Int?, exitAnim: Int?): Bundle? {
    if (context == null) return null
    return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim ?: -1, exitAnim ?: -1)
        .toBundle()
}

/**
 * 5.0之后启动页面 转场动画Bundle创建
 * */
private fun createOptionsBundle(context: Context?, vararg sharedElements: View): Bundle? {
    if (sharedElements.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null
    val activity = when (context) {
        is Activity -> context
        is Fragment -> context.activity
        else -> null
    } ?: return null
    val pairs = arrayOf<androidx.core.util.Pair<View, String>>()
    sharedElements.forEachIndexed { index, view ->
        pairs[index] = androidx.core.util.Pair.create(view, view.transitionName)
    }
    return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs).toBundle()
}
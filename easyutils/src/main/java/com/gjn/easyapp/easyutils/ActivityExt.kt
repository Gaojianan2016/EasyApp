package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment

fun Class<out Activity>.startActivity(
    context: Context?,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    @AnimRes enterAnim: Int? = null, @AnimRes exitAnim: Int? = null,
    sharedElements: Array<View>? = null
) = context?.startActivity(this, extras, extrasMap, enterAnim, exitAnim, sharedElements)

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
    @AnimRes enterAnim: Int? = null, @AnimRes exitAnim: Int? = null,
    sharedElements: Array<View>? = null
) {
    if (enterAnim == null || exitAnim == null) {
        startActivity(
            clz,
            extras,
            extrasMap,
            sharedElements?.let { createOptionsBundle(this, *it) })
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
    if (extras == null) put(extrasMap) else putExtras(extras)
    activity.startActivity(this, options)
}

fun Class<out Activity>.startActivityForResult(
    context: Context?, requestCode: Int,
    extras: Bundle? = null,
    extrasMap: Map<String, Any?> = mapOf(),
    @AnimRes enterAnim: Int? = null, @AnimRes exitAnim: Int? = null,
    sharedElements: Array<View>? = null
) = context?.startActivityForResult(
    this, requestCode,
    extras, extrasMap,
    enterAnim, exitAnim,
    sharedElements
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
    @AnimRes enterAnim: Int? = null, @AnimRes exitAnim: Int? = null,
    sharedElements: Array<View>? = null
) {
    if (enterAnim == null || exitAnim == null) {
        startActivityForResult(
            clz, requestCode,
            extras, extrasMap,
            sharedElements?.let { createOptionsBundle(this, *it) }
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
    if (extras == null) put(extrasMap) else putExtras(extras)
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
 * 获取入口Activity类名
 * */
fun Context.getLauncherActivity(pkg: String = packageName): String {
    if (pkg.isEmpty()) return ""
    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        `package` = pkg
    }
    val info = packageManager.queryIntentActivities(intent, 0)
    if (info.size == 0) return ""
    return info[0].activityInfo.name
}

/**
 * 5.0之后启动页面 跳转动画Bundle创建
 * */
fun createOptionsBundle(
    context: Context?,
    @AnimRes enterAnim: Int?,
    @AnimRes exitAnim: Int?
): Bundle? {
    if (context == null) return null
    return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim ?: -1, exitAnim ?: -1)
        .toBundle()
}

/**
 * 5.0之后启动页面 转场动画Bundle创建
 * */
fun createOptionsBundle(context: Context?, vararg sharedElements: View): Bundle? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null
    val activity = when (context) {
        is Activity -> context
        is Fragment -> context.activity
        else -> null
    } ?: return null
    val pairs = arrayOfNulls<androidx.core.util.Pair<View, String>>(sharedElements.size)
    sharedElements.forEachIndexed { index, view ->
        pairs[index] = androidx.core.util.Pair.create(view, view.transitionName)
    }
    return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs).toBundle()
}
package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
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
            clz, extras, extrasMap,
            sharedElements?.let { createOptionsBundle(*it) }
        )
        return
    }
    startActivity(clz, extras, extrasMap, createOptionsBundle(enterAnim, exitAnim))
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
            sharedElements?.let { createOptionsBundle(*it) }
        )
        return
    }
    startActivityForResult(
        clz, requestCode,
        extras, extrasMap,
        createOptionsBundle(enterAnim, exitAnim)
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
    if (context == null) return
    if (extras == null) put(extrasMap) else putExtras(extras)
    when (context) {
        is Activity -> context.startActivityForResult(this, requestCode, options)
        is Fragment -> context.startActivityForResult(this, requestCode, options)
    }
}

/**
 * 获取 android.R.id.content 帧布局
 * */
fun Activity.contentLayout(): FrameLayout = window.findViewById(android.R.id.content)
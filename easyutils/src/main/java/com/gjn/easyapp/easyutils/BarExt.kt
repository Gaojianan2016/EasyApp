package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt


fun Context.getAndroidIdentifier(name: String, defType: String) =
    resources.getIdentifier(name, defType, "android")

fun Context.getAndroidIdentifierId(name: String) = getAndroidIdentifier(name, "id")

fun Context.getAndroidIdentifierLayout(name: String) = getAndroidIdentifier(name, "layout")

fun Context.getAndroidIdentifierDrawable(name: String) = getAndroidIdentifier(name, "drawable")

fun Context.getAndroidIdentifierColors(name: String) = getAndroidIdentifier(name, "colors")

fun Context.getAndroidIdentifierDimen(name: String) = getAndroidIdentifier(name, "dimen")

fun Context.getAndroidIdentifierBool(name: String) = getAndroidIdentifier(name, "bool")

private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
private const val TAG_OFFSET = "TAG_OFFSET"
private const val KEY_OFFSET = -0x539

/**
 * 获取状态栏高度(px)
 * */
fun Context.statusBarHeight(): Int {
    val resId = getAndroidIdentifierDimen("status_bar_height")
    return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
}

/**
 * 设置是否显示状态栏
 * */
fun Activity.setStatusBarVisibility(isVisible: Boolean) {
    if (isVisible) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.findViewWithTag<View>(TAG_STATUS_BAR)?.visible()
        window.decorView.findViewWithTag<View>(TAG_OFFSET)?.addMarginTopEqualStatusBarHeight()
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.findViewWithTag<View>(TAG_STATUS_BAR)?.gone()
        window.decorView.findViewWithTag<View>(TAG_OFFSET)?.subtractMarginTopEqualStatusBarHeight()

    }
}

/**
 * 状态栏是否可见
 * */
fun Activity.isStatusBarVisible() =
    window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0

/**
 * 设置状态栏模式
 * */
fun Activity.setStatusBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var vis = window.decorView.systemUiVisibility
        vis = if (isLightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = vis
    }
}

/**
 * 状态栏模式是否是亮
 * */
fun Activity.isStatusBarLightMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
    }
    return false
}

/**
 * 设置状态栏颜色
 * */
fun Activity.setStatusBarColor(@ColorInt color: Int, isDecor: Boolean = false): View {
    transparentStatusBar()
    return applyStatusBarColor(color, isDecor)
}

/**
 *
 * */
private fun Activity.applyStatusBarColor(color: Int, isDecor: Boolean): View {
    val parent: ViewGroup =
        if (isDecor) window.decorView as ViewGroup else window.findViewById(android.R.id.content)
    var fakeStatusBarView = parent.findViewWithTag<View>(TAG_STATUS_BAR)
    if (fakeStatusBarView != null) {
        if (fakeStatusBarView.visibility == View.GONE) {
            fakeStatusBarView.visible()
        }
        fakeStatusBarView.setBackgroundColor(color)
    } else {
        fakeStatusBarView = View(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight()
            )
            setBackgroundColor(color)
            tag = TAG_STATUS_BAR
        }
        parent.addView(fakeStatusBarView)
    }
    return fakeStatusBarView
}

/**
 * 透明状态栏
 * */
fun Activity.transparentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = option or vis
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}


/**
 * 给view添加状态栏高度的TopMargin
 * */
fun View.addMarginTopEqualStatusBarHeight() {
    tag = TAG_OFFSET
    //存在offSet 不做处理
    if (getTag(KEY_OFFSET) == true) return
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        params.leftMargin, params.topMargin + context.statusBarHeight(),
        params.rightMargin, params.bottomMargin
    )
    //设置offSet
    setTag(KEY_OFFSET, true)
}

/**
 * 给view减去状态栏高度的TopMargin
 * */
fun View.subtractMarginTopEqualStatusBarHeight() {
    //不存在offSet 不做处理
    if (getTag(KEY_OFFSET) != true) return
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        params.leftMargin, params.topMargin - context.statusBarHeight(),
        params.rightMargin, params.bottomMargin
    )
    //设置offSet
    setTag(KEY_OFFSET, false)
}

//fun Context.navigationBarHeight(): Int {
//    val resId = getAndroidIdentifierDimen("navigation_bar_height")
//    return if (hasNavigationBar() && resId > 0) resources.getDimensionPixelSize(resId) else 0
//}
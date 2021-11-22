package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout

private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
private const val TAG_OFFSET = "TAG_OFFSET"
private const val KEY_OFFSET = -0x539

/**
 * 获取状态栏高度(px)
 * */
fun Context.statusBarHeight(): Int {
    val resId = getSystemDimenIdentifier("status_bar_height")
    return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
}

/**
 * 设置是否显示状态栏 全屏相关有FLAG_FULLSCREEN
 * */
fun Activity.setStatusBarVisibility(isVisible: Boolean) {
    if (isVisible) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        showFakeStatusBarView()
        addTopOffsetStatusBarHeight()
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideFakeStatusBarView()
        subtractTopOffsetStatusBarHeight()
    }
}

/**
 * 状态栏是否可见
 * */
fun Activity.isStatusBarVisible() =
    window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0

/**
 * 设置状态栏 Light模式
 * */
fun Activity.setStatusBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var vis = decorViewGroup.systemUiVisibility
        vis = if (isLightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorViewGroup.systemUiVisibility = vis
    }
}

/**
 * 状态栏 Light模式是否开启
 * true-黑色, false-白色
 * */
fun Activity.isStatusBarLightMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return decorViewGroup.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
    }
    return false
}

/**
 * 设置状态栏颜色
 * isDecor true - 添加到 DecorView(有状态栏高度), false - 添加到 ContentView(无状态栏高度)
 * */
fun Activity.setStatusBarColor(@ColorInt color: Int, isDecor: Boolean = false): View {
    transparentStatusBar()
    return applyStatusBarColor(color, isDecor)
}

/**
 * 设置(fakeStatusBar)状态栏颜色
 * */
fun View.setStatusBarColor(@ColorInt color: Int) {
    (context as Activity).transparentStatusBar()
    visible()
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams.height = context.statusBarHeight()
    setBackgroundColor(color)
}

/**
 * DrawerLayout 设置状态栏颜色
 * DrawerLayout 布局需要设置 android:fitsSystemWindows="true"
 * isTop true - 设置DrawerLayout置顶, false - 反之不设
 * */
fun Activity.setStatusBarColor4Drawer(
    drawer: DrawerLayout,
    fakeStatusBar: View,
    @ColorInt color: Int,
    isTop: Boolean = false
) {
    drawer.fitsSystemWindows = false
    fakeStatusBar.setStatusBarColor(color)
    for (i in 0 until drawer.childCount) {
        drawer.getChildAt(i).fitsSystemWindows = false
    }
    if (isTop) {
        hideFakeStatusBarView()
    } else {
        setStatusBarColor(color)
    }
}

/**
 * 透明状态栏
 * */
fun Activity.transparentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = decorViewGroup.systemUiVisibility
        decorViewGroup.systemUiVisibility = option or vis
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

/**
 * 获取actionBar高度(px)
 * */
fun Application.actionBarHeight(): Int {
    val tValue = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tValue, true)) {
        TypedValue.complexToDimensionPixelSize(tValue.data, resources.displayMetrics)
    } else {
        0
    }
}

/**
 * 获取导航栏高度(px)
 * */
fun Context.navigationBarHeight(): Int {
    val resId = getSystemDimenIdentifier("navigation_bar_height")
    return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
}

/**
 * 设置导航栏是否显示
 * */
fun Activity.setNavBarVisibility(isVisible: Boolean) {
    val decorView = decorViewGroup
    val child = decorView.getChildViewByResourceName("navigationBarBackground")
    if (isVisible) {
        child?.visible()
    } else {
        child?.invisible()
    }
    val uiOptions =
        (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    if (isVisible) {
        decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
    } else {
        decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
    }
}

/**
 * 导航栏是否显示
 * onWindowFocusChanged 可调用获取结果
 * */
fun Activity.isNavBarVisible(): Boolean {
    var isVisible = false
    val decorView = decorViewGroup
    val child = decorView.getChildViewByResourceName("navigationBarBackground")
    if (child?.isVisible() == true) {
        isVisible = true
    }
    if (isVisible) {
        //三星android 10以下存在导航栏bug
        if (isPhoneRom(PhoneRom.ROM_SAMSUNG) && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                return Settings.Global.getInt(
                    contentResolver, "navigationbar_hide_bar_enabled"
                ) == 0
            } catch (e: Exception) {
            }
        }
        isVisible = (decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0
    }
    return isVisible
}

/**
 * 是否支持导航栏
 * */
fun Application.isSupportNavBar(): Boolean {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val size = Point()
    val realSize = Point()
    wm.defaultDisplay.getSize(size)
    wm.defaultDisplay.getRealSize(realSize)
    return realSize.y != size.y || realSize.x != size.x
}

/**
 * 设置导航栏颜色 api21以上
 * */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.setNavBarColor(@ColorInt color: Int) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.navigationBarColor = color
}

/**
 * 获取导航栏颜色 api21以上
 * */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.navigationBarColor() = window.navigationBarColor

/**
 * 设置导航栏 开启关闭LightMode api26以上
 * true-黑色, false-白色
 * */
@RequiresApi(Build.VERSION_CODES.O)
fun Activity.setNavBarLightMode(isLightMode: Boolean) {
    var vis = decorViewGroup.systemUiVisibility
    vis = if (isLightMode) {
        vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    } else {
        vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
    decorViewGroup.systemUiVisibility = vis
}

/**
 * 导航栏是否开启LightMode api26以上
 * */
fun Activity.isNavBarLightMode() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) (decorViewGroup.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0
    else false

private fun Activity.showFakeStatusBarView() {
    decorViewGroup.findViewWithTag<View>(TAG_STATUS_BAR)?.visible()
}

private fun Activity.hideFakeStatusBarView() {
    decorViewGroup.findViewWithTag<View>(TAG_STATUS_BAR)?.gone()
}

private fun Activity.addTopOffsetStatusBarHeight() {
    decorViewGroup.findViewWithTag<View>(TAG_OFFSET)?.addMarginTopEqualStatusBarHeight()
}

private fun Activity.subtractTopOffsetStatusBarHeight() {
    decorViewGroup.findViewWithTag<View>(TAG_OFFSET)?.subtractMarginTopEqualStatusBarHeight()
}

private fun Activity.applyStatusBarColor(color: Int, isDecor: Boolean): View {
    val parent: ViewGroup = if (isDecor) decorViewGroup else contentFrameLayout
    var fakeStatusBarView = parent.findViewWithTag<View>(TAG_STATUS_BAR)
    if (fakeStatusBarView != null) {
        if (fakeStatusBarView.visibility == View.GONE) {
            fakeStatusBarView.visible()
        }
        fakeStatusBarView.setBackgroundColor(color)
    } else {
        //创建fakeStatusBarView
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
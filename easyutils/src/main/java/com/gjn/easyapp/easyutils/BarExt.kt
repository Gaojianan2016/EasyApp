package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.drawerlayout.widget.DrawerLayout

const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
const val TAG_OFFSET = "TAG_OFFSET"
const val KEY_OFFSET = -0x539

/**
 * 状态栏高度(px)
 * */
inline val Context.statusBarHeight: Int
    get() {
        val resId = getSystemDimenIdentifier("status_bar_height")
        return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
    }

/**
 * 状态栏是否显示
 * 设置隐藏 只是单纯的隐藏不是沉浸式
 * */
inline var Activity.isStatusBarVisible: Boolean
    get() = rootWindowInsetsCompat?.isVisible(WindowInsetsCompat.Type.statusBars()) == true
    set(value) {
        windowInsetsControllerCompat?.run {
            if (value) show(WindowInsetsCompat.Type.statusBars()) else hide(WindowInsetsCompat.Type.statusBars())
        }
    }

/**
 * 状态栏Light模式
 * */
inline var Activity.isStatusBarLightMode: Boolean
    get() = windowInsetsControllerCompat?.isAppearanceLightStatusBars == true
    set(value) {
        windowInsetsControllerCompat?.isAppearanceLightStatusBars = value
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
    layoutParams.height = context.statusBarHeight
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
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        updateMargins(top = topMargin + context.statusBarHeight)
    }
    //设置offSet
    setTag(KEY_OFFSET, true)
}

/**
 * 给view减去状态栏高度的TopMargin
 * */
fun View.subtractMarginTopEqualStatusBarHeight() {
    //不存在offSet 不做处理
    if (getTag(KEY_OFFSET) != true) return
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        updateMargins(top = topMargin - context.statusBarHeight)
    }
    //设置offSet
    setTag(KEY_OFFSET, false)
}

/**
 * actionBar高度(px)
 * */
inline val Application.actionBarHeight: Int
    get() {
        val tValue = TypedValue()
        return if (theme.resolveAttribute(android.R.attr.actionBarSize, tValue, true)) {
            TypedValue.complexToDimensionPixelSize(tValue.data, resources.displayMetrics)
        } else {
            0
        }
    }

/**
 * 导航栏高度(px)
 * */
inline val Context.navigationBarHeight: Int
    get() {
        val resId = getSystemDimenIdentifier("navigation_bar_height")
        return if (resId > 0) resources.getDimensionPixelSize(resId) else 0
    }

/**
 * 导航栏显示
 * */
inline var Activity.isNavBarVisible: Boolean
    get() = rootWindowInsetsCompat?.isVisible(WindowInsetsCompat.Type.navigationBars()) == true
    set(value) {
        windowInsetsControllerCompat?.run {
            if (value) show(WindowInsetsCompat.Type.navigationBars()) else hide(WindowInsetsCompat.Type.navigationBars())
        }
    }

/**
 * 是否支持导航栏
 * */
fun Application.isSupportNavBar(): Boolean {
    val size = Point()
    val realSize = Point()
    windowManager.run {
        defaultDisplay.getSize(size)
        defaultDisplay.getRealSize(realSize)
    }
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
 * 导航栏LightMode
 * */
inline var Activity.isNavBarLightMode: Boolean
    get() = windowInsetsControllerCompat?.isAppearanceLightNavigationBars == true
    set(value) {
        windowInsetsControllerCompat?.isAppearanceLightNavigationBars = value
    }

inline fun Activity.showFakeStatusBarView() {
    decorViewGroup.findViewWithTag<View>(TAG_STATUS_BAR)?.visible()
}

inline fun Activity.hideFakeStatusBarView() {
    decorViewGroup.findViewWithTag<View>(TAG_STATUS_BAR)?.gone()
}

inline fun Activity.addTopOffsetStatusBarHeight() {
    decorViewGroup.findViewWithTag<View>(TAG_OFFSET)?.addMarginTopEqualStatusBarHeight()
}

inline fun Activity.subtractTopOffsetStatusBarHeight() {
    decorViewGroup.findViewWithTag<View>(TAG_OFFSET)?.subtractMarginTopEqualStatusBarHeight()
}

/**
 * 设置fakeBarView颜色
 * */
private fun Activity.applyStatusBarColor(color: Int, isDecor: Boolean): View {
    val parent = if (isDecor) decorViewGroup else contentFrameLayout
    var fakeStatusBarView = parent.findViewWithTag<View>(TAG_STATUS_BAR)
    if (fakeStatusBarView != null) {
        if (fakeStatusBarView.isGone) fakeStatusBarView.visible()
        fakeStatusBarView.setBackgroundColor(color)
    } else {
        //创建fakeStatusBarView
        fakeStatusBarView = View(this).apply {
            setBackgroundColor(color)
            tag = TAG_STATUS_BAR
        }
        parent.addView(
            fakeStatusBarView,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
        )
    }
    return fakeStatusBarView
}
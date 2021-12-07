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
 * 状态栏显示
 * */
inline var Activity.isStatusBarVisible: Boolean
    get() = window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
    set(value) {
        if (value) {
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
 * 状态栏Light模式
 * */
inline var Activity.isStatusBarLightMode: Boolean
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return decorViewGroup.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
        }
        return false
    }
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var vis = decorViewGroup.systemUiVisibility
            vis = if (value) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorViewGroup.systemUiVisibility = vis
        }
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
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        params.leftMargin, params.topMargin + context.statusBarHeight,
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
        params.leftMargin, params.topMargin - context.statusBarHeight,
        params.rightMargin, params.bottomMargin
    )
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
    get() {
        var isVisible = false
        val decorView = decorViewGroup
        val child = decorView.findChildViewByResourceName("navigationBarBackground")
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
    set(value) {
        val decorView = decorViewGroup
        val child = decorView.findChildViewByResourceName("navigationBarBackground")
        if (value) child?.visible() else child?.invisible()
        val uiOptions = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        if (value) {
            decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
        }
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
 * 导航栏LightMode
 * */
@get:RequiresApi(Build.VERSION_CODES.O)
@set:RequiresApi(Build.VERSION_CODES.O)
inline var Activity.isNavBarLightMode: Boolean
    get() = (decorViewGroup.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0
    set(value) {
        var vis = decorViewGroup.systemUiVisibility
        vis = if (value) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
        decorViewGroup.systemUiVisibility = vis
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
                statusBarHeight
            )
            setBackgroundColor(color)
            tag = TAG_STATUS_BAR
        }
        parent.addView(fakeStatusBarView)
    }
    return fakeStatusBarView
}
package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.drawerlayout.widget.DrawerLayout

object StatusBarUtil {

    fun statusBarMode(activity: Activity, isDark: Boolean = false, colorOrDrawable: Any? = null) {
        //透明状态栏
        activity.transparentStatusBar()
        //设置StatusBar背景颜色
        when (colorOrDrawable) {
            is Int -> {
                activity.setBarBackgroundColor(colorOrDrawable)
            }
            is Drawable -> {
                activity.setBarBackgroundDrawable(colorOrDrawable)
            }
        }
        //FlymeUI单独设置
        if (setFlymeBarMode(activity, isDark)) {
            return
        }
        //MIUI单独设置
        if (setMIUIBarMode(activity, isDark)) {
            return
        }
        //api23以上才可以修改 状态栏字体颜色
        setStatusBarModeV23(activity, isDark)
    }

    fun setContentViewFitsSystemWindows(activity: Activity, flag: Boolean) {
        val content = activity.contentFrameLayout()
        for (i in 0 until content.childCount) {
            val child = content.getChildAt(i)
            if (child !is BarView) {
                if (child is DrawerLayout) {
                    //对DrawerLayout+NavigationView生成的侧滑进行置顶修改
                    child.clipToPadding = false
                }
                child.fitsSystemWindows = flag
            }
        }

    }

    private fun setFlymeBarMode(activity: Activity, dark: Boolean): Boolean {
        try {
            val layoutParams = activity.window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(layoutParams)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(layoutParams, value)
            activity.window.attributes = layoutParams
            return true
        } catch (e: Exception) {
        }
        return false
    }

    private fun setMIUIBarMode(activity: Activity, dark: Boolean): Boolean {
        try {
            val clazz: Class<*> = activity.window.javaClass
            val layoutParams = "android.view.MiuiWindowManager\$LayoutParams".toClass()
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val modeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            if (dark) {
                //透明状态栏 黑色字体
                extraFlagField.invoke(activity.window, modeFlag, modeFlag)
            } else {
                //白色字体
                extraFlagField.invoke(activity.window, 0, modeFlag)
            }
            //开发版 7.7.13及以后版本采用了安卓6.0系统API
            setStatusBarModeV23(activity, dark)
            return true
        } catch (e: Exception) {
        }
        return false
    }

    private fun setStatusBarModeV23(activity: Activity, dark: Boolean) {
        val decorView = activity.decorViewGroup()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }
}

class BarView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resources.displayMetrics.widthPixels
        val h: Int = context.statusBarHeight()
        setMeasuredDimension(w, h)
    }
}

fun Activity.setBarBackgroundColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //大于21
        window.statusBarColor = color
        return
    }
    val content = contentFrameLayout()
    //清除旧的BarView
    content.clearChildView(BarView::class.java)
    val barView = BarView(this)
    barView.setBackgroundResource(color)
    //添加新的BarView
    content.addView(barView)
}

fun Activity.setBarBackgroundDrawable(drawable: Drawable) {
    val content = contentFrameLayout()
    //清除旧的BarView
    content.clearChildView(BarView::class.java)
    val barView = BarView(this)
    barView.background = drawable
    //添加新的BarView
    content.addView(barView)
}

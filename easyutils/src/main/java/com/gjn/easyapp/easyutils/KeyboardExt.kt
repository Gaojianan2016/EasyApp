package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlin.math.abs

/**
 * 强制显示软键盘
 * */
fun Context.showSoftInput() {
    inputMethodManager.toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

/**
 * 强制显示软键盘
 * */
fun Activity.showSoftInput() {
    if (!isSoftInputVisible) toggleSoftInput()
}

/**
 * 强制显示软键盘并获取焦点
 * */
fun View.showSoftInput(flags: Int = 0) {
    val imm = context.inputMethodManager
    //获取焦点
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    imm.showSoftInput(this, flags, object : ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN || resultCode == InputMethodManager.RESULT_HIDDEN) {
                context.toggleSoftInput()
            }
        }
    })
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * 强制隐藏软键盘
 * */
fun Activity.hideSoftInput() {
    var focusView = window.currentFocus
    if (focusView == null) {
        focusView = decorViewGroup.findViewWithTag("keyboardTagView")
        if (focusView == null) {
            focusView = EditText(window.context).apply { tag = "keyboardTagView" }
            decorViewGroup.addView(focusView, 0, 0)
        }
        focusView.requestFocus()
    }
    focusView.hideSoftInput()
}

/**
 * 强制隐藏软键盘
 * */
fun View.hideSoftInput() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 切换软键盘显示隐藏
 * */
fun Context.toggleSoftInput() {
    inputMethodManager.toggleSoftInput(0, 0)
}

/**
 * 是否显示软键盘
 * */
inline val Activity.isSoftInputVisible: Boolean get() = decorViewInvisibleHeight > 0

/**
 * 监听软键盘高度变化
 * */
fun Activity.registerSoftInputChangedListener(block: (Int) -> Unit) {
    val flags = window.attributes.flags
    if ((flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    val content = contentFrameLayout
    val oldInvisibleHeight = arrayOf(decorViewInvisibleHeight)
    val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val height = decorViewInvisibleHeight
        if (oldInvisibleHeight[0] != height) {
            block.invoke(height)
            oldInvisibleHeight[0] = height
        }
    }
    content.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    content.setTag(-0x456, globalLayoutListener)
}

/**
 * 取消监听软键盘高度变化
 * */
fun Activity.unregisterSoftInputChangedListener() {
    val content = contentFrameLayout
    content.getTag(-0x456)?.let {
        if (it is ViewTreeObserver.OnGlobalLayoutListener) {
            content.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
    }
}

/**
 * 修复5497bug 全面屏软键盘bug
 * Activity不要设置adjustResize属性
 * */
fun Activity.fixAndroidBug5497() {
    val softInputMode = window.attributes.softInputMode
    //softInputMode & ~SOFT_INPUT_ADJUST_RESIZE
    window.setSoftInputMode(softInputMode and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE.inv())
    val content = contentFrameLayout
    val child = content.getChildAt(0)
    val paddingBottom = content.paddingBottom
    val oldInvisibleHeight = arrayOf(contentViewInvisibleHeight)
    content.viewTreeObserver.addOnGlobalLayoutListener {
        val height = contentViewInvisibleHeight
        if (oldInvisibleHeight[0] != height) {
            child.setPadding(
                child.left,
                child.top,
                child.right,
                paddingBottom + decorViewInvisibleHeight
            )
            oldInvisibleHeight[0] = height
        }
    }
}

/**
 * 修复软键盘泄露
 * 反射清空软键盘内的数据
 * */
@Deprecated("androidx 已修复")
fun Activity.fixSoftInputLeaks() {
    val imm = inputMethodManager
    val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
    for (leakView in leakViews) {
        try {
            val field = InputMethodManager::class.java.getDeclaredField(leakView)
            val obj = field.get(imm)
            if (obj !is View) continue
            if (obj.rootView == decorViewGroup.rootView) {
                field.set(imm, null)
            }
        } catch (e: Exception) {
        }
    }
}

/**
 * 获取 android.R.id.content 未显示高度
 * */
inline val Activity.contentViewInvisibleHeight: Int
    get() {
        val content = contentFrameLayout
        val outRect = Rect()
        content.getWindowVisibleDisplayFrame(outRect)
        val delta = abs(content.bottom - outRect.bottom)
        //差值超过通知栏+状态栏高度
        return if (delta > navigationBarHeight() + statusBarHeight()) delta else 0
    }

/**
 * 获取decorView未显示高度
 * */
inline val Activity.decorViewInvisibleHeight: Int
    get() {
        val outRect = Rect()
        decorViewGroup.getWindowVisibleDisplayFrame(outRect)
        val delta = abs(decorViewGroup.bottom - outRect.bottom)
        //差值超过通知栏+状态栏高度
        return if (delta > navigationBarHeight() + statusBarHeight()) delta else 0
    }
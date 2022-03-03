package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

private const val KEYBOARD_TAG = "keyboardTagView"


//////////////////////
/// 键盘操作
//////////////////////

inline val View.isShowKeyboard: Boolean
    get() = rootWindowInsetsCompat?.isVisible(WindowInsetsCompat.Type.ime()) == true

inline val View.keyboardShowHeight: Int
    get() = rootWindowInsetsCompat?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0

@RequiresApi(Build.VERSION_CODES.M)
fun View.showKeyboard() = windowInsetsControllerCompat?.show(WindowInsetsCompat.Type.ime())

@RequiresApi(Build.VERSION_CODES.M)
fun View.hideKeyboard() = windowInsetsControllerCompat?.hide(WindowInsetsCompat.Type.ime())

@RequiresApi(Build.VERSION_CODES.M)
fun View.toggleKeyboard() = if (isShowKeyboard) hideKeyboard() else showKeyboard()

//////////////////////
/// 旧的方法
//////////////////////

/**
 * 切换软键盘显示隐藏
 * */
fun Context.toggleSoftInput(showFlags: Int = 0, hideFlags: Int = 0) {
    inputMethodManager.toggleSoftInput(showFlags, hideFlags)
}

fun View.hideSoftInputFromWindow(flags: Int = 0) {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, flags)
}

/**
 * 是否显示软键盘
 * */
fun Activity.isSoftInputVisible(): Boolean = decorViewInvisibleHeight > 0

/**
 * 显示软键盘
 * */
fun Activity.showSoftInput() {
    if (isSoftInputVisible()) return
    toggleSoftInput()
}

/**
 * 隐藏软键盘
 * */
fun Activity.hideSoftInput() {
    if (!isSoftInputVisible()) return
    getFocusView().hideSoftInputFromWindow()
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
            child.updatePadding(
                child.left,
                child.top,
                child.right,
                paddingBottom + decorViewInvisibleHeight
            )
            oldInvisibleHeight[0] = height
        }
    }
}

private fun Activity.getFocusView(): View {
    var focusView = window.currentFocus
    if (focusView == null) {
        focusView = decorViewGroup.findViewWithTag(KEYBOARD_TAG)
        if (focusView == null) {
            focusView = EditText(window.context).apply { tag = KEYBOARD_TAG }
            decorViewGroup.addView(focusView, 0, 0)
        }
        focusView.requestFocus()
    }
    return focusView
}
package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.screenWidth(): Int = resources.displayMetrics.widthPixels

fun Context.screenHeight(): Int = resources.displayMetrics.heightPixels

fun Context.statusBarHeight(): Int {
    val resId = ResourcesUtils.getInternalDimen("status_bar_height")
    return if (resId > 0) {
        resources.getDimensionPixelSize(resId)
    } else {
        0
    }
}

fun Context.navigationBarHeight(): Int {
    val resId = ResourcesUtils.getInternalDimen("navigation_bar_height")
    return if (hasNavigationBar() && resId > 0) {
        resources.getDimensionPixelSize(resId)
    } else {
        0
    }
}

fun Context.hasNavigationBar(): Boolean {
    var result = false
    val resId = ResourcesUtils.getInternalBoolean("config_showNavigationBar")
    if(resId > 0){
        result = resources.getBoolean(resId)
    }
    try {
        //判断是否修改过底边栏
        val navBarOverride = "android.os.SystemProperties".toClass().invokeMethod(
            "get", arrayOf(String::class.java), arrayOf("qemu.hw.mainkeys")
        ) as String
        when (navBarOverride) {
            "0" -> result = true
            "1" -> result = false
            else -> {}
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun Context.showToast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

fun Context.toggleKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}

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
package com.gjn.easyapp.easyutils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.screenWidth() = resources.displayMetrics.widthPixels

fun Context.screenHeight() = resources.displayMetrics.heightPixels



fun Context.hasNavigationBar(): Boolean {
    var result = false
    val resId = ResourcesUtils.getInternalBoolean("config_showNavigationBar")
    if (resId > 0) result = resources.getBoolean(resId)
    try {
        //判断是否修改过底边栏
        val navBarOverride = "android.os.SystemProperties".toClass().invokeMethod(
            "get", arrayOf(String::class.java), arrayOf("qemu.hw.mainkeys")
        ) as String
        when (navBarOverride) {
            "0" -> result = true
            "1" -> result = false
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

fun Context.checkPermission(permission: String) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
    else ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle

fun Class<out Activity>.startActivity(context: Context?) {
    startActivity(context, null)
}

fun Class<out Activity>.startActivity(context: Context?, bundle: Bundle? = null) {
    Intent(context, this).apply { bundle?.let { putExtras(it) } }.startActivity(context)
}

fun Class<out Activity>.startActivity(context: Context?, map: Map<String, Any?> = mapOf()) {
    Intent(context, this).put(map).startActivity(context)
}

fun Intent.startActivity(context: Context?) {
    context?.startActivity(this)
}

fun Class<out Activity>.startActivityForResult(
    activity: Activity?,
    requestCode: Int
) {
    startActivityForResult(activity, requestCode, null)
}

fun Class<out Activity>.startActivityForResult(
    activity: Activity?,
    requestCode: Int,
    bundle: Bundle? = null
) {
    Intent(activity, this).apply { bundle?.let { putExtras(it) } }
        .startActivityForResult(activity, requestCode)
}

fun Class<out Activity>.startActivityForResult(
    activity: Activity?,
    requestCode: Int,
    map: Map<String, Any?> = mapOf()
) {
    Intent(activity, this).put(map).startActivityForResult(activity, requestCode)
}

fun Intent.startActivityForResult(activity: Activity?, requestCode: Int) {
    activity?.startActivityForResult(this, requestCode)
}

/**
 * 判断是否开启软键盘
 * */
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

object ActivityUtils {

    @JvmOverloads
    fun toNextActivity(activity: Activity, cls: Class<out Activity>, bundle: Bundle? = null) {
        showNextActivity(activity, cls, bundle)
        activity.finish()
    }

    @JvmOverloads
    fun showNextActivity(activity: Activity, cls: Class<out Activity>, bundle: Bundle? = null) {
        cls.startActivity(activity, bundle)
    }
}
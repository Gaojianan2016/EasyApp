package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.forEach
import androidx.core.widget.TextViewCompat

///////////////////////////////////
///// 基础操作
///////////////////////////////////

inline val View.viewWidth: Int
    get() =
        if (width == 0) {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            measuredWidth
        } else {
            width
        }

inline val View.viewHeight: Int
    get() =
        if (height == 0) {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            measuredHeight
        } else {
            height
        }

fun View.isVisible() = visibility == View.VISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enabled() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

inline val View.rootWindowInsetsCompat: WindowInsetsCompat?
    get() = ViewCompat.getRootWindowInsets(this)

inline val View.windowInsetsControllerCompat: WindowInsetsControllerCompat?
    get() = ViewCompat.getWindowInsetsController(this)

/**
 * 判断是否是RTL布局
 * */
fun Context.isLayoutRtl(): Boolean {
    val local =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) resources.configuration.locales[0]
        else resources.configuration.locale
    return TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL
}

/**
 * 获取子视图 按资源名称
 * */
fun ViewGroup.findChildViewByResourceName(resourceName: String): View? {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child.id != View.NO_ID) {
            if (resources.getResourceEntryName(child.id) == resourceName) {
                return child
            }
        }
    }
    return null
}

/**
 * 移除对应类的子视图
 * */
fun ViewGroup.removeChildView(cls: Class<out View>) {
    for (i in 0 until this.childCount) {
        if (getChildAt(i)::class.java.name == cls.name) {
            removeViewAt(i)
        }
    }
}

/**
 * 修复滑动view顶部 内嵌焦点问题 e.g [ListView, GridView, WebView, RecyclerView]
 * */
fun View.fixScrollViewTopping() {
    isFocusable = false
    val viewGroup = (if (this is ViewGroup) this else null) ?: return
    viewGroup.forEach { child ->
        child.isFocusable = false
        if (child is ViewGroup) child.fixScrollViewTopping()
    }
}

/**
 * 划线
 * */
fun TextView.strikeLine() {
    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

/**
 * 下划线
 */
fun TextView.underline() {
    paintFlags = Paint.UNDERLINE_TEXT_FLAG
}

/**
 * 获取修剪Text
 * */
fun TextView.trimText() = text.toString().trim()

/**
 * 获取修剪Hint
 * */
fun TextView.trimHint() = hint.toString().trim()

/**
 * 获取Text或者Hint
 * */
fun TextView.getTrimTextOrHint(): String =
    if (text.isNullOrEmpty()) {
        trimHint()
    } else {
        trimText()
    }

/**
 * 设置TextAppearance
 * */
fun TextView.setTextAppearanceResource(@StyleRes resId: Int) {
    TextViewCompat.setTextAppearance(this, resId)
}

/**
 * 设置TextColor
 * */
fun TextView.setTextColorResource(@ColorRes id: Int) {
    setTextColor(ActivityCompat.getColor(context, id))
}

/**
 * 光标移动到最后
 * */
fun EditText.moveLastSelection() {
    setSelection(text.length)
}

/**
 * 切换密码显示隐藏
 * */
fun EditText.togglePasswordVisible() {
    transformationMethod =
        if (isPasswordVisible()) HideReturnsTransformationMethod.getInstance()
        else PasswordTransformationMethod.getInstance()
    moveLastSelection()
}

/**
 * 是否显示密码
 * */
fun EditText.isPasswordVisible() =
    transformationMethod === PasswordTransformationMethod.getInstance()

/**
 * 监听输入框文本变化
 * */
fun EditText.monitorTextChange(
    beforeTextChangedBlock: ((CharSequence?, Int, Int, Int) -> Unit)? = null,
    onTextChangedBlock: ((CharSequence?, Int, Int, Int) -> Unit)? = null,
    afterTextChangedBlock: ((Editable?) -> Unit)
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChangedBlock?.invoke(text, start, count, after)
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangedBlock?.invoke(text, start, before, count)
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChangedBlock.invoke(editable)
        }
    })
}

/**
 * 监听点击在指定view内外
 * */
fun View.monitorClickInOrOutView(
    ev: MotionEvent,
    block: ((Boolean) -> Unit)? = null
) {
    if (ev.action == MotionEvent.ACTION_DOWN) {
        block?.invoke(isClickIn(ev))
    }
}

/**
 * 监听Activity中点击是否在EditText内外
 * */
fun Activity.monitorClickInOrOutEditText(
    ev: MotionEvent,
    block: ((Boolean) -> Unit)? = null
) {
    if (ev.action == MotionEvent.ACTION_DOWN) {
        val v = currentFocus
        block?.invoke(v is EditText && v.isClickIn(ev))
    }
}

/**
 * 是否点击在view内
 * */
private fun View.isClickIn(event: MotionEvent): Boolean {
    val leftTop = intArrayOf(0, 0)
    getLocationInWindow(leftTop)
    val left = leftTop[0]
    val top = leftTop[1]
    val bottom = top + height
    val right = left + width
    return event.x > left && event.x < right && event.y > top && event.y < bottom
}
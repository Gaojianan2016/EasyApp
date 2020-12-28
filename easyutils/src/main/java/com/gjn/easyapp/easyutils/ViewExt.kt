package com.gjn.easyapp.easyutils

import android.app.Activity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat

fun View.viewWidth(): Int {
    var w = width
    if (w == 0) {
        measure(0, 0)
        w = measuredWidth
    }
    return w
}

fun View.viewHeight(): Int {
    var h = height
    if (h == 0) {
        measure(0, 0)
        h = measuredHeight
    }
    return h
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.click(block: View.() -> Unit) {
    this.setOnClickListener { it.block() }
}

fun View.clickLong(block: View.() -> Boolean) {
    this.setOnLongClickListener {
        return@setOnLongClickListener it.block()
    }
}

fun ViewGroup.clearChildView(cls: Class<out View>) {
    for (i in 0 until this.childCount) {
        if (this.getChildAt(i)::class.java.name == cls.name) {
            this.removeViewAt(i)
        }
    }
}

fun TextView.trimText(): String = text.toString().trim()

fun TextView.trimHint(): String = hint.toString().trim()

fun TextView.textAppearanceCompat(@StyleRes resId: Int) {
    TextViewCompat.setTextAppearance(this, resId)
}

fun EditText.getTextOrHint(): String = if (text.isNullOrEmpty()) {
    trimHint()
} else {
    trimText()
}

fun EditText.toLastSelection() {
    setSelection(text.length)
}

fun EditText.togglePassword(): Boolean {
    val isHide: Boolean = if (transformationMethod === PasswordTransformationMethod.getInstance()) {
        transformationMethod = HideReturnsTransformationMethod.getInstance()
        true
    } else {
        transformationMethod = PasswordTransformationMethod.getInstance()
        false
    }
    toLastSelection()
    return isHide
}

fun setOnClickListeners(vararg view: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block() }
    view.forEach { it?.setOnClickListener(listener) }
}

fun setOnClickListeners(vararg view: View?, listener: View.OnClickListener) {
    view.forEach { it?.setOnClickListener(listener) }
}

fun setOnLongClickListeners(vararg view: View?, block: View.() -> Boolean) {
    val listener = View.OnLongClickListener { return@OnLongClickListener it.block() }
    view.forEach { it?.setOnLongClickListener(listener) }
}

fun setOnLongClickListeners(vararg view: View?, listener: View.OnLongClickListener) {
    view.forEach { it?.setOnLongClickListener(listener) }
}

object ViewUtils {

    fun dispatchTouchView(
        view: View,
        ev: MotionEvent,
        listener: OnTouchViewListener
    ) {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (isClickInView(view, ev)) {
                listener.onTouchIn()
            } else {
                listener.onTouchOut()
            }
        }
    }

    private fun isClickInView(v: View, event: MotionEvent): Boolean {
        val leftTop = intArrayOf(0, 0)
        v.getLocationInWindow(leftTop)
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + v.height
        val right = left + v.width
        return event.x > left && event.x < right && event.y > top && event.y < bottom
    }

    fun dispatchTouchEditText(
        activity: Activity,
        ev: MotionEvent,
        listener: OnTouchViewListener
    ) {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = activity.currentFocus
            if (isClickInEditText(v, ev)) {
                listener.onTouchIn()
            } else {
                listener.onTouchOut()
            }
        }
    }

    private fun isClickInEditText(v: View?, event: MotionEvent): Boolean {
        return if (v is EditText) {
            isClickInView(v, event)
        } else false
    }

    abstract class OnTouchViewListener {
        fun onTouchIn() {}

        fun onTouchOut() {}
    }
}
package com.gjn.easyapp.easyutils

import android.app.Activity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView

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

fun TextView.trimString(): String = text.toString().trim()

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
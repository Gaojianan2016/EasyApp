package com.gjn.easyapp.easyutils

import android.app.Activity
import android.graphics.Paint
import android.text.Editable
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

fun ViewGroup.getChildViewByResourceName(resourceEntryName: String): View?{
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child.id != View.NO_ID) {
            if (resources.getResourceEntryName(child.id) == resourceEntryName) {
                return child
            }
        }
    }
    return null
}

fun ViewGroup.clearChildView(cls: Class<out View>) {
    for (i in 0 until this.childCount) {
        if (this.getChildAt(i)::class.java.name == cls.name) {
            this.removeViewAt(i)
        }
    }
}

//画中横线
fun TextView?.strikeLine(){
    this?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.trimText(): String = text.toString().trim()

fun TextView.trimHint(): String = hint.toString().trim()

fun TextView.setTextAppearanceResource(@StyleRes resId: Int) {
    TextViewCompat.setTextAppearance(this, resId)
}

fun TextView.setTextColorResource(@ColorRes id: Int) {
    setTextColor(ActivityCompat.getColor(context, id))
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

fun EditText.monitorTextChange(
    beforeTextChangedBlock: ((CharSequence?, Int, Int, Int) -> Unit)? = null,
    onTextChangedBlock: ((CharSequence?, Int, Int, Int) -> Unit)? = null,
    afterTextChangedBlock: ((Editable?) -> Unit)
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChangedBlock?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangedBlock?.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChangedBlock.invoke(s)
        }
    })
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
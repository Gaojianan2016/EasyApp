package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

object ViewUtils {

    fun getEditTextString(editText: EditText?): String {
        return editText?.text.toString().trim()
    }

    fun getTextViewString(textView: TextView?): String {
        return textView?.text.toString().trim()
    }

    fun getViewWidth(view: View): Int {
        var w = view.width
        if (w == 0) {
            view.measure(0, 0)
            w = view.measuredWidth
        }
        return w
    }

    fun getViewHeight(view: View): Int {
        var h = view.height
        if (h == 0) {
            view.measure(0, 0)
            h = view.measuredHeight
        }
        return h
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun setSelectionLast(editText: EditText?){
        editText?.run {
            setSelection(editText.text.length)
        }
    }

    fun togglePasswordEditText(editText: EditText?){
        editText?.run {
            transformationMethod = if (transformationMethod === PasswordTransformationMethod.getInstance()) {
                HideReturnsTransformationMethod.getInstance()
            }else{
                PasswordTransformationMethod.getInstance()
            }
            setSelectionLast(editText)
        }
    }

    fun toggleKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun isKeyboardShowing(activity: Activity): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight = activity.window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        //DecorView即为activity的顶级view
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom
    }

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
        return if (v is EditText) { isClickInView(v, event) } else false
    }

    abstract class OnTouchViewListener {
        fun onTouchIn(){}

        fun onTouchOut(){}
    }
}
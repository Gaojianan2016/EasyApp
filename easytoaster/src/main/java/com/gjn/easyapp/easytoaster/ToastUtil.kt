package com.gjn.easyapp.easytoaster

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.runBlocking


object ToastUtil {

    const val EASY_TYPE_NULL = -1
    const val EASY_TYPE_CONFIRM = 0
    const val EASY_TYPE_INFO = 1
    const val EASY_TYPE_WARNING = 2
    const val EASY_TYPE_ERROR = 3
    const val EASY_TYPE_FAIL = 4
    const val EASY_TYPE_SUCCESS = 5
    const val EASY_TYPE_WAIT = 6
    const val EASY_TYPE_PROHIBIT = 7

    private var mContext: Context? = null
    private var mToast: Toast? = null
    private var mEasyToastView: View? = null
    private var isApplication = false

    fun instance(context: Context): ToastUtil {
        isApplication = false
        init(context)
        return this
    }

    fun instanceApplication(context: Context): ToastUtil {
        isApplication = true
        init(context)
        return this
    }

    private fun init(context: Context) {
        mContext = context
        if (mEasyToastView == null) {
            mEasyToastView = LayoutInflater.from(mContext)
                .inflate(R.layout.easytoast_transient_notification, null)
        }
    }

    @SuppressLint("ShowToast")
    fun showToast(
        msg: CharSequence?, view: View? = null, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.BOTTOM, xOffset: Int = 0, yOffset: Int = 0
    ) {
        if (mContext == null) {
            return
        }
        runBlocking {
            if (mToast != null) {
                mToast!!.cancel()
                mToast = null
            }
            if (isApplication) {
                mToast = Toast.makeText(mContext, null, duration)
                mToast!!.setText(msg)
            } else {
                mToast = Toast.makeText(mContext, msg, duration)
            }
            if (view != null) {
                mToast!!.view = view
                mToast!!.setText(msg)
            }
            mToast!!.setGravity(gravity, xOffset, yOffset)
            mToast?.show()
        }
    }

    fun showNullToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_NULL, duration, gravity, xOffset, yOffset)
    }

    fun showConfirmToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_CONFIRM, duration, gravity, xOffset, yOffset)
    }

    fun showInfoToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_INFO, duration, gravity, xOffset, yOffset)
    }

    fun showWarningToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_WARNING, duration, gravity, xOffset, yOffset)
    }

    fun showErrorToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_ERROR, duration, gravity, xOffset, yOffset)
    }

    fun showFailToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_FAIL, duration, gravity, xOffset, yOffset)
    }

    fun showSuccessToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_SUCCESS, duration, gravity, xOffset, yOffset)
    }

    fun showWaitToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_WAIT, duration, gravity, xOffset, yOffset)
    }

    fun showProhibitToast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        showEasyToast(msg, EASY_TYPE_PROHIBIT, duration, gravity, xOffset, yOffset)
    }

    private fun showEasyToast(msg: CharSequence?, type: Int = EASY_TYPE_NULL, duration: Int = Toast.LENGTH_SHORT,
                      gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0){
        if (mEasyToastView == null) {
            return
        }
        val iv: ImageView = mEasyToastView!!.findViewById(R.id.iv_easy_toast_transient_notification)
        iv.visibility = View.VISIBLE
        when (type) {
            EASY_TYPE_CONFIRM -> iv.setImageResource(R.drawable.easy_toast_confirm)
            EASY_TYPE_INFO -> iv.setImageResource(R.drawable.easy_toast_info)
            EASY_TYPE_WARNING -> iv.setImageResource(R.drawable.easy_toast_warning)
            EASY_TYPE_ERROR -> iv.setImageResource(R.drawable.easy_toast_error)
            EASY_TYPE_FAIL -> iv.setImageResource(R.drawable.easy_toast_fail)
            EASY_TYPE_SUCCESS -> iv.setImageResource(R.drawable.easy_toast_success)
            EASY_TYPE_WAIT -> iv.setImageResource(R.drawable.easy_toast_wait)
            EASY_TYPE_PROHIBIT -> iv.setImageResource(R.drawable.easy_toast_prohibit)
            EASY_TYPE_NULL -> iv.visibility = View.GONE
            else -> iv.visibility = View.GONE
        }
        showToast(msg, mEasyToastView, duration, gravity, xOffset, yOffset)
    }
}
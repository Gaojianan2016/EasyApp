package com.gjn.easyapp.easytoaster

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.gjn.easyapp.easyutils.inflate
import kotlinx.coroutines.runBlocking

class ToastUtil(private val mContext: Context) {

    private var mToast: Toast? = null
    var mEasyToastView = mContext.inflate(R.layout.easytoast_transient_notification)

    @SuppressLint("ShowToast")
    fun showToast(
        msg: CharSequence?,
        duration: Int,
        view: View?,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        runBlocking {
            if (mToast != null) {
                mToast!!.cancel()
                mToast = null
            }
            mToast = if (isShowAppName) {
                Toast.makeText(mContext, msg, duration)
            } else {
                Toast.makeText(mContext, "", duration).apply { setText(msg) }
            }
            mToast!!.apply {
                if (view != null) {
                    this.view = view
                    setText(msg)
                }
                setGravity(gravity, xOffset, yOffset)
            }.show()
        }
    }

    fun showNullToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_NULL, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showConfirmToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_CONFIRM, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showInfoToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_INFO, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showWarningToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_WARNING, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showErrorToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_ERROR, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showFailToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_FAIL, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showSuccessToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_SUCCESS, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showWaitToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_WAIT, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    fun showProhibitToast(
        msg: CharSequence?,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        showEasyToast(msg, EASY_TYPE_PROHIBIT, duration, gravity, xOffset, yOffset, isShowAppName)
    }

    private fun showEasyToast(
        msg: CharSequence?,
        type: Int,
        duration: Int,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        isShowAppName: Boolean
    ) {
        if (mEasyToastView == null) return
        val icon: ImageView = mEasyToastView!!.findViewById(R.id.iv_easy_toast_icon)
        icon.visibility = View.VISIBLE
        when (type) {
            EASY_TYPE_CONFIRM -> icon.setImageResource(R.drawable.easy_toast_confirm)
            EASY_TYPE_INFO -> icon.setImageResource(R.drawable.easy_toast_info)
            EASY_TYPE_WARNING -> icon.setImageResource(R.drawable.easy_toast_warning)
            EASY_TYPE_ERROR -> icon.setImageResource(R.drawable.easy_toast_error)
            EASY_TYPE_FAIL -> icon.setImageResource(R.drawable.easy_toast_fail)
            EASY_TYPE_SUCCESS -> icon.setImageResource(R.drawable.easy_toast_success)
            EASY_TYPE_WAIT -> icon.setImageResource(R.drawable.easy_toast_wait)
            EASY_TYPE_PROHIBIT -> icon.setImageResource(R.drawable.easy_toast_prohibit)
            else -> icon.visibility = View.GONE
        }
        showToast(msg, duration, mEasyToastView, gravity, xOffset, yOffset, isShowAppName)
    }

    companion object {
        private const val EASY_TYPE_NULL = -1
        private const val EASY_TYPE_CONFIRM = 0
        private const val EASY_TYPE_INFO = 1
        private const val EASY_TYPE_WARNING = 2
        private const val EASY_TYPE_ERROR = 3
        private const val EASY_TYPE_FAIL = 4
        private const val EASY_TYPE_SUCCESS = 5
        private const val EASY_TYPE_WAIT = 6
        private const val EASY_TYPE_PROHIBIT = 7

        @SuppressLint("StaticFieldLeak")
        private var toastUtils: ToastUtil? = null

        fun init(context: Context): ToastUtil {
            if (toastUtils == null) {
                synchronized(ToastUtil::class.java) {
                    if (toastUtils == null) {
                        toastUtils = ToastUtil(context)
                    }
                }
            }
            return toastUtils!!
        }

        fun showToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            view: View? = null,
            gravity: Int = Gravity.BOTTOM,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showToast(msg, duration, view, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showNullToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showNullToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showConfirmToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showConfirmToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showInfoToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showInfoToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showWarningToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showWarningToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showErrorToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showErrorToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showFailToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showFailToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showSuccessToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showSuccessToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showWaitToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showWaitToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }

        fun showProhibitToast(
            msg: CharSequence?,
            duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            xOffset: Int = 0,
            yOffset: Int = 0,
            isShowAppName: Boolean = true
        ) {
            toastUtils?.showProhibitToast(msg, duration, gravity, xOffset, yOffset, isShowAppName)
        }
    }
}
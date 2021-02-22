package com.gjn.easyapp.easytoaster

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.gjn.easyapp.easyutils.ResourcesUtils
import kotlinx.coroutines.runBlocking

class ToastUtil(
    private val mContext: Context,
    var isApplication: Boolean = false
) {

    private var mToast: Toast? = null
    var mEasyToastView: View? =
        ResourcesUtils.inflate(mContext, R.layout.easytoast_transient_notification, null)

    @SuppressLint("ShowToast")
    fun showToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT, view: View? = null,
        gravity: Int = Gravity.BOTTOM, xOffset: Int = 0, yOffset: Int = 0
    ) {
        runBlocking {
            if (mToast != null) {
                mToast!!.cancel()
                mToast = null
            }
            if (isApplication) {
                mToast = Toast.makeText(mContext, "", duration)
                mToast!!.setText(msg)
            } else {
                mToast = Toast.makeText(mContext, msg, duration)
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
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(msg, EASY_TYPE_NULL, duration, gravity, xOffset, yOffset)
    }

    fun showConfirmToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(
            msg, EASY_TYPE_CONFIRM, duration, gravity, xOffset, yOffset
        )
    }

    fun showInfoToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(msg, EASY_TYPE_INFO, duration, gravity, xOffset, yOffset)
    }

    fun showWarningToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(
            msg, EASY_TYPE_WARNING, duration, gravity, xOffset, yOffset
        )
    }

    fun showErrorToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(msg, EASY_TYPE_ERROR, duration, gravity, xOffset, yOffset)
    }

    fun showFailToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(msg, EASY_TYPE_FAIL, duration, gravity, xOffset, yOffset)
    }

    fun showSuccessToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(
            msg, EASY_TYPE_SUCCESS, duration, gravity, xOffset, yOffset
        )
    }

    fun showWaitToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(msg, EASY_TYPE_WAIT, duration, gravity, xOffset, yOffset)
    }

    fun showProhibitToast(
        msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        showEasyToast(
            msg, EASY_TYPE_PROHIBIT, duration, gravity, xOffset, yOffset
        )
    }

    private fun showEasyToast(
        msg: CharSequence?, type: Int = EASY_TYPE_NULL, duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
    ) {
        if (mEasyToastView == null) return
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
            else -> iv.visibility = View.GONE
        }
        showToast(msg, duration, mEasyToastView, gravity, xOffset, yOffset)
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

        private var toastUtils: ToastUtil? = null

        fun getInstant(context: Context, isApplication: Boolean = false): ToastUtil {
            if (toastUtils == null) {
                synchronized(ToastUtil::class.java) {
                    if (toastUtils == null) {
                        toastUtils = ToastUtil(context, isApplication)
                    }
                }
            }
            return toastUtils!!
        }

        fun showToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT, view: View? = null,
            gravity: Int = Gravity.BOTTOM, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showToast(msg, duration, view, gravity, xOffset, yOffset)
        }

        fun showNullToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showNullToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showConfirmToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showConfirmToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showInfoToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showInfoToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showWarningToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showWarningToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showErrorToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showErrorToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showFailToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showFailToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showSuccessToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showSuccessToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showWaitToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showWaitToast(msg, duration, gravity, xOffset, yOffset)
        }

        fun showProhibitToast(
            msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffset: Int = 0
        ) {
            toastUtils?.showProhibitToast(msg, duration, gravity, xOffset, yOffset)
        }
    }
}
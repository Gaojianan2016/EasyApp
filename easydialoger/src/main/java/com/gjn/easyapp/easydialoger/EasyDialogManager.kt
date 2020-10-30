package com.gjn.easyapp.easydialoger

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easydialoger.base.ConvertLayoutDialogFragment
import com.gjn.easyapp.easydialoger.base.OnDialogCancelListener
import com.gjn.easyapp.easydialoger.base.ViewHolder
import com.gjn.easyapp.easyutils.dp
import com.gjn.easyapp.easyutils.widthPixels
import java.util.*

class EasyDialogManager {

    private val fragmentStack: Stack<BaseDialogFragment> = Stack()
    private var mFragmentManager: FragmentManager? = null
    private var mActivity: Activity? = null

    constructor(activity: FragmentActivity){
        mFragmentManager = activity.supportFragmentManager
        mActivity = activity
    }

    constructor(fragment: Fragment){
        mFragmentManager = fragment.childFragmentManager
        mActivity = fragment.activity
    }

    fun addOnDialogCancelListener(listener: OnDialogCancelListener) {
        for (fragment in fragmentStack) {
            fragment?.addOnDialogCancelListener(listener)
        }
    }

    fun showDialog(dialogFragment: BaseDialogFragment?): BaseDialogFragment? {
        if (dialogFragment == null) {
            return null
        }
        dismissDialog(dialogFragment)
        show(dialogFragment)
        return dialogFragment
    }

    fun dismissDialog(dialogFragment: BaseDialogFragment?) {
        if (dialogFragment == null) {
            return
        }
        if (fragmentStack.contains(dialogFragment)) {
            dismiss(dialogFragment)
        }
    }

    fun clearDialogs() {
        for (fragment in fragmentStack) {
            dismissDialog(fragment)
        }
        fragmentStack.clear()
    }

    @JvmOverloads
    fun showAndroidDialog(
        title: CharSequence,
        msg: CharSequence,
        positive: CharSequence? = null,
        positiveClickListener: DialogInterface.OnClickListener? = null,
        negative: CharSequence? = null,
        negativeClickListener: DialogInterface.OnClickListener? = null,
        neutral: CharSequence? = null,
        neutralClickListener: DialogInterface.OnClickListener? = null
    ): BaseDialogFragment? {
        if (mActivity == null) {
            return null
        }
        val builder = AlertDialog.Builder(mActivity!!)
        builder.run {
            setTitle(title)
            setMessage(msg)
            if (negative != null) {
                setPositiveButton(positive, positiveClickListener)
            }
            if (negative != null) {
                setNegativeButton(positive, positiveClickListener)
            }
            if (neutral != null) {
                setNeutralButton(neutral, positiveClickListener)
            }
        }
        val dialogFragment = EasyDialogFragment.newInstance(builder)
        return showDialog(dialogFragment)
    }

    @JvmOverloads
    fun showEasyNormalDialog(
        msg: CharSequence,
        dimAmount: Float = BaseDialogFragment.DIM_AMOUNT,
        positive: CharSequence,
        positiveClickListener: View.OnClickListener? = null,
        negative: CharSequence? = null,
        negativeClickListener: View.OnClickListener? = null
    ): BaseDialogFragment? {
        if (mActivity == null) {
            return null
        }
        val dialogFragment = EasyDialogFragment.newInstance(R.layout.edf_dialog_normal,
            object : ConvertLayoutDialogFragment {
                override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
                    if (negative == null) {
                        holder.findViewById<View>(R.id.edf_v_line_edb)?.visibility = View.GONE
                        holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.visibility = View.GONE
                    } else {
                        holder.findViewById<View>(R.id.edf_v_line_edb)?.visibility = View.VISIBLE
                        holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.visibility = View.VISIBLE
                    }
                    holder.findViewById<TextView>(R.id.edf_tv_msg_edn)?.text = msg
                    holder.findViewById<TextView>(R.id.edf_tv_yes_edb)?.run {
                        text = positive
                        setOnClickListener {
                            positiveClickListener?.onClick(it)
                            dismiss(dialogFragment as BaseDialogFragment)
                        }
                    }
                    holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.run {
                        text = negative
                        setOnClickListener {
                            negativeClickListener?.onClick(it)
                            dismiss(dialogFragment as BaseDialogFragment)
                        }
                    }
                }
            }
        )
        dialogFragment.run {
            this.dimAmount = dimAmount
            isTransparent = true
        }
        return showDialog(dialogFragment)
    }

    @JvmOverloads
    fun showEasyDialog(
        msg: CharSequence,
        dimAmount: Float = BaseDialogFragment.DIM_AMOUNT,
        maxSize: Int,
        positive: CharSequence,
        positiveClickListener: EasyDialogFragment.EasyInputListener? = null,
        negative: CharSequence? = null,
        negativeClickListener: View.OnClickListener? = null
    ): BaseDialogFragment? {
        if (mActivity == null) {
            return null
        }
        val dialogFragment = EasyDialogFragment.newInstance(R.layout.edf_dialog_input,
            object : ConvertLayoutDialogFragment {
                override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
                    if (negative == null) {
                        holder.findViewById<View>(R.id.edf_v_line_edb)?.visibility = View.GONE
                        holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.visibility = View.GONE
                    } else {
                        holder.findViewById<View>(R.id.edf_v_line_edb)?.visibility = View.VISIBLE
                        holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.visibility = View.VISIBLE
                    }
                    holder.findViewById<TextView>(R.id.edf_tv_msg_edi)?.text = msg
                    val et = holder.findViewById<EditText>(R.id.edf_et_content_edi)
                    val size = holder.findViewById<TextView>(R.id.edf_tv_size_edi)
                    size?.run {
                        text = "0/$maxSize"
                        setTextColor(Color.BLACK)
                    }
                    et?.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            if (s != null && s.length >= maxSize) {
                                size?.run {
                                    text = "${s.length}/$maxSize"
                                    setTextColor(Color.RED)
                                }
                            } else {
                                size?.run {
                                    text = "0/$maxSize"
                                    setTextColor(Color.BLACK)
                                }
                            }
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }
                    })
                    holder.findViewById<TextView>(R.id.edf_tv_yes_edb)?.run {
                        text = positive
                        setOnClickListener {
                            positiveClickListener?.confirm(et!!.text, maxSize)
                            dismiss(dialogFragment as BaseDialogFragment)
                        }
                    }
                    holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.run {
                        text = negative
                        setOnClickListener {
                            negativeClickListener?.onClick(it)
                            dismiss(dialogFragment as BaseDialogFragment)
                        }
                    }
                }
            }
        )
        dialogFragment.run {
            this.dimAmount = dimAmount
            isTransparent = true
        }
        return showDialog(dialogFragment)
    }

    fun showSmallLoadingDialog(): BaseDialogFragment? = showEasyLoadingDialog(LOADING_S)

    fun showNormalLoadingDialog(): BaseDialogFragment? = showEasyLoadingDialog(LOADING_N)

    fun showBigLoadingDialog(): BaseDialogFragment? = showEasyLoadingDialog(LOADING_B)

    @JvmOverloads
    fun showEasyLoadingDialog(
        size: Int,
        dimAmount: Float = BaseDialogFragment.DIM_AMOUNT
    ): BaseDialogFragment? {
        if (mActivity == null) {
            return null
        }
        val edge = when (size) {
            LOADING_B -> mActivity!!.widthPixels() / LOADING_B
            LOADING_N -> mActivity!!.widthPixels() / LOADING_N
            else -> mActivity!!.widthPixels() / LOADING_S
        }
        val dialogFragment = EasyDialogFragment.newInstance(R.layout.edf_dialog_loading,
            object : ConvertLayoutDialogFragment {
                override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
                    val progressBar = holder.findViewById<ProgressBar>(R.id.edf_pb_loading_edl)
                    val padding = when (size) {
                        LOADING_B -> 5.dp(mActivity!!).toInt()
                        LOADING_N -> 20.dp(mActivity!!).toInt()
                        else -> 35.dp(mActivity!!).toInt()
                    }
                    progressBar?.setPadding(padding, padding, padding, padding)
                }
            }
        )
        dialogFragment.run {
            this.dimAmount = dimAmount
            width = edge
            height = edge
            isCloseOnTouchOutside = false
            isTransparent = true
        }
        return showDialog(dialogFragment)
    }

    private fun show(dialogFragment: BaseDialogFragment) {
        if (mFragmentManager == null) {
            return
        }
        log("show $dialogFragment")
        fragmentStack.push(dialogFragment)
        dialogFragment.addOnDialogCancelListener(object : OnDialogCancelListener {
            override fun onCancel(dialog: DialogInterface, dialogFragment: DialogFragment) {
                fragmentStack.remove(dialogFragment)
            }
        })
        dialogFragment.show(mFragmentManager!!, dialogFragment.tag)
    }

    private fun dismiss(dialogFragment: BaseDialogFragment) {
        if (mFragmentManager == null) {
            return
        }
        log("dismiss $dialogFragment")
        fragmentStack.remove(dialogFragment)
        dialogFragment.clearOnDialogCancelListeners()
        dialogFragment.dismissAllowingStateLoss()
    }

    private fun log(msg: String) {
        Log.i(TAG, msg)
    }

    companion object {
        const val LOADING_S = 3
        const val LOADING_N = 5
        const val LOADING_B = 7
        private const val TAG = "EasyDialogUtils"
    }
}
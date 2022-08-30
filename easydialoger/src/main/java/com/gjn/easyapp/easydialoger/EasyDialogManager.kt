package com.gjn.easyapp.easydialoger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easydialoger.base.ConvertLayoutDialogFragment
import com.gjn.easyapp.easydialoger.base.OnDialogCancelListener
import com.gjn.easyapp.easydialoger.base.ViewHolder
import com.gjn.easyapp.easyutils.*
import java.util.concurrent.CopyOnWriteArrayList

class EasyDialogManager {

    private val fragmentList: CopyOnWriteArrayList<BaseDialogFragment> = CopyOnWriteArrayList()
    private var mFragmentManager: FragmentManager? = null
    private var mActivity: Activity? = null

    constructor(activity: FragmentActivity) {
        mFragmentManager = activity.supportFragmentManager
        mActivity = activity
    }

    constructor(fragment: Fragment) {
        mFragmentManager = fragment.childFragmentManager
        mActivity = fragment.activity
    }

    fun addOnDialogCancelListener(listener: OnDialogCancelListener) {
        for (fragment in fragmentList) {
            fragment?.addOnDialogCancelListener(listener)
        }
    }

    fun showDialog(dialogFragment: BaseDialogFragment?): BaseDialogFragment? {
        if (dialogFragment == null) return dialogFragment
        dismissDialog(dialogFragment)
        show(dialogFragment)
        return dialogFragment
    }

    fun dismissDialog(dialogFragment: BaseDialogFragment?) {
        if (dialogFragment == null) return
        if (fragmentList.contains(dialogFragment)) {
            dismiss(dialogFragment)
        }
    }

    fun clearDialogs() {
        for (fragment in fragmentList) {
            dismissDialog(fragment)
        }
        fragmentList.clear()
    }

    fun showAndroidDialog(
        title: CharSequence,
        msg: CharSequence,
        positive: CharSequence? = null,
        positiveClickBlock: ((DialogInterface, Int) -> Unit)? = null,
        negative: CharSequence? = null,
        negativeClickBlock: ((DialogInterface, Int) -> Unit)? = null,
        neutral: CharSequence? = null,
        neutralClickBlock: ((DialogInterface, Int) -> Unit)? = null,
    ): BaseDialogFragment? {
        mActivity?.let {
            val builder = AlertDialog.Builder(it)
            builder.run {
                setTitle(title)
                setMessage(msg)
                if (positive != null) {
                    setPositiveButton(positive) { dialog, which ->
                        positiveClickBlock?.invoke(dialog, which)
                    }
                }
                if (negative != null) {
                    setNegativeButton(negative) { dialog, which ->
                        negativeClickBlock?.invoke(dialog, which)
                    }
                }
                if (neutral != null) {
                    setNeutralButton(neutral) { dialog, which ->
                        neutralClickBlock?.invoke(dialog, which)
                    }
                }
            }
            val dialogFragment = EasyDialogFragment.newInstance(builder)
            return showDialog(dialogFragment)
        }
        return null
    }

    fun showEasyNormalDialog(
        msg: CharSequence,
        dimAmount: Float = BaseDialogFragment.DIM_AMOUNT,
        positive: CharSequence,
        positiveClickBlock: (View.() -> Unit)? = null,
        negative: CharSequence? = null,
        negativeClickBlock: (View.() -> Unit)? = null
    ): BaseDialogFragment? {
        mActivity?.let {
            val dialogFragment = EasyDialogFragment.newInstance(R.layout.edf_dialog_normal,
                object : ConvertLayoutDialogFragment {
                    override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
                        if (negative == null) {
                            holder.findViewById<View>(R.id.edf_v_line_edb)?.gone()
                            holder.findViewById<View>(R.id.edf_tv_no_edb)?.gone()
                        } else {
                            holder.findViewById<View>(R.id.edf_v_line_edb)?.visible()
                            holder.findViewById<View>(R.id.edf_tv_no_edb)?.visible()
                        }
                        holder.findViewById<TextView>(R.id.edf_tv_msg_edn)?.text = msg
                        holder.findViewById<TextView>(R.id.edf_tv_yes_edb)?.run {
                            text = positive
                            click {
                                positiveClickBlock?.invoke(this)
                                dismiss(dialogFragment as BaseDialogFragment)
                            }
                        }
                        holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.run {
                            text = negative
                            click {
                                negativeClickBlock?.invoke(this)
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
        return null
    }

    @SuppressLint("SetTextI18n")
    fun showEasyDialog(
        msg: CharSequence,
        dimAmount: Float = BaseDialogFragment.DIM_AMOUNT,
        maxSize: Int,
        positive: CharSequence,
        positiveClickBlock: ((String, Int) -> Unit)? = null,
        negative: CharSequence? = null,
        negativeClickBlock: (View.() -> Unit)? = null
    ): BaseDialogFragment? {
        mActivity?.let {
            val dialogFragment = EasyDialogFragment.newInstance(R.layout.edf_dialog_input,
                object : ConvertLayoutDialogFragment {
                    override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
                        if (negative == null) {
                            holder.findViewById<View>(R.id.edf_v_line_edb)?.gone()
                            holder.findViewById<View>(R.id.edf_tv_no_edb)?.gone()
                        } else {
                            holder.findViewById<View>(R.id.edf_v_line_edb)?.visible()
                            holder.findViewById<View>(R.id.edf_tv_no_edb)?.visible()
                        }
                        holder.findViewById<TextView>(R.id.edf_tv_msg_edi)?.text = msg
                        val et = holder.findViewById<EditText>(R.id.edf_et_content_edi)
                        val size = holder.findViewById<TextView>(R.id.edf_tv_size_edi)
                        if (et == null || size == null) return

                        fun changeSize(curSize: Int, color: Int) {
                            size.text = "$curSize/$maxSize"
                            size.setTextColor(color)
                        }

                        changeSize(0, Color.BLACK)
                        et.addTextChangedListener {
                            if (it == null) {
                                changeSize(0, Color.BLACK)
                            } else {
                                if (it.length >= maxSize) {
                                    changeSize(it.length, Color.RED)
                                } else {
                                    changeSize(it.length, Color.BLACK)
                                }
                            }
                        }
                        holder.findViewById<TextView>(R.id.edf_tv_yes_edb)?.run {
                            text = positive
                            click {
                                positiveClickBlock?.invoke(et.trimText(), maxSize)
                                dismiss(dialogFragment as BaseDialogFragment)
                            }
                        }
                        holder.findViewById<TextView>(R.id.edf_tv_no_edb)?.run {
                            text = negative
                            click {
                                negativeClickBlock?.invoke(this)
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
        return null
    }

    fun showSmallLoadingDialog(): BaseDialogFragment? = showEasyLoadingDialog(LOADING_S)

    fun showNormalLoadingDialog(): BaseDialogFragment? = showEasyLoadingDialog(LOADING_N)

    fun showBigLoadingDialog(): BaseDialogFragment? = showEasyLoadingDialog(LOADING_B)

    fun showEasyLoadingDialog(
        size: Int,
        dimAmount: Float = BaseDialogFragment.DIM_AMOUNT
    ): BaseDialogFragment? {
        mActivity?.let {
            val edge = when (size) {
                LOADING_B -> it.screenWidth / LOADING_B
                LOADING_N -> it.screenWidth / LOADING_N
                else -> it.screenWidth / LOADING_S
            }
            val dialogFragment = EasyDialogFragment.newInstance(R.layout.edf_dialog_loading,
                object : ConvertLayoutDialogFragment {
                    override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
                        val progressBar = holder.findViewById<ProgressBar>(R.id.edf_pb_loading_edl)
                        val padding = when (size) {
                            LOADING_B -> 5.dp.toInt()
                            LOADING_N -> 20.dp.toInt()
                            else -> 35.dp.toInt()
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
        return null
    }

    private fun show(dialogFragment: BaseDialogFragment) {
        mFragmentManager?.run {
            log("show $dialogFragment")
            fragmentList.add(dialogFragment)
            dialogFragment.addOnDialogCancelListener(object : OnDialogCancelListener {
                override fun onCancel(dialog: DialogInterface, dialogFragment: DialogFragment) {
                    log("cancel dismiss $dialogFragment")
                    fragmentList.remove(dialogFragment)
                }
            })
            dialogFragment.show(this, dialogFragment.tag)
        }
    }

    private fun dismiss(dialogFragment: BaseDialogFragment) {
        log("dismiss $dialogFragment")
        fragmentList.remove(dialogFragment)
        dialogFragment.clearOnDialogCancelListeners()
        dialogFragment.dismissAllowingStateLoss()
    }

    private fun log(msg: String) {
        logI(msg, TAG)
    }

    companion object {
        const val LOADING_S = 3
        const val LOADING_N = 5
        const val LOADING_B = 7
        private const val TAG = "EasyDialogUtils"
    }
}
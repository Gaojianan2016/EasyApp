package com.gjn.easyapp.easydialoger.base

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.gjn.easyapp.easyutils.logE
import com.gjn.easyapp.easyutils.setDeclaredField

abstract class BaseDialogFragment : DialogFragment(), ConvertLayoutDialogFragment,
    ConvertDataBindingDialogFragment {

    var isCloseOnTouchOutside: Boolean = true
    var isCanClose: Boolean = true
    var isShowAnimations: Boolean = false
    var isTransparent: Boolean = false
    var windowAnimations: Int = View.NO_ID
    var dimAmount: Float = DIM_AMOUNT
    var width: Int = WRAP_CONTENT
    var height: Int = WRAP_CONTENT
    var gravity: Int = Gravity.CENTER
    var isMatchWidth: Boolean = false
    var isMatchHeight: Boolean = false

    private var onDialogCancelListeners: MutableList<OnDialogCancelListener>? = null
    private var onDialogDismissListeners: MutableList<OnDialogDismissListener>? = null

    abstract fun createDialogBuilder(): AlertDialog.Builder?

    abstract fun layoutResId(): Int

    abstract fun dataBindingResId(): Int

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        if (createDialogBuilder() != null) createDialogBuilder()!!.create() else super.onCreateDialog(savedInstanceState)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (createDialogBuilder() == null) {
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            isTransparent = true
            if (layoutResId() != View.NO_ID) {
                val holder = ViewHolder.create(context, layoutResId(), container)
                convertView(holder, this)
                return holder.view
            }
            if (dataBindingResId() != View.NO_ID) {
                val holder = DataBindingHolder.create(context, dataBindingResId(), container)
                convertDataBinding(holder, this)
                return holder.dataBinding.root
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.run {
            setCanceledOnTouchOutside(isCloseOnTouchOutside)
            setCancelable(isCanClose)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            dialog?.window?.let { window ->
                if (isTransparent) {
                    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                val params = window.attributes
                if (dimAmount != DIM_AMOUNT) {
                    params.dimAmount = dimAmount
                }
                if (windowAnimations != View.NO_ID && isShowAnimations) {
                    params.windowAnimations = windowAnimations
                }
                params.width = if (isMatchWidth) MATCH_PARENT else width
                params.height = if (isMatchHeight) MATCH_PARENT else height
                params.gravity = gravity
                window.attributes = params
            }
        } catch (e: Exception) {
            logE("${e.message}", TAG, e)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val clz = Class.forName("androidx.fragment.app.DialogFragment")
            this.setDeclaredField(clz, "mDismissed", false)
            this.setDeclaredField(clz, "mShownByMe", true)
            manager.beginTransaction().apply {
                add(this@BaseDialogFragment, tag)
            }.commitAllowingStateLoss()
            //super.show(manager, tag)
        } catch (e: Exception) {
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismissListeners?.forEach { it.onDismiss(dialog, this) }
    }

    override fun onCancel(dialog: DialogInterface) {
        onDialogCancelListeners?.forEach { it.onCancel(dialog, this) }
    }

    fun addOnDialogCancelListener(listener: OnDialogCancelListener) {
        if (onDialogCancelListeners == null) {
            onDialogCancelListeners = mutableListOf()
        }
        onDialogCancelListeners?.add(listener)
    }

    fun removeOnDialogCancelListener(listener: OnDialogCancelListener) {
        onDialogCancelListeners?.remove(listener)
    }

    fun clearOnDialogCancelListeners() {
        onDialogCancelListeners?.clear()
    }

    fun addOnDialogDismissListener(listener: OnDialogDismissListener) {
        if (onDialogDismissListeners == null) {
            onDialogDismissListeners = mutableListOf()
        }
        onDialogDismissListeners?.add(listener)
    }

    fun removeOnDialogDismissListener(listener: OnDialogDismissListener) {
        onDialogDismissListeners?.remove(listener)
    }

    fun clearOnDialogDismissListeners() {
        onDialogDismissListeners?.clear()
    }

    companion object {
        private const val TAG = "BaseDialogFragment"
        private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
        private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
        const val DIM_AMOUNT = 0.6f
    }
}


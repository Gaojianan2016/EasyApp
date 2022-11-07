package com.gjn.easyapp.easydialoger.base

import android.content.DialogInterface
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.gjn.easyapp.easydialoger.EasyDialogFragment

interface ConvertLayoutDialogFragment {
    fun convertView(holder: ViewHolder, dialogFragment: DialogFragment)
}

interface ConvertDataBindingDialogFragment {
    fun convertDataBinding(holder: DataBindingHolder, dialogFragment: DialogFragment)
}

interface OnDialogCancelListener {
    fun onCancel(dialog: DialogInterface, dialogFragment: DialogFragment)
}

interface OnDialogDismissListener {
    fun onDismiss(dialog: DialogInterface, dialogFragment: DialogFragment)
}

fun simpleLayoutDialog(
    @LayoutRes resId: Int,
    block: (View?, DialogFragment) -> Unit
): BaseDialogFragment =
    EasyDialogFragment.newInstance(resId, object : ConvertLayoutDialogFragment {
        override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
            block.invoke(holder.view, dialogFragment)
        }
    })

@Suppress("UNCHECKED_CAST")
fun <T> simpleDataBindingDialog(
    @LayoutRes resId: Int,
    block: (T, DialogFragment) -> Unit
): BaseDialogFragment =
    EasyDialogFragment.newInstance(resId, object : ConvertDataBindingDialogFragment {
        override fun convertDataBinding(holder: DataBindingHolder, dialogFragment: DialogFragment) {
            block.invoke(holder.dataBinding as T, dialogFragment)
        }
    })
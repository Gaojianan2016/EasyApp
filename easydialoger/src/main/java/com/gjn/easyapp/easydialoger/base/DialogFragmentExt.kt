package com.gjn.easyapp.easydialoger.base

import android.content.DialogInterface
import androidx.fragment.app.DialogFragment

interface ConvertLayoutDialogFragment {
    fun convertView(holder: ViewHolder, dialogFragment: DialogFragment)
}

interface ConvertDataBindingDialogFragment {
    fun convertDataBinding(holder: DataBindingHolder, dialogFragment: DialogFragment)
}

interface OnDialogCancelListener {
    fun onCancel(dialog: DialogInterface, dialogFragment: DialogFragment)
}
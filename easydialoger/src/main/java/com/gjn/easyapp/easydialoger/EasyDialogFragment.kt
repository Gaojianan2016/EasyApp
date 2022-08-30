package com.gjn.easyapp.easydialoger

import android.text.Editable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gjn.easyapp.easydialoger.base.*

class EasyDialogFragment : BaseDialogFragment() {

    private var dialogBuilder: AlertDialog.Builder? = null

    @LayoutRes
    private var layoutResId = View.NO_ID

    @LayoutRes
    private var dataBindingResId = View.NO_ID

    private var layoutConvert: ConvertLayoutDialogFragment? = null

    private var dataBindingConvert: ConvertDataBindingDialogFragment? = null

    override fun createDialogBuilder(): AlertDialog.Builder? = dialogBuilder

    override fun layoutResId(): Int = layoutResId

    override fun dataBindingResId(): Int = dataBindingResId

    override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
        layoutConvert?.convertView(holder, dialogFragment)
    }

    override fun convertDataBinding(holder: DataBindingHolder, dialogFragment: DialogFragment) {
        dataBindingConvert?.convertDataBinding(holder, dialogFragment)
    }

    companion object {
        fun newInstance(): EasyDialogFragment {
            return newInstance(null)
        }

        fun newInstance(builder: AlertDialog.Builder?): EasyDialogFragment {
            val dialogFragment = EasyDialogFragment()
            dialogFragment.dialogBuilder = builder
            return dialogFragment
        }

        fun newInstance(@LayoutRes resId: Int): EasyDialogFragment {
            return newInstance(resId, null as ConvertLayoutDialogFragment)
        }

        fun newInstance(
            @LayoutRes resId: Int,
            layoutConvert: ConvertLayoutDialogFragment?
        ): EasyDialogFragment {
            val dialogFragment = EasyDialogFragment()
            dialogFragment.layoutResId = resId
            dialogFragment.layoutConvert = layoutConvert
            return dialogFragment
        }

        fun newInstance(
            @LayoutRes resId: Int,
            dataBindingConvert: ConvertDataBindingDialogFragment?
        ): EasyDialogFragment {
            val dialogFragment = EasyDialogFragment()
            dialogFragment.dataBindingResId = resId
            dialogFragment.dataBindingConvert = dataBindingConvert
            return dialogFragment
        }
    }
}
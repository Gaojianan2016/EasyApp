package com.gjn.easyapp.easydialoger

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easydialoger.base.DataBindingHolder
import com.gjn.easyapp.easydialoger.base.ViewHolder

open class SimpleDataBindingDialogFragment<VDB : ViewDataBinding> : BaseDialogFragment() {

    protected lateinit var dataBinding: VDB

    override fun layoutResId() = View.NO_ID

    override fun createDialogBuilder(): AlertDialog.Builder? = null

    override fun convertView(holder: ViewHolder, dialogFragment: DialogFragment) {
    }

    override fun dataBindingResId() = View.NO_ID

    override fun convertDataBinding(holder: DataBindingHolder, dialogFragment: DialogFragment) {
        dataBinding = holder.dataBinding as VDB
    }

}
package com.gjn.easyapp.easybase

import com.gjn.easyapp.easydialoger.base.BaseDialogFragment

interface UIEvent {

    fun showToast(msg: String?)

    fun showEasyDialog(dialog: BaseDialogFragment)

    fun dismissEasyDialog(dialog: BaseDialogFragment)

    fun dismissAllEasyDialog()

}
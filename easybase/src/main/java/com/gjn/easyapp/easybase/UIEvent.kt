package com.gjn.easyapp.easybase

import android.app.Activity
import android.os.Bundle
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment

interface UIEvent {
    fun showToast(msg: String?)

    fun showEasyDialog(dialog: BaseDialogFragment)

    fun dismissEasyDialog(dialog: BaseDialogFragment)

    fun dismissAllEasyDialog()

}
package com.gjn.easyapp.easybase

import android.os.Bundle
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment

interface UIEvent {
    fun showToast(msg: String?)

    fun showNextActivity(cls: Class<*>, bundle: Bundle? = null)

    fun toNextActivity(cls: Class<*>, bundle: Bundle? = null)

    fun showEasyDialog(dialog: BaseDialogFragment)

    fun dismissEasyDialog(dialog: BaseDialogFragment)

    fun dismissAllEasyDialog()

}
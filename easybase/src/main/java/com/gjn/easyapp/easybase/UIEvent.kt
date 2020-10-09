package com.gjn.easyapp.easybase

import android.os.Bundle

interface UIEvent {
    fun showToast(msg: String?)

    fun showNextActivity(cls: Class<*>, bundle: Bundle? = null)

    fun toNextActivity(cls: Class<*>, bundle: Bundle? = null)

}
package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun Class<*>.startActivity(activity: Activity, bundle: Bundle? = null) {
    activity.startActivity(Intent(activity, this).apply { bundle?.let { putExtras(it) } })
}

object ActivityUtils {

    @JvmOverloads
    fun toNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null) {
        showNextActivity(activity, cls, bundle)
        activity.finish()
    }

    @JvmOverloads
    fun showNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null) {
        cls.startActivity(activity, bundle)
    }
}
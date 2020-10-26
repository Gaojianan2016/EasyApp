package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun Class<*>.start(activity: Activity, bundle: Bundle? = null) {
    activity.startActivity(Intent(activity, this).apply { if (bundle != null) putExtras(bundle) })
}

object ActivityUtils {

    @JvmOverloads
    fun toNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null) {
        showNextActivity(activity, cls, bundle)
        activity.finish()
    }

    @JvmOverloads
    fun showNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null) {
        cls.start(activity, bundle)
    }
}
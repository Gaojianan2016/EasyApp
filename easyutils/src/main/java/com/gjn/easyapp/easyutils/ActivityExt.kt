package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun Class<out Activity>.startActivity(activity: Activity, bundle: Bundle? = null) {
    activity.startActivity(Intent(activity, this).apply { bundle?.let { putExtras(it) } })
}

object ActivityUtils {

    @JvmOverloads
    fun toNextActivity(activity: Activity, cls: Class<out Activity>, bundle: Bundle? = null) {
        showNextActivity(activity, cls, bundle)
        activity.finish()
    }

    @JvmOverloads
    fun showNextActivity(activity: Activity, cls: Class<out Activity>, bundle: Bundle? = null) {
        cls.startActivity(activity, bundle)
    }
}
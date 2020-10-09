package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

object ActivityUtils {

    @JvmOverloads
    fun toNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null){
        showNextActivity(activity, cls, bundle)
        activity.finish()
    }

    @JvmOverloads
    fun showNextActivity(activity: Activity, cls: Class<*>, bundle: Bundle? = null){
        val intent = Intent(activity, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        activity.startActivity(intent)
    }
}
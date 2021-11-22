package com.gjn.easyapp.easyinitalizer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.startup.Initializer
import com.gjn.easyapp.easyutils.logI

/**
 * EasyApp程序初始化器
 * */
internal class EasyAppInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        application = context as Application
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()

    private val lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if(debug) logI("add    -> ${activity.javaClass.simpleName}", "EasyAppInitializer")
            activityStackCache.add(activity)
        }

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityResumed(activity: Activity) = Unit

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStopped(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

        override fun onActivityDestroyed(activity: Activity) {
            if(debug) logI("remove -> ${activity.javaClass.simpleName}", "EasyAppInitializer")
            activityStackCache.remove(activity)
        }
    }

    companion object {
        internal lateinit var application: Application private set

        const val debug = true
    }
}
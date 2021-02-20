package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.concurrent.CopyOnWriteArrayList

class AppManager private constructor() {

    private val activityList: CopyOnWriteArrayList<Activity> = CopyOnWriteArrayList()

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (activityList.contains(activity)) {
            finishActivity(activity)
            activityList.remove(activity)
        }
    }

    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    fun finishActivity(vararg clz: Class<out Activity>) {
        activityList.forEach { activity ->
            if (clz.contains(activity::class.java)) {
                finishActivity(activity)
            }
        }
    }

    fun clearActivity() {
        for (activity in activityList) {
            finishActivity(activity)
        }
        activityList.clear()
    }

    fun exitApp(context: Context) {
        clearActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
    }

    companion object {
        val instance: AppManager by lazy { AppManager() }
    }
}
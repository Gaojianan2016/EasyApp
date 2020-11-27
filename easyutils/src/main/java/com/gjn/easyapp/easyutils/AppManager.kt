package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*

class AppManager private constructor() {

    private val activityStack: Stack<Activity> = Stack()

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (activityStack.contains(activity)) {
            finishActivity(activity)
            activityStack.remove(activity)
        }
    }

    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    fun finishActivity(vararg clz: Class<out Activity>) {
        activityStack.forEach { activity ->
            if (clz.contains(activity::class.java)) {
                finishActivity(activity)
            }
        }
    }

    fun topActivity(): Activity = activityStack.lastElement()

    fun clearActivity() {
        for (activity in activityStack) {
            finishActivity(activity)
        }
        activityStack.clear()
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
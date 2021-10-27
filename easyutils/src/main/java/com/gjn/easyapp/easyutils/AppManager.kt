package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*
import kotlin.system.exitProcess

class AppManager private constructor() {

    private val activityStack: Stack<Activity> = Stack()

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.removeAll {
            if (it == activity) it.finishActivity()
            it == activity
        }
    }

    fun finishActivity(vararg clz: Class<out Activity>) {

        activityStack.forEach { activity ->
            if (clz.contains(activity::class.java)) {
                removeActivity(activity)
            }
        }
    }

    fun getTopActivity() = activityStack.lastElement()

    fun finishTopActivity(){
        removeActivity(getTopActivity())
    }

    fun isActivityExists(clazz: Class<out Activity>): Boolean =
        activityStack.any { it::class.java == clazz }

    fun clearActivity() {
        for (activity in activityStack) {
            activity.finishActivity()
        }
        activityStack.clear()
    }

    fun exitApp(){
        try {
            clearActivity()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun killApp(context: Context) {
        try {
            clearActivity()
            context.activityManager().killBackgroundProcesses(context.packageName)
        }catch (e: Exception){
            e.printStackTrace()
            exitProcess(0)
        }
    }

    companion object {
        val instance by lazy(AppManager::class.java) { AppManager() }
    }
}
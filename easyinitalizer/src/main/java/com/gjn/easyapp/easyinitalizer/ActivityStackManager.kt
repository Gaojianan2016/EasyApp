package com.gjn.easyapp.easyinitalizer

import android.app.Activity
import android.content.Context
import com.gjn.easyapp.easyutils.activityManager
import com.gjn.easyapp.easyutils.finishActivity
import java.util.*

//////////////////////////////////
///// ActivityStackManager
//////////////////////////////////

internal val activityStackCache = Stack<Activity>()

val activityListByStack: List<Activity> get() = activityStackCache.toList()

val topActivityByStack: Activity get() = activityStackCache.lastElement()

fun finishActivityByStack(activity: Activity) = finishActivityByStack(activity.javaClass)

fun <T : Activity> finishActivityByStack(clazz: Class<T>): Boolean =
    activityStackCache.removeAll {
        if (it.javaClass == clazz) it.finishActivity()
        it.javaClass == clazz
    }

fun isActivityExistsByStack(activity: Activity) = isActivityExistsByStack(activity.javaClass)

fun <T : Activity> isActivityExistsByStack(clazz: Class<T>): Boolean =
    activityStackCache.any { it.javaClass == clazz }

fun finishAllActivitiesByStack(): Boolean =
    activityStackCache.removeAll {
        it.finishActivity()
        true
    }

fun Context.killAppByStack() {
    finishAllActivitiesByStack()
    activityManager.killBackgroundProcesses(packageName)
}
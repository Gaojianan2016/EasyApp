package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import java.util.*

//////////////////////
///// startActivity
//////////////////////

inline fun <reified T : Activity> Context.startActivity(
    @AnimRes enterResId: Int? = null, @AnimRes exitResId: Int? = null,
    sharedElements: Array<View>? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) {
    when {
        enterResId != null && exitResId != null -> startActivity<T>(
            makeCustomAnimationBundle(enterResId, exitResId),
            pairs = pairs, block
        )
        sharedElements != null -> startActivity<T>(
            makeSceneTransitionAnimationBundle(*sharedElements),
            pairs = pairs, block
        )
        else -> startActivity<T>(null, pairs = pairs, block)
    }
}

inline fun <reified T : Activity> Context.startActivity(
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = startActivity<T>(null, pairs = pairs, block)

inline fun <reified T : Activity> Context.startActivity(
    options: Bundle? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = startActivity(intentOf<T>(*pairs).apply(block), options)

inline fun <reified T : Activity> Fragment.startActivity(
    @AnimRes enterResId: Int? = null, @AnimRes exitResId: Int? = null,
    sharedElements: Array<View>? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = requireActivity().startActivity<T>(enterResId, exitResId, sharedElements, pairs = pairs, block)

inline fun <reified T : Activity> Fragment.startActivity(
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = requireActivity().startActivity<T>(pairs = pairs, block)

inline fun <reified T : Activity> Fragment.startActivity(
    options: Bundle? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = requireActivity().startActivity<T>(options, pairs = pairs, block)

//////////////////////////////////
///// startActivityForResult
//////////////////////////////////

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    @AnimRes enterResId: Int? = null, @AnimRes exitResId: Int? = null,
    sharedElements: Array<View>? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) {
    when {
        enterResId != null && exitResId != null -> startActivityForResult<T>(
            requestCode, options = makeCustomAnimationBundle(enterResId, exitResId),
            pairs = pairs, block = block
        )
        sharedElements != null -> startActivityForResult<T>(
            requestCode, options = makeSceneTransitionAnimationBundle(*sharedElements),
            pairs = pairs, block = block
        )
        else -> startActivityForResult<T>(requestCode, options = null, pairs = pairs, block = block)
    }
}

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = startActivityForResult<T>(requestCode, options = null, pairs = pairs, block = block)

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    options: Bundle? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = startActivityForResult(intentOf<T>(*pairs).apply(block), requestCode, options)


inline fun <reified T : Activity> Fragment.startActivityForResult(
    requestCode: Int,
    @AnimRes enterResId: Int? = null, @AnimRes exitResId: Int? = null,
    sharedElements: Array<View>? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = requireActivity().startActivityForResult<T>(
    requestCode,
    enterResId, exitResId,
    sharedElements,
    pairs = pairs, block
)

inline fun <reified T : Activity> Fragment.startActivityForResult(
    requestCode: Int,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = requireActivity().startActivityForResult<T>(requestCode, null, pairs = pairs, block)

inline fun <reified T : Activity> Fragment.startActivityForResult(
    requestCode: Int,
    options: Bundle? = null,
    vararg pairs: Pair<String, *>,
    crossinline block: Intent.() -> Unit = {}
) = requireActivity().startActivityForResult<T>(requestCode, options, pairs = pairs, block)

//////////////////////////////////
///// finishActivity
//////////////////////////////////

fun Activity.finishActivity() {
    if (!isFinishing) finish()
}

fun Activity.finishWithResult(vararg pairs: Pair<String, *>) =
    finishWithResult(Activity.RESULT_OK, *pairs)

fun Activity.finishWithResult(resultCode: Int, vararg pairs: Pair<String, *>) {
    setResult(resultCode, Intent().apply { putExtras(bundleOf(*pairs)) })
    finishActivity()
}

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

fun Context.killAppByStack(){
    finishAllActivitiesByStack()
    activityManager().killBackgroundProcesses(packageName)
}

/**
 * 获取 android.R.id.content 帧布局
 * */
fun Activity.contentFrameLayout(): FrameLayout = window.findViewById(android.R.id.content)

/**
 * 获取 decorView 布局
 * */
fun Activity.decorViewGroup(): ViewGroup = window.decorView as ViewGroup
package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.abs

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
///// 其他常规操作
//////////////////////////////////

/**
 * 获取 android.R.id.content 帧布局
 * */
inline val Activity.contentFrameLayout: FrameLayout get() = window.findViewById(android.R.id.content)

/**
 * 获取 decorView 布局
 * */
inline val Activity.decorViewGroup: ViewGroup get() = window.decorView as ViewGroup

/**
 * 根布局插入兼容
 * */
inline val Activity.rootWindowInsetsCompat: WindowInsetsCompat?
    get() = window.decorView.rootWindowInsetsCompat

/**
 * 窗口插入控制兼容
 * */
inline val Activity.windowInsetsControllerCompat: WindowInsetsControllerCompat?
    get() = WindowCompat.getInsetsController(window, window.decorView)

inline fun <reified T> Activity.getIntentKey(key: String) =
    lazy<T?> { intent.extras[key] }

inline fun <reified T> Activity.getIntentKey(key: String, default: T) =
    lazy { intent.extras[key] ?: default }

/**
 * 获取 android.R.id.content 未显示高度
 * */
inline val Activity.contentViewInvisibleHeight: Int
    get() {
        val content = contentFrameLayout
        val outRect = Rect().apply { content.getWindowVisibleDisplayFrame(this) }
        val delta = abs(content.bottom - outRect.bottom)
        //差值超过通知栏+状态栏高度
        return if (delta > navigationBarHeight + statusBarHeight) delta else 0
    }

/**
 * 获取decorView未显示高度
 * */
inline val Activity.decorViewInvisibleHeight: Int
    get() {
        val decorView = decorViewGroup
        val outRect = Rect().apply { decorView.getWindowVisibleDisplayFrame(this) }
        val delta = abs(decorView.bottom - outRect.bottom)
        //差值超过通知栏+状态栏高度
        return if (delta > navigationBarHeight + statusBarHeight) delta else 0
    }
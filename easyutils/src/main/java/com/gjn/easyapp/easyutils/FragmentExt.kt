package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

//////////////////////
///// startActivity
//////////////////////

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

/**
 * 快速实例化 带参Fragment
 * */
fun Fragment.newInstance(vararg pairs: Pair<String, *>) =
    apply { arguments = bundleOf(*pairs) }

inline fun <reified T> Fragment.getArgumentsKey(key: String) =
    lazy<T?> { arguments[key] }

inline fun <reified T> Fragment.getArgumentsKey(key: String, default: T) =
    lazy { arguments[key] ?: default }
package com.gjn.easyapp.easyutils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(this, text, duration).apply { show() }

fun Context.toast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(this, text, duration).apply { show() }

fun Context.longToast(text: CharSequence?, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(this, text, duration).apply { show() }

fun Context.longToast(@StringRes text: Int, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(this, text, duration).apply { show() }

fun Fragment.toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT): Toast =
    requireActivity().toast(text, duration)

fun Fragment.toast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT): Toast =
    requireActivity().toast(text, duration)

fun Fragment.longToast(text: CharSequence?, duration: Int = Toast.LENGTH_LONG): Toast =
    requireActivity().longToast(text, duration)

fun Fragment.longToast(@StringRes text: Int, duration: Int = Toast.LENGTH_LONG): Toast =
    requireActivity().longToast(text, duration)


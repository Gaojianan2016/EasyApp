package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding

const val INFLATE = "inflate"
const val BIND = "bind"

inline fun <reified VB : ViewBinding> Activity.inflateBinding() =
    lazy { inflateViewBinding<VB>(layoutInflater).apply { setContentView(root) } }

inline fun <reified VB : ViewBinding> Dialog.inflateBinding() =
    lazy { inflateViewBinding<VB>(layoutInflater).apply { setContentView(root) } }

inline fun <reified VB : ViewBinding> bindViewBinding(view: View) =
    VB::class.java.getMethod(BIND, View::class.java).invoke(null, view) as VB

inline fun <reified VB : ViewBinding> inflateViewBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod(INFLATE, LayoutInflater::class.java).invoke(null, layoutInflater) as VB
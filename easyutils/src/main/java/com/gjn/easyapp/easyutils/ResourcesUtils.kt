package com.gjn.easyapp.easyutils

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

object ResourcesUtils {

    fun inflate(
        context: Context?,
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = false
    ): View? = LayoutInflater.from(context).inflate(resource, root, attachToRoot)

    fun inflate(
        inflater: LayoutInflater,
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = false
    ): View? = inflater.inflate(resource, root, attachToRoot)

    fun getInternalId(name: String?): Int = getInternal(name, "id")

    fun getInternalLayout(name: String?): Int = getInternal(name, "layout")

    fun getInternalDrawable(name: String?): Int = getInternal(name, "drawable")

    fun getInternalColors(name: String?): Int = getInternal(name, "colors")

    fun getInternal(name: String?, type: String?): Int {
        return Resources.getSystem().getIdentifier(name, type, "android")
    }


}
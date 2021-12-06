package com.gjn.easyapp.easyutils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.io.File

/**
 * 获取标识符
 * */
private fun Context.getIdentifier(name: String, defType: String, defPackage: String) =
    resources.getIdentifier(name, defType, defPackage)

/////////////////////////////////
////  android包内的操作
/////////////////////////////////

/**
 * 获取系统标识符
 * */
private fun Context.getSystemIdentifier(name: String, defType: String) =
    getIdentifier(name, defType, "android")

fun Context.getSystemIdIdentifier(name: String) = getSystemIdentifier(name, "id")

fun Context.getSystemLayoutIdentifier(name: String) = getSystemIdentifier(name, "layout")

fun Context.getSystemDrawableIdentifier(name: String) = getSystemIdentifier(name, "drawable")

fun Context.getSystemColorIdentifier(name: String) = getSystemIdentifier(name, "color")

fun Context.getSystemDimenIdentifier(name: String) = getSystemIdentifier(name, "dimen")

fun Context.getSystemIntIdentifier(name: String) = getSystemIdentifier(name, "integer")

fun Context.getSystemBoolIdentifier(name: String) = getSystemIdentifier(name, "bool")

/////////////////////////////////
////  app包内的操作
/////////////////////////////////

/**
 * 获取App标识符
 * */
private fun Context.getAppIdentifier(name: String, defType: String) =
    getIdentifier(name, defType, packageName)

fun Context.getAppIdIdentifier(name: String) = getAppIdentifier(name, "id")

fun Context.getAppAnimIdentifier(name: String) = getAppIdentifier(name, "anim")

fun Context.getAppMenuIdentifier(name: String) = getAppIdentifier(name, "menu")

fun Context.getAppStyleIdentifier(name: String) = getAppIdentifier(name, "style")

fun Context.getAppLayoutIdentifier(name: String) = getAppIdentifier(name, "layout")

fun Context.getAppDrawableIdentifier(name: String) = getAppIdentifier(name, "drawable")

fun Context.getAppMipmapIdentifier(name: String) = getAppIdentifier(name, "mipmap")

fun Context.getAppColorIdentifier(name: String) = getAppIdentifier(name, "color")

fun Context.getAppDimenIdentifier(name: String) = getAppIdentifier(name, "dimen")

fun Context.getAppIntIdentifier(name: String) = getAppIdentifier(name, "integer")

fun Context.getAppBoolIdentifier(name: String) = getAppIdentifier(name, "bool")

fun Context.getAppXmlIdentifier(name: String) = getAppIdentifier(name, "xml")

/////////////////////////////////
////  assets 操作
/////////////////////////////////

/**
 * 获取assets String
 * */
fun Context.assetsStr(fileName: String): String {
    if (fileName.isEmpty()) return ""
    return try {
        String(assets.open(fileName).readBytes())
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 复制assets file
 * */
fun Context.assetsCopyFile(fileName: String, target: File): Boolean {
    if (fileName.isEmpty()) return false
    if (!target.createNewFile()) return false
    return try {
        assets.open(fileName).copyTo(target.outputStream())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/////////////////////////////////
////  raw 操作
/////////////////////////////////

/**
 * 获取raw String
 * */
fun Context.rawStr(@RawRes resId: Int): String =
    try {
        String(resources.openRawResource(resId).readBytes())
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }


/**
 * 复制assets file
 * */
fun Context.rawCopyFile(@RawRes resId: Int, target: File): Boolean {
    if (!target.createNewFile()) return false
    return try {
        resources.openRawResource(resId).copyTo(target.outputStream())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/////////////////////////////////
////  inflate 操作
/////////////////////////////////

/**
 * 布局设置
 * */
fun Context?.inflate(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
): View? = LayoutInflater.from(this).inflate(resource, root, attachToRoot)

/**
 * DataBindingUtil 布局设置
 * */
fun <T : ViewDataBinding> Context?.inflateDataBindingUtil(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = false
): T = DataBindingUtil.inflate(LayoutInflater.from(this), resource, root, attachToRoot) as T


/////////////////////////////////
////  string 操作
/////////////////////////////////

/**
 * 获取字符串
 * */
fun Context.string(
    @StringRes resId: Int,
    vararg formatArgs: Any
) = try {
    getString(resId, *formatArgs)
} catch (e: Exception) {
    e.printStackTrace()
    resId.toString()
}

/**
 * 获取字符串数组
 * */
fun Context.stringArray(@ArrayRes resId: Int): Array<String> =
    try {
        resources.getStringArray(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        arrayOf(resId.toString())
    }
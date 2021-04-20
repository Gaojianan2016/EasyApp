package com.gjn.easyapp.easyutils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun Context.getAndroidIdentifier(name: String, defType: String) =
    resources.getIdentifier(name, defType, "android")

fun Context.getAndroidIdentifierId(name: String) = getAndroidIdentifier(name, "id")

fun Context.getAndroidIdentifierLayout(name: String) = getAndroidIdentifier(name, "layout")

fun Context.getAndroidIdentifierDrawable(name: String) = getAndroidIdentifier(name, "drawable")

fun Context.getAndroidIdentifierColors(name: String) = getAndroidIdentifier(name, "colors")

fun Context.getAndroidIdentifierDimen(name: String) = getAndroidIdentifier(name, "dimen")

fun Context.getAndroidIdentifierBool(name: String) = getAndroidIdentifier(name, "bool")

/**
 * 获取AndroidManifest.xml文件中，<application>标签下的meta-data值。
 * */
fun Context.getApplicationMetaData(key: String): String {
    val applicationInfo: ApplicationInfo? = try {
        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    } catch (e: Exception) {
        null
    }
    return applicationInfo?.metaData?.getString(key) ?: ""
}

fun Context.hasPermission(permission: String): Boolean {
    val packageInfo = try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
    } catch (e: Exception) {
        null
    }
    packageInfo?.run {
        if (requestedPermissions.contains(permission)) {
            return true
        }
    }
    return false
}

/**
 * 获取assets String内容
 * */
fun Context.assetsStr(fileName: String): String {
    try {
        val stream = assets.open(fileName)
        val size = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        return String(buffer)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

object ResourcesUtils {

    @JvmOverloads
    fun inflate(
        context: Context?,
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = false
    ): View? = LayoutInflater.from(context).inflate(resource, root, attachToRoot)

    @JvmOverloads
    fun inflate(
        inflater: LayoutInflater,
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = false
    ): View? = inflater.inflate(resource, root, attachToRoot)

    fun <T : ViewDataBinding> inflateDataBinding(
        context: Context?,
        @LayoutRes resource: Int,
        root: ViewGroup? = null,
        attachToRoot: Boolean = false
    ): T =
        DataBindingUtil.inflate(LayoutInflater.from(context), resource, root, attachToRoot) as T

    fun getInternalId(name: String?): Int = getInternal(name, "id")

    fun getInternalLayout(name: String?): Int = getInternal(name, "layout")

    fun getInternalDrawable(name: String?): Int = getInternal(name, "drawable")

    fun getInternalColors(name: String?): Int = getInternal(name, "colors")

    fun getInternalDimen(name: String?): Int = getInternal(name, "dimen")

    fun getInternalBoolean(name: String?): Int = getInternal(name, "bool")

    fun getInternal(name: String?, type: String?): Int =
        Resources.getSystem().getIdentifier(name, type, "android")

}
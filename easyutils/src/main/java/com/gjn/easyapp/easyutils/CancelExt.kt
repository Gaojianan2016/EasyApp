package com.gjn.easyapp.easyutils

import android.content.Context
import android.os.Environment

/**
 * 清除外部缓存 /mnt/sdcard/android/data/com.xxx.xxx/cache
 */
fun Context.clearAppExternalCache() {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        externalCacheDir?.deleteFile()
    }
}

/**
 * 清除内部缓存 /data/data/com.xxx.xxx/cache
 */
fun Context.clearAppInternalCache() {
    cacheDir.deleteFile()
}

/**
 * 清除File文件 /data/data/com.xxx.xxx/files
 */
fun Context.clearAppFile() {
    filesDir.deleteFile()
}

/**
 * 清除SharedPreference /data/data/com.xxx.xxx/shared_prefs
 */
fun Context.clearAppSharedPrefs() {
    CancelUtils.DATA + packageName + CancelUtils.SHARED_PREFERENCE.file().deleteFile()
}

/**
 * 清除数据库 /data/data/com.xxx.xxx/databases
 */
fun Context.clearAppDatabases() {
    CancelUtils.DATA + packageName + CancelUtils.DATABASES.file().deleteFile()
}

fun Context.appExternalCacheSize(): Long =
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        externalCacheDir?.fileSize() ?: 0
    } else {
        0
    }

fun Context.appInternalCacheSize(): Long = cacheDir.fileSize()

fun Context.appFileSize(): Long = filesDir.fileSize()

object CancelUtils {

    const val DATA = "/data/data/"
    const val SHARED_PREFERENCE = "/shared_prefs"
    const val DATABASES = "/databases"

    fun clearAppAllData(context: Context){
        context.clearAppInternalCache()
        context.clearAppFile()
        context.clearAppExternalCache()
        context.clearAppDatabases()
        context.clearAppSharedPrefs()
    }

    fun clearAppCancel(context: Context){
        context.clearAppInternalCache()
        context.clearAppExternalCache()
    }

    fun getAppCancelSize(context: Context): Long = context.appExternalCacheSize() + context.appInternalCacheSize()

}
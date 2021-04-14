package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment

/**
 * SharedPreference路径 /data/data/packageName/shared_prefs
 * */
fun Context.sharedPreferenceDir() = "/data/data/${packageName}/shared_prefs".file()

/**
 * 数据库路径 /data/data/packageName/databases
 * */
fun Context.databasesDir() = "/data/data/${packageName}/databases".file()

/**
 * 清除外部缓存 /mnt/sdcard/android/data/packageName/cache
 */
fun Context.clearAppExternalCache() {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        externalCacheDir?.deleteFile()
    }
}

/**
 * 清除内部缓存 /data/data/packageName/cache
 */
fun Context.clearAppInternalCache() {
    cacheDir.deleteFile()
}

/**
 * 清除File文件 /data/data/packageName/files
 */
fun Context.clearAppFile() {
    filesDir.deleteFile()
}

/**
 * 清除SharedPreference /data/data/packageName/shared_prefs
 */
fun Context.clearAppSharedPrefs() {
    sharedPreferenceDir().deleteFile()
}

/**
 * 清除数据库 /data/data/packageName/databases
 */
fun Context.clearAppDatabases() {
    databasesDir().deleteFile()
}

//文件大小
fun Context.appExternalCacheSize() =
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        externalCacheDir?.fileSize() ?: 0 else 0

fun Context.appInternalCacheSize() = cacheDir.fileSize()

fun Context.appFileSize() = filesDir.fileSize()

fun Context.appSharedPrefsSize() = sharedPreferenceDir().fileSize()

fun Context.appDatabasesSize() = databasesDir().fileSize()

object CancelUtils {

    @SuppressLint("SdCardPath")
    const val DATA = "/data/data/"
    const val SHARED_PREFERENCE = "/shared_prefs"
    const val DATABASES = "/databases"

    fun clearAppAllData(context: Context) {
        context.clearAppInternalCache()
        context.clearAppFile()
        context.clearAppExternalCache()
        context.clearAppDatabases()
        context.clearAppSharedPrefs()
    }

    fun clearAppCancel(context: Context) {
        context.clearAppInternalCache()
        context.clearAppExternalCache()
    }

    fun getAppCancelSize(context: Context) =
        context.appExternalCacheSize() + context.appInternalCacheSize()

}
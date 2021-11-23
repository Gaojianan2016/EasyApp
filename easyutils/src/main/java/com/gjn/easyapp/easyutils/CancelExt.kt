package com.gjn.easyapp.easyutils

import android.content.Context
import android.os.Environment

/**
 * SharedPreference路径 /data/data/packageName/shared_prefs
 * */
inline val Context.sharedPreferenceDir get() = "/data/data/${packageName}/shared_prefs".toFile()

/**
 * 数据库路径 /data/data/packageName/databases
 * */
inline val Context.databasesDir get() = "/data/data/${packageName}/databases".toFile()

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
    sharedPreferenceDir.deleteFile()
}

/**
 * 清除数据库 /data/data/packageName/databases
 */
fun Context.clearAppDatabases() {
    databasesDir.deleteFile()
}

fun Context.clearAppAllData(){
    clearAppInternalCache()
    clearAppExternalCache()
    clearAppFile()
    clearAppDatabases()
    clearAppSharedPrefs()
}

fun Context.clearAppCancel(){
    clearAppInternalCache()
    clearAppExternalCache()
}

//文件大小
fun Context.appExternalCacheSize() =
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        externalCacheDir?.getFileLength() ?: 0 else 0

fun Context.appInternalCacheSize() = cacheDir.getFileLength()

fun Context.appFileSize() = filesDir.getFileLength()

fun Context.appSharedPrefsSize() = sharedPreferenceDir.getFileLength()

fun Context.appDatabasesSize() = databasesDir.getFileLength()

fun Context.appCancelSize() = appExternalCacheSize() + appInternalCacheSize()
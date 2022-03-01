package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment

@SuppressLint("SdCardPath")
const val DATA_DATA_PATH = "/data/data/"
const val SHARED_PREFS_PATH = "/shared_prefs"
const val DATABASES_PATH = "/databases"

/**
 * SharedPreference路径 /data/data/packageName/shared_prefs
 * */
inline val Context.sharedPreferenceDir get() = (DATA_DATA_PATH + packageName + SHARED_PREFS_PATH).toFile()

/**
 * 数据库路径 /data/data/packageName/databases
 * */
inline val Context.databasesDir get() = (DATA_DATA_PATH + packageName + DATABASES_PATH).toFile()

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

/**
 * 应用外部缓存文件大小
 * */
fun Context.appExternalCacheSize() =
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        externalCacheDir?.getFileLength() ?: 0 else 0

/**
 * 应用内部缓存文件大小
 * */
fun Context.appInternalCacheSize() = cacheDir.getFileLength()

/**
 * file文件大小
 * */
fun Context.appFileSize() = filesDir.getFileLength()

/**
 *  SharedPrefs文件大小
 * */
fun Context.appSharedPrefsSize() = sharedPreferenceDir.getFileLength()

/**
 * 数据库文件大小
 * */
fun Context.appDatabasesSize() = databasesDir.getFileLength()

/**
 * app缓存文件大小
 * */
fun Context.appCancelSize() = appExternalCacheSize() + appInternalCacheSize()
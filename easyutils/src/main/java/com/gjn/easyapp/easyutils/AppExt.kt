package com.gjn.easyapp.easyutils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.io.File

/**
 * 获取packageName的Uri
 * */
fun Context.packageNameUri(): Uri = Uri.parse("package:$packageName")

fun String.toPackageNameUri(): Uri = Uri.parse("package:$this")

fun Context.installApp(path: String?) {
    installApp(path?.file())
}

fun Context.installApp(uri: Uri?) {
    installApp(uri?.file())
}

/**
 * 安装app
 * tip: api >= 26/安卓8.0
 * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}
 * */
fun Context.installApp(file: File?) {
    if (file == null) return
    if (!file.exists()) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (!this.packageManager.canRequestPackageInstalls()) {
            openUnknownAppSettings()
        } else {
            FileUtils.openFile(this, file)
        }
    }
}

/**
 * 卸载app
 * */
fun Context.uninstallApp(pkgName: String) {
    val intent = Intent(Intent.ACTION_DELETE, pkgName.toPackageNameUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/**
 * 判断某个应用是否安装。
 * */
fun Context.isInstalled(pkgName: String): Boolean =
    try {
        packageManager.getPackageInfo(pkgName, 0)
    } catch (e: Exception) {
        null
    } != null

/**
 * 获取app的启动页名称
 * */
fun Context.getAppLauncher(pkgName: String): String {
    val info = packageManager.queryIntentActivities(
        Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(pkgName)
        }, 0
    )
    if (info.isEmpty()) {
        return ""
    }
    return info[0].activityInfo.name
}

/**
 * 判断Intent是否可用
 * */
fun Context.isIntentAvailable(intent: Intent) =
    packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0

/**
 * 打开app
 * */
fun Context.openApp(pkgName: String) {
    if (isInstalled(pkgName)) {
        startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            setClassName(pkgName, getAppLauncher(pkgName))
        })
    }
}

/**
 * 打开app设置页
 * */
fun Context.openAppDetailsSettings(pkgName: String = this.packageName) {
    if (isInstalled(pkgName)) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            pkgName.toPackageNameUri()
        )
        if (isIntentAvailable(intent)) {
            startActivity(intent)
        }
    }
}

/**
 * 打开未知来源安装设置页
 * */
fun Context.openUnknownAppSettings(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageNameUri()))
    }
}

/**
 * 获取App PackageInfo
 * */
fun Context.getAppPackageInfo(pkgName: String = this.packageName, flag: Int = 0): PackageInfo? =
    packageManager.getPackageInfo(pkgName, flag)

/**
 * 获取App图标
 * */
fun Context.getAppIcon(pkgName: String = this.packageName): Drawable? {
    return try {
        getAppPackageInfo(pkgName)?.applicationInfo?.loadIcon(packageManager)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}

/**
 * 获取App图标Id
 * */
fun Context.getAppIconId(pkgName: String = this.packageName): Int{
    return try {
        getAppPackageInfo(pkgName)?.applicationInfo?.icon ?: -1
    }catch (e: Exception){
        e.printStackTrace()
        -1
    }
}

/**
 * 获取App ApplicationName
 * */
fun Context.getApplicationName(pkgName: String = this.packageName):String {
    return try {
        getAppPackageInfo(pkgName)?.applicationInfo?.loadLabel(packageManager).toString()
    }catch (e: Exception){
        e.printStackTrace()
        ""
    }
}

/**
 * 获取App 安装路径
 * */
fun Context.getAppPath(pkgName: String = this.packageName):String {
    return try {
        getAppPackageInfo(pkgName)?.applicationInfo?.sourceDir ?: ""
    }catch (e: Exception){
        e.printStackTrace()
        ""
    }
}


/**
 * 获取App VersionName
 * */
fun Context.getAppVersionName(pkgName: String = this.packageName):String {
    return try {
        getAppPackageInfo(pkgName)?.versionName ?: ""
    }catch (e: Exception){
        e.printStackTrace()
        ""
    }
}

/**
 * 获取App VersionCode
 * */
fun Context.getAppVersionCode(pkgName: String = this.packageName):Long {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getAppPackageInfo(pkgName)?.longVersionCode ?: -1L
        }else{
            getAppPackageInfo(pkgName)?.versionCode as Long
        }
    }catch (e: Exception){
        e.printStackTrace()
        -1L
    }
}

/**
 * 获取App 签名SHA1
 * */
fun Context.getAppSignaturesSHA1(pkgName: String = this.packageName)
        = getAppSignaturesHash(pkgName)

/**
 * 获取App 签名MD5
 * */
fun Context.getAppSignaturesMD5(pkgName: String = this.packageName)
    = getAppSignaturesHash(pkgName, "MD5")

/**
 * 获取App 签名SHA256
 * */
fun Context.getAppSignaturesSHA256(pkgName: String = this.packageName)
    = getAppSignaturesHash(pkgName, "SHA256")

/**
 * 获取App 签名Hash 算法默认SHA1
 * algorithm = [SHA1, SHA256, MD5]
 * */
fun Context.getAppSignaturesHash(pkgName: String = this.packageName, algorithm: String = "SHA1"):List<String> {
    val result = mutableListOf<String>()
    val signatures = getAppSignatures(pkgName)
    signatures?.forEach {
        val hash = it.toByteArray().hashTemplate(algorithm).toHexString()
            .replace(Regex("(?<=[0-9A-F]{2})[0-9A-F]{2}"), ":$0")
        result.add(hash)
    }
    return result
}

/**
 * 获取App 签名
 * */
fun Context.getAppSignatures(pkgName: String = this.packageName): Array<Signature>?{
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getAppPackageInfo(pkgName, PackageManager.GET_SIGNING_CERTIFICATES)?.signingInfo?.let {
                if (it.hasMultipleSigners()) {
                    it.apkContentsSigners
                }else{
                    it.signingCertificateHistory
                }
            }
        }else{
            getAppPackageInfo(pkgName, PackageManager.GET_SIGNATURES)?.signatures
        }
    }catch (e: Exception){
        null
    }
}
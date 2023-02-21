package com.gjn.easyapp.easyutils

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.net.Uri
import androidx.core.content.pm.PackageInfoCompat
import com.gjn.easyapp.easyinitalizer.EasyAppInitializer

//////////////////////////////////
///// ApplicationManager
//////////////////////////////////

val application: Application get() = EasyAppInitializer.application

inline val activitiesPackageInfo: PackageInfo
    get() = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

inline val applicationAssets: AssetManager get() = application.assets

inline val applicationResources: Resources get() = application.resources

inline val applicationInfo: ApplicationInfo get() = application.applicationInfo

inline val packageName: String get() = application.packageName

inline val packageNameUri: Uri get() = application.packageNameUri

inline val packageManager: PackageManager get() = application.packageManager

inline val appName: String get() = applicationInfo.loadLabel(packageManager).toString()

inline val appVersionName: String get() = activitiesPackageInfo.versionName

inline val appVersionCode: Long get() = PackageInfoCompat.getLongVersionCode(activitiesPackageInfo)

inline val isAppDebug: Boolean
    get() = packageManager.getApplicationInfo(packageName, 0).flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
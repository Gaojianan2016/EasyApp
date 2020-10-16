package com.gjn.easyapp.easyutils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission

@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
    } else {
        connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isWifiConnected(): Boolean {
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    } else {
        connectivityManager.activeNetworkInfo?.let { it.isConnected && it.type == ConnectivityManager.TYPE_WIFI }
            ?: false
    }
}

@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isMobileConnected(): Boolean {
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    } else {
        connectivityManager.activeNetworkInfo?.let { it.isConnected && it.type == ConnectivityManager.TYPE_MOBILE }
            ?: false
    }
}


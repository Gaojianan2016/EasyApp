package com.gjn.easyapp.easyutils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

fun Context.connectivityManager() =
    applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

/**
 * 打开无线设置
 * */
fun Context.openWirelessSettings() {
    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).addNewTaskFlag())
}

/**
 * 是否连接网络
 * */
@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val cm = connectivityManager()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
    } else {
        val info = cm.activeNetworkInfo
        info?.isConnected ?: false
    }
}

/**
 * 是否连接wifi
 * */
@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isWifiConnected(): Boolean {
    val cm = connectivityManager()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    } else {
        val info = cm.activeNetworkInfo
        info?.let { it.isConnected && it.type == ConnectivityManager.TYPE_WIFI } ?: false
    }
}

/**
 * 是否连接移动网络
 * */
@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isMobileConnected(): Boolean {
    val cm = connectivityManager()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    } else {
        val info = cm.activeNetworkInfo
        info?.let { it.isConnected && it.type == ConnectivityManager.TYPE_MOBILE } ?: false
    }
}

/**
 * 是否连接以太网
 * */
@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.isEthernetConnected(): Boolean {
    val cm = connectivityManager()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false
    } else {
        val info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)
        info?.let { it.state == NetworkInfo.State.CONNECTED || it.state == NetworkInfo.State.CONNECTING }
            ?: false
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresPermission(ACCESS_NETWORK_STATE)
fun Context.registerNetworkCallback(callback: ConnectivityManager.NetworkCallback): Boolean {
    try {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager().registerNetworkCallback(request, callback)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Context.unregisterNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
    connectivityManager().unregisterNetworkCallback(callback)
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkStateManager(private val context: Context) : ConnectivityManager.NetworkCallback() {

    private var listener: OnNetworkStateListener? = null
    var isRegister = false
        private set

    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun registerNetworkCallback(listener: OnNetworkStateListener?) {
        if (isRegister) {
            if (DEBUG) {
                "-----isRegistered NetworkCallback -----".logD(TAG)
            }
            return
        }
        this.listener = listener
        isRegister = context.registerNetworkCallback(this)
        if (DEBUG) {
            "-----registerNetworkCallback $isRegister-----".logD(TAG)
        }
    }

    fun unregisterNetworkCallback() {
        this.listener = null
        context.unregisterNetworkCallback(this)
        isRegister = false
        if (DEBUG) {
            "-----unregisterNetworkCallback-----".logD(TAG)
        }
    }

    override fun onAvailable(network: Network) {
        if (DEBUG) {
            "network is connect success".logD(TAG)
        }
        listener?.onConnected(-1)
    }

    override fun onLost(network: Network) {
        if (DEBUG) {
            "network is connect lost".logD(TAG)
        }
        listener?.onDisConnected()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        if (DEBUG) {
            "network is connect changed -> $networkCapabilities".logD(TAG)
        }
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            listener?.onConnected(0)
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            listener?.onConnected(1)
        }
    }

    interface OnNetworkStateListener {
        /**
         * @param type -1 默认连接成功 0 移动网络 1 wifi网络
         * */
        fun onConnected(type: Int)

        fun onDisConnected()
    }

    companion object {
        private const val TAG = "NetworkStateManager"
        private val DEBUG = BuildConfig.DEBUG
    }
}
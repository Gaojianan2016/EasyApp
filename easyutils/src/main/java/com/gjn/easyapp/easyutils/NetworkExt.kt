package com.gjn.easyapp.easyutils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

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
    val cm = connectivityManager
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
    val cm = connectivityManager
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
    val cm = connectivityManager
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
    val cm = connectivityManager
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
        connectivityManager.registerNetworkCallback(request, callback)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Context.unregisterNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
    connectivityManager.unregisterNetworkCallback(callback)
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkStateManager : ConnectivityManager.NetworkCallback() {

    private var context: Context? = null
    private val listeners = mutableListOf<OnNetworkStateListener>()

    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun registerNetworkCallback(listener: OnNetworkStateListener) {
        if (context == null) return
        if (isRegister(listener)) {
            if (DEBUG) logD("-----isRegistered NetworkCallback -----", TAG)
            return
        }
        listeners.add(listener)
        val result = context?.registerNetworkCallback(this)
        if (DEBUG) logD("-----registerNetworkCallback $result-----", TAG)
    }

    fun unregisterNetworkCallback(listener: OnNetworkStateListener) {
        if (context == null) return
        if (isRegister(listener)) {
            listeners.remove(listener)
            context?.unregisterNetworkCallback(this)
            if (DEBUG) logD("-----unregisterNetworkCallback-----", TAG)
        }
    }

    fun clearNetworkCallback() {
        listeners.clear()
        if (DEBUG) logD("-----clearNetworkCallback-----", TAG)
    }

    fun isRegister(listener: OnNetworkStateListener) = listeners.contains(listener)

    private fun initManager(context: Context) {
        this.context = context
    }

    private fun destroyManager() {
        context = null
        clearNetworkCallback()
    }

    override fun onAvailable(network: Network) {
        if (DEBUG) logD("network is connect success", TAG)
        listeners.forEach {
            it.onConnected(-1)
        }
    }

    override fun onLost(network: Network) {
        if (DEBUG) logD("network is connect lost", TAG)
        listeners.forEach {
            it.onDisConnected()
        }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        if (DEBUG) logD("network is connect changed -> $networkCapabilities", TAG)
        val result = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> 0
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> 1
            else -> 100
        }
        listeners.forEach {
            it.onConnected(result)
        }
    }

    interface OnNetworkStateListener {
        /**
         * @param type -1 默认连接成功 0 移动网络 1 wifi网络 100其他网络
         * */
        fun onConnected(type: Int)

        fun onDisConnected()
    }

    @SuppressLint("StaticFieldLeak")
    private object LazyHolder {
        val instance = NetworkStateManager()
    }

    companion object {
        private const val TAG = "NetworkStateManager"
        private val DEBUG = BuildConfig.DEBUG

        fun get() = LazyHolder.instance

        /**
         * 初始化
         * 由于持有context对象关闭必须销毁 {@link destroy()}
         * */
        fun init(context: Context) {
            get().initManager(context)
        }

        /**
         * 销毁
         * */
        fun destroy() {
            get().destroyManager()
        }
    }
}
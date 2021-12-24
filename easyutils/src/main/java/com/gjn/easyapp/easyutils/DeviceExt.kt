package com.gjn.easyapp.easyutils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * 谷歌设置默认mac地址
 * */
private const val DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00"

/**
 * 模拟器默认AndroidID
 * */
private const val EMULATOR_ANDROID_ID = "9774d56d682e549c"

/**
 * root后新增的路径
 * */
private val rootLocations = arrayOf(
    "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
    "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
    "/system/sbin/", "/usr/bin/", "/vendor/bin/"
)

/**
 * 设备是否root
 * */
fun isDeviceRooted(): Boolean {
    val su = "su"
    for (location in rootLocations) {
        if (File(location + su).exists()) {
            return true
        }
    }
    return false
}

/**
 * 是否开启adb
 * */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.isAdbEnabled() =
    Settings.Secure.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) > 0

/**
 * 获取设备版本名称
 * */
inline val sdkVersionName: String get() = Build.VERSION.RELEASE

/**
 * 获取设备版本code
 * */
inline val sdkVersionCode: Int get() = Build.VERSION.SDK_INT

/**
 * 获取设备AndroidID
 * */
@SuppressLint("HardwareIds")
fun Context.androidID(): String {
    val id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    if (id == null || id == EMULATOR_ANDROID_ID) return ""
    return id
}

/**
 * 获取设备Mac地址
 * */
@RequiresPermission(allOf = [
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_WIFI_STATE,
    Manifest.permission.CHANGE_WIFI_STATE
])
fun Context.getMacAddress(): String {
    val macAddress = getMacAddress(*emptyArray())
    if (macAddress.isNotEmpty() || getWifiEnabled()) return macAddress
    setWifiEnabled(true)
    setWifiEnabled(false)
    return getMacAddress(*emptyArray())
}

/**
 * 获取设备Mac地址
 * excepts 例外地址
 * */
@RequiresPermission(allOf = [
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_WIFI_STATE
])
fun Context.getMacAddress(vararg excepts: String): String {
    var address = getMacAddressByNetworkInterface()
    if (isAddressNotInExcepts(address, *excepts)) {
        return address
    }
    address = getMacAddressByInetAddress()
    if (isAddressNotInExcepts(address, *excepts)) {
        return address
    }
    address = getMacAddressByWifiInfo(this)
    if (isAddressNotInExcepts(address, *excepts)) {
        return address
    }
    address = getMacAddressByFile()
    if (isAddressNotInExcepts(address, *excepts)) {
        return address
    }
    return ""
}

/**
 * 修改wifi状态
 * 需要权限 android.permission.CHANGE_WIFI_STATE
 * api 29之后应用禁止修改wifi状态
 * */
@RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
@SuppressLint("WifiManagerLeak")
fun Context.setWifiEnabled(enabled: Boolean) {
    val manager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    if (manager.isWifiEnabled == enabled) return
    manager.isWifiEnabled = enabled
}

/**
 * 获取wifi开启状态
 * */
@SuppressLint("WifiManagerLeak")
fun Context.getWifiEnabled(): Boolean {
    val manager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    return manager.isWifiEnabled
}

private fun isAddressNotInExcepts(address: String, vararg excepts: String): Boolean {
    if (address.isEmpty()) {
        return false
    }
    if (DEFAULT_MAC_ADDRESS == address) {
        return false
    }
    if (excepts.isEmpty()) {
        return true
    }
    for (except in excepts) {
        if (except == address) {
            return false
        }
    }
    return true
}

private fun ByteArray?.macBytes2Address(): String {
    if (this == null || isEmpty()) return DEFAULT_MAC_ADDRESS
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02x:", b))
    }
    return sb.substring(0, sb.length - 1)
}

private fun getMacAddressByNetworkInterface(): String {
    try {
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements()) {
            val e = nis.nextElement()
            if (e == null || !e.name.equals("wlan0", true)) {
                continue
            }
            return e.hardwareAddress.macBytes2Address()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return DEFAULT_MAC_ADDRESS
}

private fun getMacAddressByInetAddress(): String {
    try {
        getInetAddress()?.let {
            val ni = NetworkInterface.getByInetAddress(it)
            return ni?.hardwareAddress.macBytes2Address()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return DEFAULT_MAC_ADDRESS
}

private fun getInetAddress(): InetAddress? {
    try {
        val nis = NetworkInterface.getNetworkInterfaces()
        while (nis.hasMoreElements()) {
            val e = nis.nextElement()
            // To prevent phone of xiaomi return "10.0.2.15"
            if (!e.isUp) continue
            val address = e.inetAddresses
            while (address.hasMoreElements()) {
                val ia = address.nextElement()
                if (!ia.isLoopbackAddress) {
                    if (ia.hostAddress.indexOf(':') < 0) return ia
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

@SuppressLint("HardwareIds")
private fun getMacAddressByWifiInfo(context: Context): String {
    try {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifi.connectionInfo != null) {
            val address = wifi.connectionInfo.macAddress
            if (address.isNotEmpty()) {
                return address
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return DEFAULT_MAC_ADDRESS
}

private fun getMacAddressByFile(): String {
    try {
        var result = execCmd("getprop wifi.interface", false)
        if (result.result == 0) {
            val name = result.successMsg
            result = execCmd("cat /sys/class/net/$name/address", false)
            if (result.result == 0) {
                val address = result.successMsg
                if (address.isNotEmpty()) {
                    return address
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return DEFAULT_MAC_ADDRESS
}

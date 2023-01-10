package com.gjn.easyapp.easyutils

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission

/**
 * 设备是否是手机
 * */
fun Context.isDevicePhone() = telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE

/**
 * 获取设备id
 * */
@SuppressLint("MissingPermission", "HardwareIds")
@RequiresPermission(READ_PHONE_STATE)
@Throws(Exception::class)
fun Context.getDeviceId(): String =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> "Unknown DeviceId"
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            if (telephonyManager.imei.isNullOrEmpty()) telephonyManager.meid ?: "Unknown DeviceId"
            else telephonyManager.imei ?: "Unknown DeviceId"
        }
        else -> telephonyManager.deviceId ?: "Unknown DeviceId"
    }

/**
 * 获取序列号
 * */
@SuppressLint("MissingPermission", "HardwareIds")
@RequiresPermission(READ_PHONE_STATE)
@Throws(Exception::class)
fun getSerial(): String =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> Build.getSerial()
        else -> Build.SERIAL
    }

/**
 * 是否准备好sim卡
 * */
fun Context.isSimCardReady() = telephonyManager.simState == TelephonyManager.SIM_STATE_READY

/**
 * 获取sim卡运营商名称
 * */
fun Context.getSimOperatorName() = telephonyManager.simOperatorName ?: "未知运营商"

/**
 * 通过Mnc获取sim卡运营商
 * */
fun Context.getSimOperatorByMnc() =
    when (telephonyManager.simOperator) {
        "46000", "46002", "46007", "46020" -> "中国移动"
        "46001", "46006", "46009" -> "中国联通"
        "46003", "46005", "46011" -> "中国电信"
        else -> "未知运营商"
    }

/**
 * 是否是特定品牌的手机
 * */
fun isPhoneRom(roms: Array<String>): Boolean {
    for (rom in roms) {
        if (Build.BRAND.contains(rom) || Build.MANUFACTURER.contains(rom)) {
            return true
        }
    }
    return false
}

/**
 * 手机品牌
 */
fun phoneBrand(): String {
    return when {
        isPhoneRom(PhoneRom.ROM_HUAWEI) -> "华为手机"
        isPhoneRom(PhoneRom.ROM_VIVO) -> "Vivo手机"
        isPhoneRom(PhoneRom.ROM_XIAOMI) -> "小米手机"
        isPhoneRom(PhoneRom.ROM_OPPO) -> "OPPO手机"
        isPhoneRom(PhoneRom.ROM_LEECO) -> "乐视手机"
        isPhoneRom(PhoneRom.ROM_360) -> "360手机"
        isPhoneRom(PhoneRom.ROM_ZTE) -> "中兴手机"
        isPhoneRom(PhoneRom.ROM_ONEPLUS) -> "一加手机"
        isPhoneRom(PhoneRom.ROM_NUBIA) -> "努比亚手机"
        isPhoneRom(PhoneRom.ROM_COOLPAD) -> "酷派手机"
        isPhoneRom(PhoneRom.ROM_LG) -> "LG手机"
        isPhoneRom(PhoneRom.ROM_GOOGLE) -> "谷歌手机"
        isPhoneRom(PhoneRom.ROM_SAMSUNG) -> "三星手机"
        isPhoneRom(PhoneRom.ROM_MEIZU) -> "魅族手机"
        isPhoneRom(PhoneRom.ROM_LENOVO) -> "联想手机"
        isPhoneRom(PhoneRom.ROM_SMARTISAN) -> "坚果手机"
        isPhoneRom(PhoneRom.ROM_HTC) -> "HTC手机"
        isPhoneRom(PhoneRom.ROM_SONY) -> "索尼手机"
        isPhoneRom(PhoneRom.ROM_GIONEE) -> "金立手机"
        isPhoneRom(PhoneRom.ROM_MOTOROLA) -> "摩托罗拉手机"
        else -> "未知品牌"
    }
}

/**
 * 手机型号
 * */
fun phoneModel(): String = Build.MODEL

/**
 * 手机制造商
 * */
fun phoneManufacturer(): String = Build.MANUFACTURER

/**
 * 手机rom
 * */
object PhoneRom {
    val ROM_HUAWEI = arrayOf("huawei")
    val ROM_VIVO = arrayOf("vivo")
    val ROM_XIAOMI = arrayOf("xiaomi")
    val ROM_OPPO = arrayOf("oppo")
    val ROM_LEECO = arrayOf("leeco", "letv")
    val ROM_360 = arrayOf("360", "qiku")
    val ROM_ZTE = arrayOf("zte")
    val ROM_ONEPLUS = arrayOf("oneplus")
    val ROM_NUBIA = arrayOf("nubia")
    val ROM_COOLPAD = arrayOf("coolpad", "yulong")
    val ROM_LG = arrayOf("lg", "lge")
    val ROM_GOOGLE = arrayOf("google")
    val ROM_SAMSUNG = arrayOf("samsung")
    val ROM_MEIZU = arrayOf("meizu")
    val ROM_LENOVO = arrayOf("lenovo")
    val ROM_SMARTISAN = arrayOf("smartisan")
    val ROM_HTC = arrayOf("htc")
    val ROM_SONY = arrayOf("sony")
    val ROM_GIONEE = arrayOf("gionee", "amigo")
    val ROM_MOTOROLA = arrayOf("motorola")
}
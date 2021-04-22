package com.gjn.easyapp.easyutils

import android.os.Build

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

/**
 * 是否是特定品牌的手机
 * */
fun isPhoneRom(roms: Array<String>): Boolean{
    for (rom in roms) {
        if (Build.BRAND.contains(rom) || Build.MANUFACTURER.contains(rom)) {
            return true
        }
    }
    return false
}
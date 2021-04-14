package com.gjn.easyapp.easyutils

import java.security.MessageDigest

//fun String?.encryptMD2ToString(): String? = encryptMD2ToString()

/**
 * 哈希加密模板 算法默认MD5
 * algorithm = [MD2, MD5, SHA1, SHA224, SHA256, SHA384, SHA512]
 * */
fun ByteArray?.hashTemplate(algorithm: String = "MD5"): ByteArray?{
    if (this == null || this.isEmpty()) return null
    return try {
        val md = MessageDigest.getInstance(algorithm)
        md.update(this)
        md.digest()
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}

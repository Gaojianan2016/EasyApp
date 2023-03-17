package com.gjn.easyapp.easyutils

import android.os.Build
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

///////////////////////////////////////////////////////////////////////////
// HASH encryption
///////////////////////////////////////////////////////////////////////////

/**
 * MD5哈希加密
 * */
fun ByteArray?.encryptMD5ToString() = encryptHash2String("MD5")

/**
 * MD5哈希加密
 * */
fun String?.encryptMD5ToString(salt: String? = null): String {
    if (isNullOrEmpty() && salt.isNullOrEmpty()) return ""
    if (isNullOrEmpty()) return salt.encryptHash2String("MD5")
    if (salt.isNullOrEmpty()) return encryptHash2String("MD5")
    return (this + salt).encryptHash2String("MD5")
}

/**
 * 哈希加密
 * */
fun ByteArray?.encryptHash2String(algorithm: String) = hashTemplate(algorithm).toHexString()

/**
 * 哈希加密
 * */
fun String?.encryptHash2String(algorithm: String) =
    this?.toByteArray().encryptHash2String(algorithm)

///////////////////////////////////////////////////////////////////////////
// HMAC encryption
///////////////////////////////////////////////////////////////////////////

/**
 *  HmacMD5加密
 * */
fun ByteArray?.encryptHmacMD5ToString(key: ByteArray?) = encryptHmac2String(key, "HmacMD5")

/**
 * HmacMD5加密
 * */
fun String?.encryptHmacMD5ToString(key: ByteArray?) = encryptHmac2String(key, "HmacMD5")

/**
 * Hmac加密
 * */
fun ByteArray?.encryptHmac2String(key: ByteArray?, algorithm: String) =
    hmacTemplate(key, algorithm).toHexString()

/**
 * Hmac加密
 * */
fun String?.encryptHmac2String(key: ByteArray?, algorithm: String) =
    this?.toByteArray().encryptHmac2String(key, algorithm)

///////////////////////////////////////////////////////////////////////////
// DES encryption
///////////////////////////////////////////////////////////////////////////

/**
 * DES加密
 * */
fun ByteArray?.encryptDES(key: ByteArray?, transformation: String, ivParams: ByteArray?) =
    symmetricTemplate(key, "DES", transformation, ivParams, true)

/**
 * DES解密
 * */
fun ByteArray?.decryptDES(key: ByteArray?, transformation: String, ivParams: ByteArray?) =
    symmetricTemplate(key, "DES", transformation, ivParams, false)


///////////////////////////////////////////////////////////////////////////
// 3DES encryption
///////////////////////////////////////////////////////////////////////////

/**
 * 3DES加密
 * */
fun ByteArray?.encrypt3DES(key: ByteArray?, transformation: String, ivParams: ByteArray?) =
    symmetricTemplate(key, "DESede", transformation, ivParams, true)

/**
 * 3DES解密
 * */
fun ByteArray?.decrypt3DES(key: ByteArray?, transformation: String, ivParams: ByteArray?) =
    symmetricTemplate(key, "DESede", transformation, ivParams, false)

///////////////////////////////////////////////////////////////////////////
// AES encryption
///////////////////////////////////////////////////////////////////////////

/**
 * AES加密
 * */
fun ByteArray?.encryptAES(key: ByteArray?, transformation: String, ivParams: ByteArray?) =
    symmetricTemplate(key, "AES", transformation, ivParams, true)

/**
 * AES解密
 * */
fun ByteArray?.decryptAES(key: ByteArray?, transformation: String, ivParams: ByteArray?) =
    symmetricTemplate(key, "AES", transformation, ivParams, false)

///////////////////////////////////////////////////////////////////////////
// RSA encryption
///////////////////////////////////////////////////////////////////////////

/**
 * RSA加密
 * */
fun ByteArray?.encryptRSA(publicKey: ByteArray?, keySize: Int, transformation: String) =
    rsaTemplate(publicKey, keySize, transformation, true)

/**
 * RSA解密
 * */
fun ByteArray?.decryptRSA(privateKey: ByteArray?, keySize: Int, transformation: String) =
    rsaTemplate(privateKey, keySize, transformation, false)

///////////////////////////////////////////////////////////////////////////
// Template
///////////////////////////////////////////////////////////////////////////

/**
 * 哈希模板 算法默认MD5
 * algorithm = [MD2, MD5, SHA1, SHA224, SHA256, SHA384, SHA512]
 * */
fun ByteArray?.hashTemplate(algorithm: String = "MD5"): ByteArray? {
    if (this == null || isEmpty()) return null
    return try {
        val md = MessageDigest.getInstance(algorithm)
        md.update(this)
        md.digest()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Hmac模板 算法默认HmacMD5
 * algorithm = [HmacMD5, HmacSHA1, HmacSHA224, HmacSHA256, HmacSHA384, HmacSHA512]
 * */
fun ByteArray?.hmacTemplate(key: ByteArray?, algorithm: String = "HmacMD5"): ByteArray? {
    if (this == null || isEmpty() || key == null || key.isEmpty()) return null
    return try {
        val secretKey = SecretKeySpec(key, algorithm)
        val mac = Mac.getInstance(algorithm)
        mac.init(secretKey)
        mac.doFinal(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 对称模板
 * algorithm = [DES, DESede, AES]
 * transformation = DES/CBC/PKCS5Padding
 * */
fun ByteArray?.symmetricTemplate(
    key: ByteArray?,
    algorithm: String,
    transformation: String,
    ivParams: ByteArray?,
    isEncrypt: Boolean
): ByteArray? {
    if (this == null || isEmpty() || key == null || key.isEmpty()) return null
    return try {
        val secretKey = if ("DES".equals(algorithm, true)) {
            SecretKeyFactory.getInstance(algorithm).generateSecret(DESKeySpec(key))
        } else {
            SecretKeySpec(key, algorithm)
        }
        val cipher = Cipher.getInstance(transformation)
        val opMode = if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE
        if (ivParams == null || ivParams.isEmpty()) {
            cipher.init(opMode, secretKey)
        } else {
            cipher.init(opMode, secretKey, IvParameterSpec(ivParams))
        }
        cipher.doFinal(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 非对称模板
 * keySize = [1024, 2048 ...]
 * transformation = [DES, CBC, PKCS5Padding ...]
 * */
fun ByteArray?.rsaTemplate(
    key: ByteArray?,
    keySize: Int,
    transformation: String,
    isEncrypt: Boolean
): ByteArray? {
    if (this == null || isEmpty() || key == null || key.isEmpty()) return null
    return try {
        val keyFactory = if (Build.VERSION.SDK_INT < 28) {
            KeyFactory.getInstance("RSA", "BC")
        } else {
            KeyFactory.getInstance("RSA")
        }
        val rsaKey = if (isEncrypt) {
            //公钥加密
            keyFactory.generatePublic(X509EncodedKeySpec(key))
        } else {
            //私钥解密
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(key))
        }
        val cipher = Cipher.getInstance(transformation)
        val opMode = if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE
        cipher.init(opMode, rsaKey)

        var maxLen = keySize / 8
        //填充模式处理
        if (isEncrypt && transformation.endsWith("pkcs1padding", true)) {
            maxLen -= 11
        }
        //分段处理数据
        val count = size / maxLen
        if (count > 0) {
            var ret = ByteArray(0)
            var buff = ByteArray(maxLen)
            var index = 0
            //遍历全部分段
            for (i in 0 until count) {
                System.arraycopy(this, index, buff, 0, maxLen)
                ret = ret.splice(cipher.doFinal(buff))
                index += maxLen
            }
            //判断是否存在剩余数据
            if (index != size) {
                val restLen = size - index
                buff = ByteArray(restLen)
                System.arraycopy(this, index, buff, 0, maxLen)
                ret = ret.splice(cipher.doFinal(buff))
            }
            ret
        } else {
            cipher.doFinal(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * RC4 encryption/decryption.
 * 待测试 java修改而来
 * */
fun ByteArray?.rc4(
    key: ByteArray?
): ByteArray? {
    if (this == null || isEmpty() || key == null || key.isEmpty()) return null
    if (key.size > 256) {
        throw IllegalArgumentException("key must be between 1 and 256 bytes");
    }

    val iS = ByteArray(256)
    val iK = ByteArray(256)
    val keyLen = key.size
    for (i in 0 until 256) {
        iS[i] = i.toByte()
        iK[i] = key[i % keyLen]
    }

    var j = 0
    var tmp: Byte
    for (i in 0 until 256) {
        j = (j + iS[i] + iK[i]) and 0xFF
        tmp = iS[j]
        iS[j] = iS[i]
        iS[i] = tmp
    }

    val ret = ByteArray(size)
    var i = 0
    var t: Int
    var k: Byte
    for (count in indices) {
        i = (i + 1) and 0xFF
        j = (j + iS[i]) and 0xFF
        tmp = iS[j]
        iS[j] = iS[i]
        iS[i] = tmp
        t = (iS[i] + iS[j]) and 0xFF
        k = iS[t]
        ret[count] = this[count] xor k
    }
    return ret
}

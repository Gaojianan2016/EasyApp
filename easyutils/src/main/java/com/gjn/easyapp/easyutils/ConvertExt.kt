package com.gjn.easyapp.easyutils

import android.os.Parcel
import android.os.Parcelable
import java.nio.charset.Charset

private val HEX_DIGITS_UPPER = "0123456789ABCDEF".toCharArray()
private val HEX_DIGITS_LOWER = "0123456789abcdef".toCharArray()

/**
 * int 转字符串
 * */
fun Int.toHexString() = Integer.toHexString(this)

/**
 * 字符串转int
 * */
fun String.hexString2Int() = Integer.parseInt(this, 16)

/**
 * byte[] 转为 16进制字符串
 * eg. {0, byte(10)} -> 000A
 * */
fun ByteArray?.toHexString(isUpperCase: Boolean = true): String {
    if (this == null || size == 0) return ""
    val hexDigits: CharArray = if (isUpperCase) HEX_DIGITS_UPPER else HEX_DIGITS_LOWER
    //size << 1
    val ret = CharArray(size shl 1)
    var i = 0
    var j = 0
    while (i < size) {
        //bytes[i] >> 4 & 0x0f
        ret[j++] = hexDigits[this[i].toInt() shr 4 and 0x0f]
        //bytes[i] & 0x0f
        ret[j++] = hexDigits[this[i].toInt() and 0x0f]
        i++
    }
    return String(ret)
}

/**
 * 16进制字符串 转为 byte[]
 * eg. 000a -> {0, byte(10)}
 * */
fun String?.hexString2Bytes(): ByteArray {
    if (this.isNullOrEmpty()) return ByteArray(0)
    var hexString = this
    var len = length
    if (length % 2 != 0) {
        hexString = "0$hexString"
        len++
    }
    val hexBytes = hexString.toUpperCase().toCharArray()
    //len >> 1
    val ret = ByteArray(len shr 1)
    var i = 0
    while (i < len) {
        // i >> 1 = hex[i] << 4 | hex[i+1]
        ret[i shr 1] = (hexBytes[i].hex2Dec() shl 4 or hexBytes[i + 1].hex2Dec()).toByte()
        i += 2
    }
    return ret
}

private fun Char.hex2Dec(): Int {
    return when (this) {
        in '0'..'9' -> this - '0'
        in 'A'..'F' -> this - 'A' + 10
        else -> throw IllegalArgumentException()
    }
}

/**
 * Bytes to Parcelable
 * */
fun <T> ByteArray?.toParcelable(creator: Parcelable.Creator<T>): T?{
    if (this == null) return null
    val parcel = Parcel.obtain()
    parcel.unmarshall(this, 0, this.size)
    parcel.setDataPosition(0)
    val result = creator.createFromParcel(parcel) as T
    parcel.recycle()
    return result
}

/**
 * Parcelable to Bytes
 * */
fun Parcelable?.toBytes(): ByteArray?{
    if (this == null) return null
    val parcel = Parcel.obtain()
    this.writeToParcel(parcel, 0)
    val result = parcel.marshall()
    parcel.recycle()
    return result
}


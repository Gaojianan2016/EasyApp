package com.gjn.easyapp.easyutils

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * html加解密
 * 加密kotlin自带方法 String.htmlEncode 方法
 * 加密kotlin自带方法 String.parseAsHtml 方法
 * */

/**
 * url加密
 * */
fun String.urlEncode(charsetName: String = "UTF-8") = URLEncoder.encode(this, charsetName)

/**
 * url解密
 * */
fun String.urlDecode(charsetName: String = "UTF-8"): String {
    val safe = this.replace(Regex("%(?![0-9a-fA-F]{2})"), "%25")
        .replace(Regex("\\+"), "%2B")
    return URLDecoder.decode(safe, charsetName)
}

/**
 * base64加密
 * */
fun ByteArray.base64Encode(flags: Int = Base64.NO_WRAP) = Base64.encode(this, flags)

/**
 * base64加密成String
 * */
fun ByteArray.base64Encode2String(flags: Int = Base64.NO_WRAP) = Base64.encodeToString(this, flags)

/**
 * base64加密
 * */
fun String.base64Encode(flags: Int = Base64.NO_WRAP) = toByteArray().base64Encode(flags)

/**
 * base64加密
 * */
fun String.base64Encode2String(flags: Int = Base64.NO_WRAP) =
    toByteArray().base64Encode2String(flags)

/**
 * base64解密
 * */
fun ByteArray.base64Decode(flags: Int = Base64.NO_WRAP) = Base64.decode(this, flags)

/**
 * base64解密
 * */
fun String.base64Decode(flags: Int = Base64.NO_WRAP) = toByteArray().base64Decode(flags)

/**
 * 二进制加密
 * */
fun String.binaryEncode(): String {
    if (isEmpty()) return ""
    val sb = StringBuilder()
    for (char in toCharArray()) {
        sb.append(Integer.toBinaryString(char.toInt())).append(" ")
    }
    return sb.deleteCharAt(sb.length - 1).toString()
}

/**
 * 二进制解密
 * */
fun String.binaryDecode(): String {
    if (isEmpty()) return ""
    val sb = StringBuilder()
    for (str in split(Regex(" "))) {
        sb.append(Integer.parseInt(str, 2).toChar())
    }
    return sb.toString()
}
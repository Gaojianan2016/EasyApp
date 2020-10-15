package com.gjn.easyapp.easyutils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.toMd5(): String{
    try {
        //获取摘要器 MessageDigest
        val messageDigest = MessageDigest.getInstance("MD5")
        //通过摘要器对字符串的二进制字节数组进行hash计算
        val digest = messageDigest.digest(toByteArray())
        val stringBuilder = StringBuilder()
        for (byte in digest) {
            //循环每个字符 将计算结果转化为正整数
            val digestInt = byte.toInt() and 0xff
            //将10进制转化为较短的16进制
            val hex = Integer.toHexString(digestInt)
            //转化结果如果是个位数会省略0,因此判断并补0
            if(hex.length < 2) stringBuilder.append(0)
            //将循环结果添加到缓冲区
            stringBuilder.append(hex)
        }
        return stringBuilder.toString()
    }catch (e: NoSuchAlgorithmException){
        println("MD5 算法不存在")
    }
    return this
}


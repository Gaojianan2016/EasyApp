package com.gjn.easyapp.easyutils

import androidx.core.util.PatternsCompat
import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexConstants {

    /**
     * 手机号
     * */
    const val REGEX_MOBILE_NUMBER =
        "^((13[0-9])|(14[57])|(15[0-35-9])|(16[2567])|(17[01235-8])|(18[0-9])|(19[189]))\\d{8}$"

    /**
     * 电话号
     * */
    const val REGEX_TEL_NUMBER = "^0\\d{2,3}[- ]?\\d{7,8}$"

    /**
     * 一代身份证
     * */
    const val REGEX_ID_CARD_1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"

    /**
     * 二代身份证
     * */
    const val REGEX_ID_CARD_2 =
        "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"

    /**
     * url
     * */
    const val REGEX_URL = "[a-zA-z]+://[^\\s]*"

    /**
     * 中国邮编号码
     * */
    const val REGEX_ZH_POST_CODE = "[1-9]\\d{5}(?!\\d)"
}

/**
 * 是否是手机号码
 * */
fun CharSequence.isMobileNumber(): Boolean {
    if (isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_MOBILE_NUMBER, this)
}

/**
 * 是否是电话号码
 * */
fun CharSequence.isTelNumber(): Boolean {
    if (isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_TEL_NUMBER, this)
}

/**
 * 是否是身份证
 * */
fun CharSequence.isIdCard(): Boolean {
    if (isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_ID_CARD_2, this)
            || Pattern.matches(RegexConstants.REGEX_ID_CARD_1, this)
}

/**
 * 是否是E-mail
 * */
fun CharSequence.isEmail(): Boolean {
    if (isEmpty()) return false
    return PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * 是否是url
 * */
fun CharSequence.isUrl(): Boolean {
    if (isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_URL, this)
}

/**
 * 是否是webUrl
 * */
fun CharSequence.isWebUrl(): Boolean {
    if (isEmpty()) return false
    return PatternsCompat.WEB_URL.matcher(this).matches()
}

/**
 * 是否是Ip地址
 * */
fun CharSequence.isIpAddress(): Boolean {
    if (isEmpty()) return false
    return PatternsCompat.IP_ADDRESS.matcher(this).matches()
}

/**
 * 是否是中国邮编号码
 * */
fun CharSequence.isZhPostCode(): Boolean {
    if (isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_ZH_POST_CODE, this)
}

/**
 * 查找正则表达式
 * */
fun CharSequence.findRegex(regex: String?, block: (Matcher) -> Unit) {
    if (regex.isNullOrEmpty()) return
    val matcher = Pattern.compile(regex).matcher(this)
    while (matcher.find()) {
        block.invoke(matcher)
    }
}
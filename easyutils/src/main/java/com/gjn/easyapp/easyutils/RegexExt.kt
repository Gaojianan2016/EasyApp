package com.gjn.easyapp.easyutils

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
    const val REGEX_ID_CARD_2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"

    /**
     * E-mail
     * */
    const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

    /**
     * url
     * */
    const val REGEX_URL = "[a-zA-z]+://[^\\s]*"

    /**
     * ip地址
     * */
    const val REGEX_IP_ADDRESS =
        "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"

    /**
     * 中国邮编号码
     * */
    const val REGEX_ZH_POST_CODE = "[1-9]\\d{5}(?!\\d)"
}

/**
 * 是否是手机号码
 * */
fun CharSequence.isMobileNumber(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_MOBILE_NUMBER, this)
}

/**
 * 是否是电话号码
 * */
fun CharSequence.isTelNumber(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_TEL_NUMBER, this)
}

/**
 * 是否是身份证
 * */
fun CharSequence.isIdCard(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_ID_CARD_2, this)
            || Pattern.matches(RegexConstants.REGEX_ID_CARD_1, this)
}

/**
 * 是否是E-mail
 * */
fun CharSequence.isEmail(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_EMAIL, this)
}

/**
 * 是否是url
 * */
fun CharSequence.isUrl(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_URL, this)
}

/**
 * 是否是Ip地址
 * */
fun CharSequence.isIpAddress(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_IP_ADDRESS, this)
}

/**
 * 是否是中国邮编号码
 * */
fun CharSequence.isZhPostCode(): Boolean {
    if (this.isEmpty()) return false
    return Pattern.matches(RegexConstants.REGEX_ZH_POST_CODE, this)
}
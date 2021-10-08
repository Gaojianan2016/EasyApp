package com.gjn.easyapp.easyutils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import java.util.regex.Pattern

/**
 * 获取字符串
 * */
fun Context.string(
    @StringRes resId: Int,
    vararg formatArgs: Any
) = try {
    getString(resId, *formatArgs)
} catch (e: Exception) {
    e.printStackTrace()
    resId.toString()
}

/**
 * 获取字符串数组
 * */
fun Context.stringArray(@ArrayRes resId: Int): Array<String> =
    try {
        resources.getStringArray(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        arrayOf(resId.toString())
    }

/**
 * 转为MD5
 * */
fun String.toMd5() = encryptMD5ToString()

/**
 * 转义特殊词 e.g. [\ -> \\, $ -> \$, ....]
 * */
fun String.escapeSpecialWord(): String {
    if (isEmpty()) return this
    var result = this
    val specials = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
    for (special in specials) {
        if (result.contains(special)) {
            result = replace(special, "\\$special")
        }
    }
    return result
}

/**
 * 是否包含中文字符串
 * */
fun String.containsChinese(): Boolean {
    if (isEmpty()) return false
    for (c in this) if (c.isChinese()) return true
    return false
}

/**
 * 是否全是中文字符串
 * */
fun String.isChinese(): Boolean {
    if (isEmpty()) return false
    for (c in this) if (!c.isChinese()) return false
    return true
}

/**
 * 是否是中文字符
 * */
fun Char.isChinese(): Boolean {
    val ub = Character.UnicodeBlock.of(this)
    return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
}

/**
 * 是否包含emoji字符
 * */
fun String.containsEmoji(): Boolean {
    if (isEmpty()) return false
    for (c in this) if (c.isEmoji()) return true
    return false
}

/**
 * 是否是emoji字符
 * */
fun Char.isEmoji() = !(toInt() == 0x0 || toInt() == 0x9 ||
        toInt() == 0xA || toInt() == 0xD || toInt() in 0x20..0xD7FF
        || toInt() in 0xE000..0xFFFD)

/**
 * 字节转gb mb kb字符串 0.5Tb 1.20Gb 60.00Mb 798.35Kb 666b
 * */
fun Long.byteToStr(): String = when {
    this >= UnitObj.SIZE_TB -> (this / UnitObj.SIZE_TB.toDouble()).format(suffix = "Tb")
    this >= UnitObj.SIZE_GB -> (this / UnitObj.SIZE_GB.toDouble()).format(suffix = "Gb")
    this >= UnitObj.SIZE_MB -> (this / UnitObj.SIZE_MB.toDouble()).format(suffix = "Mb")
    this >= UnitObj.SIZE_KB -> (this / UnitObj.SIZE_KB.toDouble()).format(suffix = "Kb")
    else -> (toDouble()).format(0, suffix = "b")
}

/**
 * 隐藏手机号码
 * */
fun String.hidePhone(): String = hideSubstring(3, 4)

/**
 * 隐藏姓名
 * */
fun String.hideName(): String = hideSubstring()

/**
 * 隐藏中间字段 177****1234  张*良 张*
 * */
fun String.hideSubstring(start: Int = 1, end: Int = 1): String {
    if (isEmpty() || length < 2) return this
    if (length == 2) return "${substring(0, 1)}*"
    val result = StringBuilder()
    val head: String
    val foot: String
    val surplus: Int
    if (start + end > length) {
        head = substring(0, 1)
        foot = substring(length - 1)
        surplus = length - 2
    } else {
        head = substring(0, start)
        foot = substring(length - end)
        surplus = length - start - end
    }
    for (i in 0 until surplus) {
        result.append("*")
    }
    return head + result.toString() + foot
}

/**
 * 获取url最后一个/后的名字
 * */
fun String.getUrlLastName(): String =
    uri().lastPathSegment ?: substring(lastIndexOf('/') + 1)

fun String?.maxLengthMore(len: Int, more: String = "..."): String? =
    if (isNullOrEmpty() || length < len) {
        this
    } else {
        substring(0, len) + "..."
    }

object StringUtils {

    fun matcherColorSpan(
        spannable: SpannableStringBuilder,
        color: Int,
        vararg keywords: String
    ): SpannableStringBuilder {
        val keyword = arrayOfNulls<String>(keywords.size)
        //复制数组
        System.arraycopy(keywords, 0, keyword, 0, keywords.size)
        val text = spannable.toString()
        for (i in keyword.indices) {
            //处理通配符
            keyword[i] = keyword[i]!!.escapeSpecialWord()
            //忽略字母大小写
            val reg = "(?i)${keyword[i]}"
            val matcher = Pattern.compile(reg).matcher(text)
            while (matcher.find()) {
                val span = ForegroundColorSpan(color)
                spannable.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_MARK_MARK)
            }
        }
        return spannable
    }

    fun matcherDrawableSpan(
        prefix: String,
        text: String,
        start: Int = 0,
        end: Int = prefix.length,
        drawable: Drawable? = null,
        imageSpan: ImageSpan? = null
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(prefix + text)
        if (drawable == null && imageSpan == null) return spannable
        val image = drawable ?: imageSpan!!.drawable
        val span = imageSpan ?: ImageSpan(drawable!!)
        //设置图片矩形
        image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
        return spannable.apply {
            setSpan(span, start, end, ImageSpan.ALIGN_BASELINE)
        }
    }
}
package com.gjn.easyapp.easyutils

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import androidx.annotation.ColorInt
import java.util.*

/**
 * 字符串Null或Empty 执行block
 * */
fun String?.ifNullOrEmpty(block: () -> String): String = orEmpty().ifEmpty { block.invoke() }

/**
 * 字符串Null或Empty 返回value
 * */
fun String?.orValue(value: String): String = ifNullOrEmpty { value }

/**
 * 生成UUID随机数字符串
 * */
inline val randomUUIDString: String
    get() = UUID.randomUUID().toString()

/**
 * 转为MD5
 * */
fun String.toMd5() = encryptMD5ToString()

/**
 * 转义特殊词
 * e.g. [\ -> \\, $ -> \$, ....]
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
fun Char.isEmoji(): Boolean = !(code == 0x0 || code == 0x9 || code == 0xA
        || code == 0xD || code in 0x20..0xD7FF || code in 0xE000..0xFFFD)


/**
 * 字节转GB MB KB字符串
 * */
fun Int.byteToStr(isBinary: Boolean = false): String = toLong().byteToStr(isBinary)

/**
 * 字节转GB MB KB字符串
 * e.g [0.5TB 1.20GB 60.00MB 798.35KB 666B]
 * */
fun Long.byteToStr(isBinary: Boolean = false): String {
    val tb = if (isBinary) 1.tbByte_B else 1.tbByte
    val gb = if (isBinary) 1.gbByte_B else 1.gbByte
    val mb = if (isBinary) 1.mbByte_B else 1.mbByte
    val kb = if (isBinary) 1.kbByte_B else 1.kbByte
    return when {
        this >= tb -> (this / tb.toDouble()).format(suffix = "TB")
        this >= gb -> (this / gb.toDouble()).format(suffix = "GB")
        this >= mb -> (this / mb.toDouble()).format(suffix = "MB")
        this >= kb -> (this / kb.toDouble()).format(suffix = "KB")
        else -> (toDouble()).format(0, suffix = "B")
    }
}

/**
 * 隐藏手机号码 177****1234
 * */
fun String.hidePhone() = hideSubstring(3, 4)

/**
 * 隐藏姓名 张*良 张*
 * */
fun String.hideName() = hideSubstring()

/**
 * 隐藏中间字段
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
 * 获取url最后一个/后的名字 包含后缀
 * */
fun String.getUrlLastName() =
    toUri().lastPathSegment ?: substring(lastIndexOf('/') + 1)

/**
 * 获取url最后一个/后的名字 不包含后缀
 * */
fun String.getUrlLastActualName(): String {
    val lastName = getUrlLastName()
    return lastName.substring(0, lastName.lastIndexOf('.'))
}

/**
 * 删除最后一个字符
 * */
fun String.removeLast() = if (isEmpty()) this else substring(0, length - 1)

/**
 * 设置省略文本
 * */
fun String.setOmittedText(max: Int, suffix: String = "...") =
    if (isNullOrEmpty() || length < max) this else substring(0, max) + suffix

/**
 * 获取后几位字符串
 * */
fun String.latterIndexString(index: Int) =
    if (length <= index) this else substring(length - index)

/**
 * 匹配文本 改变颜色
 * */
fun SpannableStringBuilder.matcherTextToColor(
    @ColorInt changeColor: Int,
    texts: Array<String>,
    ignoreCase: Boolean = false
): SpannableStringBuilder {
    if (texts.isEmpty()) return this
    val list = arrayOfNulls<String>(texts.size)
    texts.copyInto(list)
    val temp = toString()
    for (i in list.indices) {
        list[i] = list[i]!!.escapeSpecialWord()
        val regex = if (ignoreCase) "(?i)${list[i]}" else list[i]
        temp.findRegex(regex) {
            val span = ForegroundColorSpan(changeColor)
            setSpan(span, it.start(), it.end(), Spannable.SPAN_MARK_MARK)
        }
    }
    return this
}

/**
 * 匹配文本 改变颜色
 * */
fun CharSequence.matcherTextToColor(
    @ColorInt changeColor: Int,
    texts: Array<String>,
    ignoreCase: Boolean = false
) = SpannableStringBuilder(this).matcherTextToColor(changeColor, texts, ignoreCase)

/**
 * 创建带图的SpannableStringBuilder
 * */
fun CharSequence.createImageSpannableStringBuilder(
    prefix: String = " ",
    startIndex: Int = 0,
    endIndex: Int = prefix.length,
    drawable: Drawable? = null,
    imageSpan: ImageSpan? = null
): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(prefix + this)
    if (drawable == null && imageSpan == null) return spannable
    val image = drawable ?: imageSpan!!.drawable
    val span = imageSpan ?: ImageSpan(drawable!!)
    //设置图片矩形
    image.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)
    return spannable.apply { setSpan(span, startIndex, endIndex, ImageSpan.ALIGN_BASELINE) }
}
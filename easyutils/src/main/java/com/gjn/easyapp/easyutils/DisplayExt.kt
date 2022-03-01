package com.gjn.easyapp.easyutils

import android.content.res.Resources
import android.util.TypedValue

fun Float.px2Dimension(unit: Int) =
    if (this == 0f) 0f
    else TypedValue.applyDimension(unit, this, Resources.getSystem().displayMetrics)

//直接生成dp
inline val Float.dp: Float get() = px2Dimension(TypedValue.COMPLEX_UNIT_DIP)

inline val Int.dp: Float get() = toFloat().dp

inline val Double.dp: Float get() = toFloat().dp

inline val Long.dp: Float get() = toFloat().dp

//直接生成sp
inline val Float.sp: Float get() = px2Dimension(TypedValue.COMPLEX_UNIT_SP)

inline val Int.sp: Float get() = toFloat().sp

inline val Double.sp: Float get() = toFloat().sp

inline val Long.sp: Float get() = toFloat().sp

//直接生成pt
inline val Float.pt: Float get() = px2Dimension(TypedValue.COMPLEX_UNIT_PT)

inline val Int.pt: Float get() = toFloat().pt

inline val Double.pt: Float get() = toFloat().pt

inline val Long.pt: Float get() = toFloat().pt

//px转dp
fun Float.px2dp(): Float = this / Resources.getSystem().displayMetrics.density + 0.5f

fun Int.px2dp(): Float = toFloat().px2dp()

fun Double.px2dp(): Float = toFloat().px2dp()

fun Long.px2dp(): Float = toFloat().px2dp()

//px转sp
fun Float.px2sp(): Float = this / Resources.getSystem().displayMetrics.scaledDensity + 0.5f

fun Int.px2sp(): Float = toFloat().px2sp()

fun Double.px2sp(): Float = toFloat().px2sp()

fun Long.px2sp(): Float = toFloat().px2sp()

//px转pt
fun Float.px2pt(): Float = this * 72 / Resources.getSystem().displayMetrics.xdpi + 0.5f

fun Int.px2pt(): Float = toFloat().px2pt()

fun Double.px2pt(): Float = toFloat().px2pt()

fun Long.px2pt(): Float = toFloat().px2pt()


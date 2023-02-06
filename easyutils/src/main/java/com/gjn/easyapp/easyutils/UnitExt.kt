package com.gjn.easyapp.easyutils

import java.util.concurrent.TimeUnit
import kotlin.math.pow

//生成时间毫秒
inline val Int.secondsMillis: Long get() = TimeUnit.SECONDS.toMillis(toLong())

inline val Int.minuteMillis: Long get() = TimeUnit.MINUTES.toMillis(toLong())

inline val Int.hourMillis: Long get() = TimeUnit.HOURS.toMillis(toLong())

inline val Int.daysMillis: Long get() = TimeUnit.DAYS.toMillis(toLong())

//生成时间秒
inline val Int.minuteSeconds: Long get() = TimeUnit.MINUTES.toSeconds(toLong())

inline val Int.hourSeconds: Long get() = TimeUnit.HOURS.toSeconds(toLong())

inline val Int.daysSeconds: Long get() = TimeUnit.DAYS.toSeconds(toLong())

//生成大小Byte
//十进制
const val SIZE_BYTE_D = 10.0

inline val Int.kbByte: Long get() = (this * SIZE_BYTE_D.pow(3)).toLong()

inline val Int.mbByte: Long get() = (this * SIZE_BYTE_D.pow(6)).toLong()

inline val Int.gbByte: Long get() = (this * SIZE_BYTE_D.pow(9)).toLong()

inline val Int.tbByte: Long get() = (this * SIZE_BYTE_D.pow(12)).toLong()

inline val Int.pbByte: Long get() = (this * SIZE_BYTE_D.pow(15)).toLong()

inline val Long.kbByte: Long get() = this * 1.kbByte

inline val Long.mbByte: Long get() = this * 1.mbByte

inline val Long.gbByte: Long get() = this * 1.gbByte

inline val Long.tbByte: Long get() = this * 1.tbByte

inline val Long.pbByte: Long get() = this * 1.pbByte

//二进制
const val SIZE_BYTE_B = 2.0

inline val Int.kbByte_B: Long get() = (this * SIZE_BYTE_B.pow(10)).toLong()

inline val Int.mbByte_B: Long get() = (this * SIZE_BYTE_B.pow(20)).toLong()

inline val Int.gbByte_B: Long get() = (this * SIZE_BYTE_B.pow(30)).toLong()

inline val Int.tbByte_B: Long get() = (this * SIZE_BYTE_B.pow(40)).toLong()

inline val Int.pbByte_B: Long get() = (this * SIZE_BYTE_B.pow(50)).toLong()

inline val Long.kbByte_B: Long get() = this * 1.kbByte_B

inline val Long.mbByte_B: Long get() = this * 1.mbByte_B

inline val Long.gbByte_B: Long get() = this * 1.gbByte_B

inline val Long.tbByte_B: Long get() = this * 1.tbByte_B

inline val Long.pbByte_B: Long get() = this * 1.pbByte_B
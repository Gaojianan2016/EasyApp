package com.gjn.easyapp.easyutils

object UnitObj {

    const val TIME_MINUTE = 60L
    const val TIME_HOUR = TIME_MINUTE * 60
    const val TIME_DAY = TIME_HOUR * 24
    const val TIME_WEEK = TIME_DAY * 7
    const val TIME_MONTH = TIME_DAY * 30
    const val TIME_YEAR = TIME_DAY * 365

    private const val MILLIS_UNIT = 1000L
    const val TIME_SECONDS_MILLIS = MILLIS_UNIT
    const val TIME_MINUTE_MILLIS = TIME_MINUTE * MILLIS_UNIT
    const val TIME_HOUR_MILLIS = TIME_HOUR * MILLIS_UNIT
    const val TIME_DAY_MILLIS = TIME_DAY * MILLIS_UNIT
    const val TIME_WEEK_MILLIS = TIME_WEEK * MILLIS_UNIT
    const val TIME_MONTH_MILLIS = TIME_MONTH * MILLIS_UNIT
    const val TIME_TIME_YEAR_MILLIS = TIME_YEAR * MILLIS_UNIT

    private const val SIZE_UNIT = 1000L
    const val SIZE_KB = SIZE_UNIT
    const val SIZE_MB = SIZE_KB * SIZE_UNIT
    const val SIZE_GB = SIZE_MB * SIZE_UNIT
    const val SIZE_TB = SIZE_GB * SIZE_UNIT
}
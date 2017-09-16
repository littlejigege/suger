package com.mobile.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jimji on 2017/9/16.
 */
//是否闰年
fun Int.isLeapYear() = (this % 4 == 0) && (this % 100 != 0) || (this % 400 == 0)

fun Long.date(pattern: String = "yyyy-MM-dd HH:mm:ss"): String? = SimpleDateFormat(pattern, Locale.CHINA).format(this)

fun String.toCalender(pattern: String = "yyyy-MM-dd HH:mm:ss"): Calendar {
    val c: Calendar = Calendar.getInstance()
    c.time = SimpleDateFormat(pattern, Locale.CHINA).parse(this)
    return c
}

fun Calendar.toStr(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern, Locale.CHINA)
    val result = sdf.format(time)
    return result
}
package com.mobile.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jimji on 2017/9/16.
 */
//是否闰年
fun isLeapYear(year:Int) = (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)

fun StringToCalender(date:String,pattern: String = "yyyy-MM-dd HH:mm:ss"): Calendar {
    val c: Calendar = Calendar.getInstance()
    c.time = SimpleDateFormat(pattern, Locale.CHINA).parse(date)
    return c
}

fun Calendar.toStr(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern, Locale.CHINA)
    val result = sdf.format(time)
    return result
}
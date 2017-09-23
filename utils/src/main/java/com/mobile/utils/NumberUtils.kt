package com.mobile.utils

/**
 * Created by jimji on 2017/9/22.
 */
//计算数字的整数位
val Number.digits: Int
    get() {
        if (this.toLong() <= 0) return 0
        return (Math.log10(this.toDouble()) + 1).toInt()
    }
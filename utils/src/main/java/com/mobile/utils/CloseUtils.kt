package com.mobile.utils

import java.io.Closeable

/**
 * Created by jimji on 2017/9/16.
 */
fun Closeable.saveClose() {
    try {
        close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
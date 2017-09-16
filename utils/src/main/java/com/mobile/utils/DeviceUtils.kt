package com.mobile.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import com.mobile.utils.Utils.Companion.app

/**
 * Created by jimji on 2017/9/16.
 */
fun isXiaomi() = Build.BRAND.toLowerCase().contains("xiaomi")
fun isPhone(): Boolean = !_isTablet(app)
private fun _isTablet(context: Context): Boolean {
    return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}


package com.qgmobile.suger

import android.app.Application
import com.mobile.utils.Utils

/**
 * Created by jimji on 2017/9/16.
 */

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}
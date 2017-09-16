package com.mobile.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by jimji on 2017/9/15.
 */
class Utils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var app: Application

        fun init(app: Application) {
            this.app = app
            this.app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(p0: Activity?) {

                }

                override fun onActivityResumed(p0: Activity?) {
                }

                override fun onActivityStarted(p0: Activity?) {

                }

                override fun onActivityDestroyed(p0: Activity) {
                    ActivityManager.remove(p0)
                }

                override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {

                }

                override fun onActivityStopped(p0: Activity?) {
                }

                override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                    ActivityManager.add(p0)
                }
            })
        }
    }
}
package com.mobile.utils.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.mobile.utils.Utils.Companion.app

/**
 * Created by jimji on 2017/11/11.
 */

enum class Permission(val value: String) {
    STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),
    SMS(Manifest.permission.SEND_SMS),
    SENSORS(Manifest.permission.BODY_SENSORS),
    PHONE(Manifest.permission.READ_PHONE_STATE),
    MICROPHONE(Manifest.permission.RECORD_AUDIO),
    LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    CONTACTS(Manifest.permission.READ_CONTACTS),
    CAMERA(Manifest.permission.CAMERA),
    CALENDER(Manifest.permission.READ_CALENDAR);

    fun get(activity: Activity, callBack: (isPassed: Boolean) -> Unit) {

        val task = PermissionTask(this, object : PermissionGetCallBack {
            override fun onPassed() {
                callBack(true)
            }

            override fun onDenied() {
                callBack(false)
            }
        })
        checkActivityAndQueue(activity, task)
    }

    fun doAfterGet(activity: Activity, action: () -> Unit) {
        val task = PermissionTask(this, object : PermissionGetCallBack {
            override fun onPassed() {
                action()
            }

            override fun onDenied() {

            }
        })
        checkActivityAndQueue(activity, task)
    }

    private fun checkActivityAndQueue(activity: Activity, task: PermissionTask) {
        if (activity !is PermissionActivity && activity !is PermissionCompatActivity) {
            throw IllegalArgumentException("activity should extend PermissionCompatActivity or PermissionActivity")
        }
        if (activity is PermissionActivity) {
            getPermissionMan(activity, 1).queue(task)
        } else if (activity is PermissionCompatActivity) {
            getPermissionMan(activity, 0).queue(task)
        }
    }

    fun has(): Boolean {
        return ContextCompat.checkSelfPermission(app, this.value) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermissionMan(activity: Activity, a: Int): PermissionMan {
        return when (a) {
            1 -> {
                val obj = PermissionActivity::class.java
                val f = obj.getDeclaredField("permissionMan")
                f.isAccessible = true
                f.get(activity) as PermissionMan
            }
            else -> {
                val obj = PermissionCompatActivity::class.java
                val f = obj.getDeclaredField("permissionMan")
                f.isAccessible = true
                f.get(activity) as PermissionMan
            }
        }
    }
}





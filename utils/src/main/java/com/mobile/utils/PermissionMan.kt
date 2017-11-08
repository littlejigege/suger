package com.mobile.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.os.Build


/**
 * Created by jimji on 2017/9/12.
 */
class PermissionMan(private var ctx: Activity) {
    var permissionGetting = ""
    private var _dinied: PermissionMan.() -> Unit = {}
    private var _passed: PermissionMan.() -> Unit = {}
    private var todo: () -> Unit = {}
    fun onDinied(d: PermissionMan.() -> Unit) {
        _dinied = d
    }

    fun onPassed(p: PermissionMan.() -> Unit) {
        _passed = p
    }


    val STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val SMS = Manifest.permission.SEND_SMS
    val SENSORS = Manifest.permission.BODY_SENSORS
    val PHONE = Manifest.permission.READ_PHONE_STATE
    val MICROPHONE = Manifest.permission.RECORD_AUDIO
    val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    val CONTACTS = Manifest.permission.READ_CONTACTS
    val CAMERA = Manifest.permission.CAMERA
    val CALENDER = Manifest.permission.READ_CALENDAR


    fun use(use: PermissionMan.() -> Unit) = this.use()


    fun String.check(): Boolean {
        return ContextCompat.checkSelfPermission(ctx, this) == PackageManager.PERMISSION_GRANTED
    }

    fun String.get() {
        if (!check()) {
            permissionGetting = this
            ActivityCompat.requestPermissions(ctx, arrayOf(this), 1)
        } else {
            _passed()
        }
    }

    private val String.name: String
        get() {
            var result = "123"
            when (this) {
                STORAGE -> result = "储存卡"

                SMS -> result = "短信"

                SENSORS -> result = "传感器"

                PHONE -> result = "电话"

                MICROPHONE -> result = "麦克风"

                LOCATION -> result = "定位"

                CONTACTS -> result = "联系人"

                CAMERA -> result = "相机"

                CALENDER -> result = "日历"

            }
            return result
        }

    fun doAfterGet(permission: String, todo: () -> Unit) {
        if (permission.check()) {
            todo()
        } else {
            this.todo = todo
            permission.get()
        }
    }


    fun getAppDetailSettingIntent() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", ctx.packageName, null)
            ctx.startActivity(intent)
        }
    }

    fun onResult() {
        if (permissionGetting.check()) {
            _passed()
            todo()
            todo = {}
        } else {
            _dinied()
            todo = {}
        }
    }

}

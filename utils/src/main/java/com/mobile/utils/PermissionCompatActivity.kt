package com.mobile.utils

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.qg.musicmaven.permission.PermissionMan

/**
 * Created by jimji on 2017/9/12.
 */
open class PermissionCompatActivity : AppCompatActivity() {
    private val permissionMan by lazy { PermissionMan(this) }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionMan.onResult()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
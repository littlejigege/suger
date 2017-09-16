package com.qg.musicmaven.permission

import android.app.Activity
import android.support.v7.app.AppCompatActivity

/**
 * Created by jimji on 2017/9/12.
 */
open class PermissionActivity : Activity() {
    private val permissionMan by lazy { PermissionMan(this) }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        permissionMan.onResult()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
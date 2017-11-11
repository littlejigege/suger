package com.mobile.utils.permission

import android.app.Activity

/**
 * Created by jimji on 2017/9/12.
 */
@Suppress("LeakingThis")
open class PermissionActivity : Activity() {
    private val permissionMan = PermissionMan(this)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        permissionMan.onResult()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
package com.qgmobile.suger

import android.os.*
import com.mobile.utils.*
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity


class MainActivity : AlbumPickerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AlbumPicker.with(this).selectedPicAndHandle { it?.toast() }
    }

    override fun onBackPressed() {
        ActivityManager.doubleExit()
    }
}

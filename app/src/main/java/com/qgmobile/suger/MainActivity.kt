package com.qgmobile.suger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.mobile.utils.fullScreen
import com.mobile.utils.setStatusBarTextBlack
import com.mobile.utils.setStatusBarTextWhite


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fullScreen()
        setStatusBarTextBlack()
    }
}

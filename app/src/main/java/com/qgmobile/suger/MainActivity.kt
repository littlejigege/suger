package com.qgmobile.suger

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.UriMatcher
import android.hardware.*
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.provider.Telephony
import android.support.annotation.CallSuper
import android.support.annotation.UiThread
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.mobile.utils.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.run


class MainActivity : PermissionCompatActivity() {
    lateinit var ctx: Context
    lateinit var adapter: EasyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarTextWhite()
    }

    override fun onBackPressed() {
        ActivityManager.doubleExit()
    }




}

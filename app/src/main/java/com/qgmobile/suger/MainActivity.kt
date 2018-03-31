package com.qgmobile.suger

import android.app.Notification
import android.content.Intent
import android.os.*
import android.support.v4.content.FileProvider
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.mobile.utils.*
import com.mobile.utils.downloader.Downloader
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.File
import java.io.IOException


class MainActivity : AlbumPickerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}





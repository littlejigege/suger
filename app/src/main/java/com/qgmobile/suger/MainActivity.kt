package com.qgmobile.suger

import android.app.IntentService
import android.content.Intent
import android.content.UriMatcher
import android.hardware.*
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.provider.Telephony
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

    lateinit var adapter: EasyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setStatusBarTextWhite()
//        adapter = EasyAdapter()
//        adapter.addConfig(EasyAdapter.ItemConfig(this, String::class) {
//            layId = R.layout.item
//            onBindData { data, holder -> holder.itemView.textView.text = data.toString() }
//            onClick { data, pos -> showToast("$pos  $data") }
//        })
//        adapter.addConfig(EasyAdapter.ItemConfig(this, Int::class) {
//            layId = R.layout.item2
//            onBindData { data, holder -> holder.itemView.textView.text = data.toString() }
//            onClick { data, pos -> showToast("$pos  $data") }
//        })
//        adapter.addData(listOf("wdsda", 1, 3, "6345",12,3,4,5,6,7,7,8,8,98))
//        list.layoutManager = LinearLayoutManager(this)
//        list.adapter = adapter
//        list.setOnLoadMoreListener { adapter.addData(listOf("wdsda", 1, 3, "6345",12,3,4,5,6,7,7,8,8,98)) }

        PermissionMan(this).use { onDinied {  }
            onPassed {  }
            STORAGE

        }

    }

    override fun onBackPressed() {
        ActivityManager.doubleExit()
    }


}

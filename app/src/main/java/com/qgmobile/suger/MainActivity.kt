package com.qgmobile.suger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.mobile.utils.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*


class MainActivity : AppCompatActivity() {
    lateinit var adapter: EasyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarTextWhite()

        adapter = EasyAdapter()
        adapter.addConfig(EasyAdapter.ItemConfig(String::class, 1) {
            layId = R.layout.item
            onBindData { data, holder -> holder.itemView.textView.text = data.toString() }
            onClick { data, pos -> showToast("$pos  $data") }
        })
        adapter.addConfig(EasyAdapter.ItemConfig(Int::class, 2) {
            layId = R.layout.item2
            onBindData { data, holder -> holder.itemView.textView.text = data.toString() }
            onClick { data, pos -> showToast("$pos  $data") }
        })
        adapter.addData(listOf("wdsda", 1, 3, "6345"))
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

    }

    override fun onBackPressed() {
        ActivityManager.doubleExit()
    }
}

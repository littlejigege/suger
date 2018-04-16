package com.qgmobile.suger


import android.os.*
import com.mobile.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch


class MainActivity : AlbumPickerActivity(), NetworkObserver.NetCallback {
    override fun onChange(last: NetworkType, now: NetworkType) {
        showToast("$last  $now")
    }

    val a = NetworkObserver().apply { this.addListener(this@MainActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val a = newActorWithDelay<Unit>(1000) {
            println("666")
        }
        launch(CommonPool) {
            while (true) {
                a.offer(Unit)
            }
        }
        button2.setOnTouchListener { _, _ -> println("777777");false }
        button2.setOnClickListener { a.close() }
    }

}





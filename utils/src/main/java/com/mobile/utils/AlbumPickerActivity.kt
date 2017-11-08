package com.mobile.utils

import android.content.Intent
import android.support.v7.app.AppCompatActivity

/**
 * Created by 铖哥 on 2017/11/8.
 */
open class AlbumPickerActivity : AppCompatActivity(){

    var albumPicker : AlbumPicker?  = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        albumPicker?.onActivityResult(requestCode,resultCode,data)
    }
}
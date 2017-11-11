package com.mobile.utils

import android.content.Intent
import android.provider.MediaStore
import android.content.ContentUris
import android.provider.DocumentsContract
import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity


/**
 * Created by steve on 17-8-31.
 */


class AlbumPicker private constructor(private var act:AlbumPickerActivity) {

    private val ALBUM_PICKER = 999
    lateinit private var handler: (String?) -> Unit

    companion object {
        fun with(act: AlbumPickerActivity): AlbumPicker {
            val obj = AlbumPicker(act)
            act.albumPicker = obj
            return obj
        }
    }



    fun selectedPicAndHandle(handler: (String?) -> Unit) {
        PermissionMan(act).use {
            onPassed {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = ("image/*")
                act.startActivityForResult(intent, ALBUM_PICKER)
                this@AlbumPicker.handler = handler
            }
            onDinied {
                showToast("permssion denied")
            }
            STORAGE.get()
        }


    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ALBUM_PICKER) {

            if (resultCode == AppCompatActivity.RESULT_OK) {
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data!!)
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data!!)
                }
            }
        }
    }

    @TargetApi(19)
    private fun handleImageOnKitKat(data: Intent) {
        var imagePath: String? = null
        val uri = data.data
        if (DocumentsContract.isDocumentUri(act, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] // 解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId)!!)
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.path
        }

        handler(imagePath)

    }

    private fun handleImageBeforeKitKat(data: Intent) {
        val uri = data.data
        val imagePath = getImagePath(uri, null)
        handler(imagePath)
    }


    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        // 通过Uri和selection来获取真实的图片路径
        val cursor = act.getContentResolver().query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

}
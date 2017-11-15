package com.mobile.utils

import android.app.DownloadManager
import android.net.Uri
import java.io.File

/**
 * Created by jimji on 2017/9/17.
 */
fun systemDownload(url: String, file: File) {
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDestinationUri(Uri.parse("file://${file.absolutePath}"))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    downloadManager.enqueue(request)
}
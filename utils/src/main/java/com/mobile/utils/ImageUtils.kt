package com.mobile.utils

import android.graphics.*
import java.io.ByteArrayOutputStream
import android.view.View


/**
 * Created by jimji on 2017/9/16.
 */
fun Bitmap.toBytes(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100): ByteArray {
    val baos = ByteArrayOutputStream()
    compress(format, 100, baos)
    return baos.toByteArray()
}

fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

fun View.toBitmap(): Bitmap {
    val ret = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(ret)
    val bgDrawable = background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    draw(canvas)
    return ret
}

fun Bitmap.toRound(): Bitmap {
    val width = width
    val height = height
    val radius = Math.min(width, height) shr 1
    val ret = Bitmap.createBitmap(width, height, config)
    val paint = Paint()
    val canvas = Canvas(ret)
    val rect = Rect(0, 0, width, height)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawCircle((width shr 1).toFloat(), (height shr 1).toFloat(), radius.toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return ret
}

fun Bitmap.scale(newWidth: Int, newHeight: Int): Bitmap {
    val ret = Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    return ret
}

fun Bitmap.compressByQuality(maxByteSize: Long): Bitmap? {
    if (maxByteSize <= 0) return null
    val baos = ByteArrayOutputStream()
    var quality = 100
    compress(Bitmap.CompressFormat.JPEG, quality, baos)
    while (baos.toByteArray().size > maxByteSize && quality > 0) {
        baos.reset()
        compress(Bitmap.CompressFormat.JPEG, { quality -= 5;quality }(), baos)
    }
    if (quality < 0) return null
    val bytes = baos.toByteArray()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

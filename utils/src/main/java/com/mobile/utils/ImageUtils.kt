@file:JvmName("ImageUtils")
package com.mobile.utils

import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
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


fun Bitmap.size() = rowBytes * byteCount


fun gaussBlud(bitmap: Bitmap,blurRadius : Float = 20f ): Bitmap {

    val BITMAP_SCALE = 0.4f
    val width = Math.round(bitmap.getWidth() * BITMAP_SCALE)
    val height = Math.round(bitmap.getHeight() * BITMAP_SCALE)

    // 将缩小后的图片做为预渲染的图片
    val inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    // 创建一张渲染后的输出图片
    val outputBitmap = Bitmap.createBitmap(inputBitmap)

    // 创建RenderScript内核对象
    val rs = RenderScript.create(Utils.app)
    // 创建一个模糊效果的RenderScript的工具对象
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
    // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

    // 设置渲染的模糊程度, 25f是最大模糊度
    blurScript.setRadius(blurRadius)
    // 设置blurScript对象的输入内存
    blurScript.setInput(tmpIn)
    // 将输出数据保存到输出内存中
    blurScript.forEach(tmpOut)

    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap)

    return outputBitmap
}

fun compressByQuality(bitmap: Bitmap,maxByteSize: Long): Bitmap? {
    if (maxByteSize <= 0) return null
    val baos = ByteArrayOutputStream()
    var quality = 100
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
    while (bitmap.size() > maxByteSize && quality > 0) {
        baos.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, { quality -= 5;quality }(), baos)
    }
    if (quality < 0) return null
    val bytes = baos.toByteArray()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}



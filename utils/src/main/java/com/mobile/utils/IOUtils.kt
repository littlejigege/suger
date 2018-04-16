package com.mobile.utils

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private fun ZipOutputStream.zipFolderFrom(src:String){
    zip(File(src).listFiles(), File(src).name)
}

fun ZipOutputStream.zipFrom(vararg srcs:String){
    val files = srcs.map { File(it) }
    files.forEach { if(it.isFile) zipFileFrom(it.path) else if(it.isDirectory) zipFolderFrom(it.path) }
    this.close()
}

private fun ZipOutputStream.zipFileFrom(src:String){
    val file = File(src)
    this.zip(arrayOf(file),null)
}

private fun ZipOutputStream.zip(files:Array<File>, path:String?){
    val prefix = if(path == null) "" else "$path/"
    files.forEach {
        if(it.isFile){
            val entry = ZipEntry("$prefix${it.name}")
            val ins = it.inputStream().buffered()
            this.putNextEntry(entry)
            ins.writeTo(this,true, DEFAULT_BUFFER_SIZE)
            this.closeEntry()
        }else{
            this.zip(it.listFiles(),"$prefix${it.name}")
        }
    }
}

fun InputStream.writeTo(outputStream: OutputStream, autoClose: Boolean = false, bufferSize: Int = 1024*2) {
    val buffer = ByteArray(bufferSize)
    val br = this.buffered()
    val bw = outputStream.buffered()
    var length = 0

    while ({ length = br.read(buffer); length != -1 }.invoke()) {
        bw.write(buffer, 0, length)
    }

    bw.flush()

    if (autoClose) {
        close()
    }
}

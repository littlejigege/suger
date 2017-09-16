package com.mobile.utils

import java.io.*

/**
 * Created by jimji on 2017/9/16.
 */
//不存在则创建目录，存在则不创建
fun File.toggleDir() = if (!exists()) mkdirs() else true


//不存在则创建文件，存在则不创建
fun File.toggleFile() = if (!exists()) createNewFile() else true


fun File.moveTo(path: String): Boolean {
    if (!exists()) return false
    return if (isDirectory) {
        copyOrMoveDir(this, File(path), true)
    } else {
        copyOrMoveFile(this, File(path), true)
    }
}

fun File.copyTo(path: String): Boolean {
    if (!exists()) return false
    return if (isDirectory) {
        copyOrMoveDir(this, File(path), false)
    } else {
        copyOrMoveFile(this, File(path), false)
    }
}

private fun copyOrMoveDir(srcDir: File, destDir: File, isMove: Boolean): Boolean {
    // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
    // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
    // 为防止以上这种情况出现出现误判，须分别在后面加个路径分隔符
    val srcPath = srcDir.path + File.separator
    val destPath = destDir.path + File.separator
    if (destPath.contains(srcPath)) return false
    // 源文件不存在或者不是目录则返回false
    if (!srcDir.exists() || !srcDir.isDirectory) return false
    // 目标目录不存在返回false
    if (!destDir.toggleDir()) return false
    val files = srcDir.listFiles()
    for (file in files) {
        val oneDestFile = File(destPath + file.name)
        if (file.isFile) {
            // 如果操作失败返回false
            if (!copyOrMoveFile(file, oneDestFile, isMove)) return false
        } else if (file.isDirectory) {
            // 如果操作失败返回false
            if (!copyOrMoveDir(file, oneDestFile, isMove)) return false
        }
    }
    return !isMove || srcDir.smartDelete()
}

private fun copyOrMoveFile(srcFile: File, destFile: File, isMove: Boolean): Boolean {
    // 源文件不存在或者不是文件则返回false
    if (!srcFile.exists() || !srcFile.isFile) return false
    // 目标文件存在且是文件则返回false
    if (destFile.exists() && destFile.isFile) return false
    // 目标目录不存在返回false
    if (!destFile.parentFile.toggleDir()) return false
    return try {
        writeFileFromIS(destFile, FileInputStream(srcFile), false) && !(isMove && !srcFile.smartDelete())
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        false
    }

}

private fun writeFileFromIS(file: File, `is`: InputStream, append: Boolean): Boolean {
    if (!file.toggleFile()) return false
    var os: OutputStream? = null
    return try {
        os = BufferedOutputStream(FileOutputStream(file, append))
        val data = ByteArray(8129)
        var len: Int = 0
        while ({ len = `is`.read(data, 0, 8129);len }() != -1) {
            os.write(data, 0, len)
        }
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        `is`.saveClose()
        os?.saveClose()
    }
}


fun File.smartDelete(): Boolean {
    // 目录不存在返回true
    if (!exists()) return true
    if (isDirectory) {
        // 现在文件存在且是文件夹
        val files = listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isFile) {
                    if (!file.delete()) return false
                } else if (file.isDirectory) {
                    if (file.smartDelete()) return false
                }
            }
        }
    }
    return delete()
}
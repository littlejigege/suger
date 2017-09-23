package com.mobile.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by jimji on 2017/9/16.
 */
/**
 * 转化为半角字符
 */
fun String.toDBC(): String {
    if (isEmpty()) return this
    val chars = toCharArray()
    var i = 0
    val len = chars.size
    while (i < len) {
        when {
            chars[i].toInt() == 12288 -> chars[i] = ' '
            chars[i].toInt() in 65281..65374 -> chars[i] = (chars[i].toInt() - 65248).toChar()
            else -> chars[i] = chars[i]
        }
        i++
    }
    return String(chars)
}

/**
 * 转化为全角字符
 */
fun String.toSBC(): String {
    if (isEmpty()) return this
    val chars = toCharArray()
    var i = 0
    val len = chars.size
    while (i < len) {
        when {
            chars[i] == ' ' -> chars[i] = 12288.toChar()
            chars[i].toInt() in 33..126 -> chars[i] = (chars[i].toInt() + 65248).toChar()
            else -> chars[i] = chars[i]
        }
        i++
    }
    return String(chars)
}

fun String.md5(): String = encrypt(this, "MD5")

fun String.sha1() = encrypt(this, "SHA-1")


private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}

fun String.delete(regex: String): String = replace(regex, "")

fun String.delete(startIndex: Int, endIndex: Int) = replaceRange(IntRange(startIndex, endIndex), "")

val String.isEmail: Boolean
    get() = matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$".toRegex())

val String.isNumber: Boolean
    get() = matches("^[0-9]+\$".toRegex())

val String.isPhone: Boolean
    get() = matches("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,1,3,5-8])|(18[0-9])|(147))\\d{8}$".toRegex())

val String.isIdCard: Boolean
    get() = matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$".toRegex()) && matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$".toRegex())

val String.isChinese: Boolean
    get() = matches("^[\\u4e00-\\u9fa5]+$".toRegex())

val String.isEnglish: Boolean
    get() = matches("[a-zA-Z]".toRegex())


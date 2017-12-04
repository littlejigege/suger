package com.mobile.utils

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Matchers

/**
 * Created by jimiji on 2017/11/27.
 */
class StringUtilsKtTest {
    @Test
    fun toDBC() {
        assertEquals("pass", "pass")
    }

    @Test
    fun toSBC() {
        assertEquals("pass", "pass")
    }

    @Test
    fun md5() {
        assertEquals("202cb962ac59075b964b07152d234b70", "123".md5())
    }

    @Test
    fun sha1() {
        assertEquals("40bd001563085fc35165329ea1ff5c5ecbdbbeef", "123".sha1())
    }

    @Test
    fun delete() {
        assertEquals("33", "123123".delete("12"))
    }

    @Test
    fun deleteRange() {
        assertEquals("3123", "123123".delete(0,1))
    }

    @Test
    fun isEmail() {
        assertEquals(true,"1234@qq.com".isEmail)
        assertEquals(false,"1234qq.com".isEmail)
    }

    @Test
    fun isNumber() {
        assertEquals(true,"1234".isNumber)
        assertEquals(false,"1234qq.com".isEmail)
    }

    @Test
    fun isPhone() {
        assertEquals(true,"13143392307".isPhone)
        assertEquals(false,"1234qq.com".isPhone)
    }

    @Test
    fun isIdCard() {
    }

    @Test
    fun isChinese() {
    }

    @Test
    fun isEnglish() {
    }

}
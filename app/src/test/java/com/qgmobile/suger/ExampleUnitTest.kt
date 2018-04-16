package com.qgmobile.suger

import com.mobile.utils.MJsonMaker
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class,sdk = intArrayOf(19))
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        MJsonMaker.make {
            "a"-"b"
            "c"-{"d"-"e"}
        }
    }
}

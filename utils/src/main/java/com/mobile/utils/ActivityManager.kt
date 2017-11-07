package com.mobile.utils

import android.app.Activity
import kotlin.reflect.KClass

/**
 * Created by jimji on 2017/9/16.
 */
object ActivityManager {
    private var time: Long = 0
    private val actList = mutableListOf<Activity>()

    @Synchronized
    fun remove(act: Activity) = actList.remove(act)

    @Synchronized
    fun add(act: Activity) = actList.add(act)

    @Synchronized
    fun kill(clazz: KClass<*>) {
        actList.forEach {
            if (it::class == clazz) it.finish()
        }
    }

    @Synchronized
    fun killAll() {
        actList.forEach { it.finish() }
        actList.clear()
    }

    /**
     * action为在结束所有活动前要做的事
     */
    fun doubleExit(delay: Long = 2000, title: String = "再按一次退出", action: () -> Unit = {}) {
        if (System.currentTimeMillis() - time > delay) {
            time = System.currentTimeMillis()
            title.toast()
        } else {
            action()
            killAll()
        }
    }

}
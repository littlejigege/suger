package com.mobile.utils

import android.app.Activity
import android.os.Looper
import android.provider.Settings
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
     * action为在结束应用前要做的事
     */
    fun doubleExit(delay: Long = 2000, title: String = "再按一次退出", action: () -> Unit = {}) {
        if (System.currentTimeMillis() - time > delay) {
            time = System.currentTimeMillis()
            title.toast()
        } else {
            action()
            //杀死当前进程
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
    /**
     * 知识点
     * app的crash可以通过Thread.setDefaultUncaughtExceptionHandler来处理
     *
     */

}
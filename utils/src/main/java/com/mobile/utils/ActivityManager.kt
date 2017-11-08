package com.mobile.utils

import android.app.Activity
import android.os.Looper
import android.provider.Settings
import kotlin.reflect.KClass

/**
 * Created by jimji on 2017/9/16.
 */
object ActivityManager : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (onCrashListener != null) {
            onCrashListener!!.onCrash(e)
        } else {
            showToast("检测到异常，程序即将退出")
            doAfter(2000) {
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(1)
            }
        }

    }

    private var time: Long = 0

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    interface OnCrashListener {
        fun onCrash(throwable: Throwable)
    }

    private var onCrashListener: OnCrashListener? = null
    /**
     * 设置奔溃监听，做善后处理
     */
    fun setOnCrashListener(onCrashListener: OnCrashListener) {
        this.onCrashListener = onCrashListener
    }

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
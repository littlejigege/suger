package com.mobile.utils

import android.provider.Contacts
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.ActorJob
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.SendChannel
import kotlinx.coroutines.experimental.channels.actor

/**
 * Created by jimji on 2017/10/26.
 */
//异步执行代码块后回到主线程执行UI代码块
fun <T> coroutine(block: suspend CoroutineScope.() -> T, uiBlock: suspend (T) -> Unit) {
    val deferred = async(context = CommonPool, start = CoroutineStart.LAZY, block = block)
    launch(UI) {
        uiBlock(deferred.await())
    }
}

//延时执行UI代码块,返回构造出来的JOB，可用于取消
fun doAfter(delay: Long, repeat: Int = 1, todo: () -> Unit) = launch(UI) { (1..repeat).forEach { delay(delay);todo() } }


/**
 * 下面三个方法的区别在于使用的Channel不同
 */

//构造一个在固定时间内只有第一次调用有效的ActorJOb
fun <T> newActorWithDelay(delay: Long, action: suspend (T) -> Unit): SendChannel<T> = actor(context = UI, start = CoroutineStart.LAZY) {
    for (event in channel) {
        action(event)
        delay(delay)
    }
}

//构造一个只接受最新调用的ActorJOb
fun <T> newActorTakeLastest(action: suspend (T) -> Unit): SendChannel<T> = actor(UI, Channel.CONFLATED) {
    for (event in channel) {
        action(event)
    }
}

//构造一个来多少接多少的ActorJOb
fun <T> newActorTakeAll(action: suspend (T) -> Unit): SendChannel<T> = actor(UI, Channel.UNLIMITED) {
    for (event in channel) {
        action(event)
    }
}

/**
 * run(CommonPool){}不返回Job，只是协程执行代码块
 */


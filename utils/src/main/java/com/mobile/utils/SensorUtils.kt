package com.mobile.utils

import android.graphics.drawable.GradientDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.View
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay

/**
 * Created by jimji on 2017/10/26.
 */
/**
 * 指南针，onChange传出当前方向与正北方向的夹角，正北为0度，顺时针增加
 */
class OrientationGetter(delay: Long = 0, onChange: suspend (Float) -> Unit) : SensorBase<Float>(delay, Sensor.TYPE_ORIENTATION, onChange) {
    override fun onSensorChanged(event: SensorEvent) {
        actor.offer(event.values[SensorManager.DATA_X])
    }
}

/**
 * 传感器基类，继承后重写onSensorChanged方法就可以
 * 非常便利的实现传感器的使用
 */
open class SensorBase<in T>(private val delay: Long = 0, private val sensorType: Int, private val action: suspend (T) -> Unit) : SensorEventListener {

    protected val actor = newActorWithDelay<T>(delay) { action(it) }

    private val sensor by lazy { sensorManager.getDefaultSensor(sensorType) }


    override fun onSensorChanged(event: SensorEvent) {

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    //默认采用游戏帧率
    fun start(rate: Int = SensorManager.SENSOR_DELAY_GAME) {
        sensorManager.registerListener(this, sensor, rate)
    }
}

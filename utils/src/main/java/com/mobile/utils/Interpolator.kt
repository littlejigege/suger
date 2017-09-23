package com.mobile.utils

import android.view.animation.Interpolator

/**
 * Created by jimji on 2017/9/23.
 */
//果冻回弹插值器
class SpringInterpolator(private val factor: Double) : Interpolator {
    constructor() : this(0.3)
    override fun getInterpolation(p0: Float): Float {
        return (Math.pow(2.0, (-5 * p0).toDouble()) * Math.sin((p0 - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
    }
}
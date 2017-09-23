package com.mobile.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IntDef
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by jimji on 2017/9/16.
 */
fun Activity.hideInputMethod() = window.peekDecorView()?.let { inputMethodManager.hideSoftInputFromWindow(window.peekDecorView().windowToken, 0) }

fun Activity.showInputMethod(v: EditText) {
    if (!v.requestFocus()) {
        throw Exception("EditText can't get focus")
    }
    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED)
}

//只作用于5.0以上,实现沉浸
fun Activity.fullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    }
}


/**
 * 设置状态栏黑色字体图标，
 * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
 * 注意：使用时去掉自带的actionbar，否则会变成黑字白底
 */
fun Activity.setStatusBarTextBlack(): Boolean {
    // 无语系列：ZTK C2016只能时间和电池图标变色。。。。
    if (DeviceUtils.isZTKC2016()) {
        return false
    }

    if (StatusBarType.my != StatusBarType.DEFAULT) {
        return setStatusBarLightMode(this, StatusBarType.my)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(this.window, true)) {
            StatusBarType.my = StatusBarType.MIUI
            return true
        } else if (FlymeSetStatusBarLightMode(this.window, true)) {
            StatusBarType.my = StatusBarType.FLYME
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Android6SetStatusBarLightMode(this.window, true)
            StatusBarType.my = StatusBarType.ANDROID6
            return true
        }
    }
    return false
}

/**
 * 设置状态栏白色字体图标，
 * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
 */
fun Activity.setStatusBarTextWhite(): Boolean {
    if (StatusBarType.my == StatusBarType.DEFAULT) {
        // 默认状态，不需要处理
        return true
    }
    return when {
        StatusBarType.my == StatusBarType.MIUI -> MIUISetStatusBarLightMode(this.window, false)
        StatusBarType.my == StatusBarType.FLYME -> FlymeSetStatusBarLightMode(this.window, false)
        StatusBarType.my == StatusBarType.ANDROID6 -> Android6SetStatusBarLightMode(this.window, false)
        else -> true
    }
}


private fun setStatusBarLightMode(activity: Activity, type: Int): Boolean {
    return when (type) {
        StatusBarType.MIUI -> MIUISetStatusBarLightMode(activity.window, true)
        StatusBarType.FLYME -> FlymeSetStatusBarLightMode(activity.window, true)
        StatusBarType.ANDROID6 -> Android6SetStatusBarLightMode(activity.window, true)
        else -> false
    }
}

private fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
    var result = false
    if (window != null) {
        val clazz = window.javaClass
        try {
            val darkModeFlag: Int
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (dark) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
            }
            result = true
        } catch (ignored: Exception) {

        }

    }
    return result
}

private fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
    var result = false
    if (window != null) {
        try {
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            result = true
        } catch (ignored: Exception) {

        }

    }
    return result
}


@TargetApi(23)
private fun Android6SetStatusBarLightMode(window: Window, light: Boolean): Boolean {
    val decorView = window.decorView
    var systemUi = if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    systemUi = changeStatusBarModeRetainFlag(window, systemUi)
    decorView.systemUiVisibility = systemUi
    return true
}

@TargetApi(23)
private fun changeStatusBarModeRetainFlag(window: Window, out: Int): Int {
    var out = out
    out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN)
    out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    return out
}

private fun retainSystemUiFlag(window: Window, out: Int, type: Int): Int {
    var out = out
    val now = window.decorView.systemUiVisibility
    if (now and type == type) {
        out = out or type
    }
    return out
}

private fun isMIUICustomStatusBarLightModeImpl(): Boolean {
    return DeviceUtils.MIUIVersion in 5..8
}

private object StatusBarType {
    val DEFAULT = 0
    val MIUI = 1
    val FLYME = 2
    val ANDROID6 = 3 // Android 6.0
    var my = DEFAULT
}



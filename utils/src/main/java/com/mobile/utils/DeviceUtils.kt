package com.mobile.utils

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import com.mobile.utils.Utils.Companion.app
import java.io.File
import java.io.FileInputStream
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

/**
 * Created by jimji on 2017/9/16.
 */
object DeviceUtils {
    private val TAG = "QMUIDeviceHelper"
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private val FLYME = "flyme"
    private val ZTEC2016 = "zte c2016"
    private val ZUKZ1 = "zuk z1"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    private var sIsTabletChecked = false
    private var sIsTabletValue = false

    init {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
            val properties = Properties()
            properties.load(fileInputStream)
            val clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)

        } catch (e: Exception) {

        } finally {
            fileInputStream?.saveClose()
        }
    }

    private fun _isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 判断是否为平板设备
     */
    fun isTablet(context: Context): Boolean {
        if (sIsTabletChecked) {
            return sIsTabletValue
        }
        sIsTabletValue = _isTablet(context)
        sIsTabletChecked = true
        return sIsTabletValue
    }

    /**
     * 判断是否是flyme系统
     */
    fun isFlyme(): Boolean {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains(FLYME)
    }

    /**
     * 判断是否是MIUI系统
     */
    fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(sMiuiVersionName)
    }

    fun isMIUIV(version: Int): Boolean {
        return "v$version" == sMiuiVersionName
    }

    val MIUIVersion: Int
        get() {
            return try {
                sMiuiVersionName?.delete("v")?.toInt() ?: -1
            } catch (e: Exception) {
                -1
            }
        }

    fun isFlymeVersionHigher5_2_4(): Boolean {
        //查不到默认高于5.2.4
        var isHigher = true
        if (sFlymeVersionName != null && sFlymeVersionName != "") {
            val pattern = Pattern.compile("(\\d+\\.){2}\\d")
            val matcher = pattern.matcher(sFlymeVersionName!!)
            if (matcher.find()) {
                val versionString = matcher.group()
                if (versionString != null && versionString != "") {
                    val version = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (version.size == 3) {
                        if (Integer.valueOf(version[0]) < 5) {
                            isHigher = false
                        } else if (Integer.valueOf(version[0]) > 5) {
                            isHigher = true
                        } else {
                            if (Integer.valueOf(version[1]) < 2) {
                                isHigher = false
                            } else if (Integer.valueOf(version[1]) > 2) {
                                isHigher = true
                            } else {
                                if (Integer.valueOf(version[2]) < 4) {
                                    isHigher = false
                                } else if (Integer.valueOf(version[2]) >= 5) {
                                    isHigher = true
                                }
                            }
                        }
                    }

                }
            }
        }
        return isMeizu() && isHigher
    }

    /**
     * 判断是否为魅族
     */
    fun isMeizu(): Boolean {
        return isPhone(MEIZUBOARD) || isFlyme()
    }

    /**
     * 判断是否为小米
     */
    fun isXiaomi(): Boolean {
        return Build.BRAND.toLowerCase().contains("xiaomi")
    }


    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    fun isZUKZ1(): Boolean {
        val board = android.os.Build.MODEL
        return board != null && board.toLowerCase().contains(ZUKZ1)
    }

    fun isZTKC2016(): Boolean {
        val board = android.os.Build.MODEL
        return board != null && board.toLowerCase().contains(ZTEC2016)
    }

    private fun isPhone(boards: Array<String>): Boolean {
        val board = android.os.Build.BOARD ?: return false
        return boards.contains(board)
    }

    /**
     * 判断悬浮窗权限（目前主要用户魅族与小米的检测）。
     */
    fun isFloatWindowOpAllowed(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24)  // 24 是AppOpsManager.OP_SYSTEM_ALERT_WINDOW 的值，该值无法直接访问
        } else {
            try {
                context.applicationInfo.flags and (1 shl 27) == 1 shl 27
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }

        }
    }

    @TargetApi(19)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val method = manager.javaClass.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                val property = method.invoke(manager, op,
                        Binder.getCallingUid(), context.packageName) as Int
                return AppOpsManager.MODE_ALLOWED == property
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return false
    }

    private fun getLowerCaseName(p: Properties, get: Method, key: String): String? {
        var name: String? = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: Exception) {
            }

        }
        if (name != null) name = name.toLowerCase()
        return name
    }
}



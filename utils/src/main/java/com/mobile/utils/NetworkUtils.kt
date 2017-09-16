package com.mobile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.mobile.utils.Utils.Companion.app

/**
 * Created by jimji on 2017/9/16.
 */
enum class NetworkType {
    NETWORK_WIFI,
    NETWORK_4G,
    NETWORK_3G,
    NETWORK_2G,
    NETWORK_UNKNOWN,
    NETWORK_NO
}

/**
 * 打开网络设置界面
 */
fun openWirelessSettings() {
    app.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

/**
 * 获取活动网络信息
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 *
 * @return NetworkInfo
 */
@SuppressLint("MissingPermission")
private fun getActiveNetworkInfo(): NetworkInfo? {
    return (app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
}

/**
 * 判断网络是否连接
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 *
 * @return `true`: 是<br></br>`false`: 否
 */
fun isConnected(): Boolean {
    val info = getActiveNetworkInfo()
    return info != null && info.isConnected
}

///**
// * 判断网络是否可用
// *
// * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
// *
// * 需要异步ping，如果ping不通就说明网络不可用
// *
// * ping的ip为阿里巴巴公共ip：223.5.5.5
// *
// * @return `true`: 可用<br></br>`false`: 不可用
// */
//fun isAvailableByPing(): Boolean {
//    return isAvailableByPing(null)
//}
//
///**
// * 判断网络是否可用
// *
// * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
// *
// * 需要异步ping，如果ping不通就说明网络不可用
// *
// * @param ip ip地址（自己服务器ip），如果为空，ip为阿里巴巴公共ip
// * @return `true`: 可用<br></br>`false`: 不可用
// */
//fun isAvailableByPing(ip: String?): Boolean {
//    var ip = ip
//    if (ip == null || ip.length <= 0) {
//        ip = "223.5.5.5"// 阿里巴巴公共ip
//    }
//    val result = ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false)
//    val ret = result.result === 0
//    if (result.errorMsg != null) {
//        Log.d("NetworkUtils", "isAvailableByPing() called" + result.errorMsg)
//    }
//    if (result.successMsg != null) {
//        Log.d("NetworkUtils", "isAvailableByPing() called" + result.successMsg)
//    }
//    return ret
//}

/**
 * 判断移动数据是否打开
 *
 * @return `true`: 是<br></br>`false`: 否
 */
fun getDataEnabled(): Boolean {
    try {
        val tm = app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
        if (null != getMobileDataEnabledMethod) {
            return getMobileDataEnabledMethod.invoke(tm) as Boolean
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return false
}





/**
 * 判断wifi是否打开
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
 *
 * @return `true`: 是<br></br>`false`: 否
 */
fun getWifiEnabled(): Boolean {
    @SuppressLint("WifiManagerLeak")
    val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.isWifiEnabled
}





///**
// * 判断wifi数据是否可用
// *
// * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
// *
// * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
// *
// * @return `true`: 是<br></br>`false`: 否
// */
//fun isWifiAvailable(): Boolean {
//    return getWifiEnabled() && isAvailableByPing()
//}



private val NETWORK_TYPE_GSM = 16
private val NETWORK_TYPE_TD_SCDMA = 17
private val NETWORK_TYPE_IWLAN = 18

/**
 * 获取当前网络类型
 *
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 *
 * @return 网络类型
 *
 *  * [NetworkUtils.NetworkType.NETWORK_WIFI]
 *  * [NetworkUtils.NetworkType.NETWORK_4G]
 *  * [NetworkUtils.NetworkType.NETWORK_3G]
 *  * [NetworkUtils.NetworkType.NETWORK_2G]
 *  * [NetworkUtils.NetworkType.NETWORK_UNKNOWN]
 *  * [NetworkUtils.NetworkType.NETWORK_NO]
 *
 */
fun getNetworkType(): NetworkType {
    var netType = NetworkType.NETWORK_NO
    val info = getActiveNetworkInfo()
    if (info != null && info.isAvailable) {

        if (info.type == ConnectivityManager.TYPE_WIFI) {
            netType = NetworkType.NETWORK_WIFI
        } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
            when (info.subtype) {

                NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> netType = NetworkType.NETWORK_2G

                NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> netType = NetworkType.NETWORK_3G

                NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> netType = NetworkType.NETWORK_4G
                else -> {

                    val subtypeName = info.subtypeName
                    if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                        netType = NetworkType.NETWORK_3G
                    } else {
                        netType = NetworkType.NETWORK_UNKNOWN
                    }
                }
            }
        } else {
            netType = NetworkType.NETWORK_UNKNOWN
        }
    }
    return netType
}
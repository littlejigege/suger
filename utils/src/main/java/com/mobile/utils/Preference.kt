package com.mobile.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

@SuppressLint("StaticFieldLeak")
/**
 * 需要在preference中存取的类（除基本类型以外）必须实现序列化接口
 * Created by jimji on 2017/9/15.
 */
object Preference {
    private fun getEdit(name: String): SharedPreferences.Editor = Utils.app.getSharedPreferences(name, Context.MODE_PRIVATE).edit()

    private fun getPreference(name: String): SharedPreferences = Utils.app.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun save(name: String, pairsToSave: AnyPairs.() -> Unit) {
        val ap = AnyPairs()
        ap.pairsToSave()
        val map = ap.map
        with(getEdit(name)) {
            map.forEach { entry ->
                when (entry.value) {
                    is String -> putString(entry.key, entry.value as String)
                    is Int -> putInt(entry.key, entry.value as Int)
                    is Long -> putLong(entry.key, entry.value as Long)
                    is Float -> putFloat(entry.key, entry.value as Float)
                    is Boolean -> putBoolean(entry.key, entry.value as Boolean)
                    else -> {
                        val byteOS = ByteArrayOutputStream()
                        val objOS = ObjectOutputStream(byteOS)
                        objOS.writeObject(entry.value)
                        putString(entry.key, Base64.encodeToString(byteOS.toByteArray(), 1))
                    }
                }
            }
            apply()
        }

    }

    fun get(name: String, keyAndDefault: Pair<String, Any>): Any {
        var result = Any()
        with(getPreference(name)){
            result = when (keyAndDefault.second) {
                is String -> getString(keyAndDefault.first, keyAndDefault.second as String)
                is Int -> getInt(keyAndDefault.first, keyAndDefault.second as Int)
                is Long -> getLong(keyAndDefault.first, keyAndDefault.second as Long)
                is Float -> getFloat(keyAndDefault.first, keyAndDefault.second as Float)
                is Boolean -> getBoolean(keyAndDefault.first, keyAndDefault.second as Boolean)
                else -> {
                    val objIS = ObjectInputStream(ByteArrayInputStream(Base64.decode(getString(keyAndDefault.first, ""), 1)))
                    objIS.readObject()
                }
            }
        }

        return result
    }

    class AnyPairs {
        val map = mutableMapOf<String, Any>()
        operator fun String.minus(any: Any) {
            map.put(this, any)
        }
    }
}

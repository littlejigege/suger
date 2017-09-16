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
        val edit = getEdit(name)
        map.forEach { entry ->
            when (entry.value) {
                is String -> {
                    edit.putString(entry.key, entry.value as String)
                }
                is Int -> {
                    edit.putInt(entry.key, entry.value as Int)
                }
                is Long -> {
                    edit.putLong(entry.key, entry.value as Long)
                }
                is Float -> {
                    edit.putFloat(entry.key, entry.value as Float)
                }
                is Boolean -> {
                    edit.putBoolean(entry.key, entry.value as Boolean)
                }
                else -> {
                    val byteOS = ByteArrayOutputStream()
                    val objOS = ObjectOutputStream(byteOS)
                    objOS.writeObject(entry.value)
                    edit.putString(entry.key, Base64.encodeToString(byteOS.toByteArray(), 1))
                }
            }
        }
        edit.apply()
    }

    fun get(name: String, keyAndDefault: Pair<String, Any>): Any {
        val preference = getPreference(name)
        var result = Any()
        when (keyAndDefault.second) {
            is String -> {
                result = preference.getString(keyAndDefault.first, keyAndDefault.second as String)
            }
            is Int -> {
                result = preference.getInt(keyAndDefault.first, keyAndDefault.second as Int)
            }
            is Long -> {
                result = preference.getLong(keyAndDefault.first, keyAndDefault.second as Long)
            }
            is Float -> {
                result = preference.getFloat(keyAndDefault.first, keyAndDefault.second as Float)
            }
            is Boolean -> {
                result = preference.getBoolean(keyAndDefault.first, keyAndDefault.second as Boolean)
            }
            else -> {
                val objIS = ObjectInputStream(ByteArrayInputStream(Base64.decode(preference.getString(keyAndDefault.first, ""), 1)))
                result = objIS.readObject()
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

package com.mobile.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by jimji on 2017/10/26.
 */
/**prefrence委托，方便将属性持久化*/
fun <T> preference(name: String, pair: Pair<String, Any>): ReadWriteProperty<Any, T> = ByPreference(name, pair)

@Suppress("UNCHECKED_CAST")
private class ByPreference<T>(val name: String, val pair: Pair<String, Any>) : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T = Preference.get(name, pair) as T
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = Preference.save(name) { pair.first - value as Any }
}
package com.mobile.utils

import com.google.gson.*
import org.json.JSONObject
import java.lang.IllegalArgumentException

/**
 * Created by jimji on 2017/9/17.
 */
class JsonWrapper {
    //构建json对象
    fun objects(makePairs: JsonPairs.() -> Unit): JsonObject {
        val jp = JsonPairs()
        val json = JsonObject()
        jp.makePairs()
        jp.pairs.forEach { entry ->
            run {
                when (entry.value) {
                    is JsonElement -> json.add(entry.key, entry.value as JsonElement)

                    is String -> json.add(entry.key, JsonPrimitive(entry.value as String))

                    is Number -> json.add(entry.key, JsonPrimitive(entry.value as Number))

                    is Boolean -> json.add(entry.key, JsonPrimitive(entry.value as Boolean))

                    is Char -> json.add(entry.key, JsonPrimitive(entry.value as Char))

                }

            }
        }
        return json
    }

    //构建json数组
    //在API version 1.01中已弃用，请使用下面的方法来构造数组
    fun arrays(vararg objects: JsonObject): JsonArray {
        val array = JsonArray()
        objects.forEach { array.add(it) }
        return array
    }

    fun JsonElement.toJsonArray(size: Int): JsonArray {
        if (size <= 0) throw IllegalArgumentException("size should bigger than 0")

        val array = JsonArray()
        (0 until size).forEach { array.add(this) }
        return array
    }
}

object JsonMaker {
    fun make(makeJson: JsonWrapper.() -> JsonElement): String {
        val w = JsonWrapper()
        return Gson().toJson(w.makeJson())
    }
}

class JsonPairs {
    var pairs: MutableMap<String, Any> = HashMap()
    operator fun String.minus(value: Any) {
        pairs.put(this, value)
    }
}
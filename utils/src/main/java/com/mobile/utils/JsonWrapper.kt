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



class JsonPairs {
    var pairs: MutableMap<String, Any> = HashMap()
    operator fun String.minus(value: Any) {
        pairs.put(this, value)

    }

}


class JsonMaker {
    companion object {

        fun array(vararg elements: () -> Unit): Array<() -> Unit> {
            return Array(elements.size) { i -> elements[i] }
        }

        fun array(vararg elements: Number) : Array<Number> = Array(elements.size) { i -> elements[i] }

        fun array(vararg elements: String) = Array(elements.size) { i -> elements[i] }

        @Deprecated("")
        fun make(makeJson: JsonWrapper.() -> JsonElement): String {
            val w = JsonWrapper()
            return Gson().toJson(w.makeJson())
        }

        fun create(makePair: JsonPairs.() -> Unit): String {
            val json = JsonObject()
            val pairs = JsonPairs()
            pairs.makePair()
            process(json, pairs.pairs)
            return json.toString()
        }

        /**
         * @param key 当前要在key下创建json OBJECT
         * @param array 用于创建key - array
         * @param jsonObj 在当前的jsonObj 创建上面的参数
         *
         */
        fun process(jsonObj: JsonObject, pairs: MutableMap<String, Any>, key: String? = null, array: (Array<() -> Unit>)? = null) {

            var i = 0
            var json = jsonObj
            val mapper: MutableMap<String, () -> Unit> by lazy { hashMapOf<String, () -> Unit>() }
            val objArrayMapper: MutableMap<String, Array<() -> Unit>> by lazy { hashMapOf<String, Array<() -> Unit>>() }
            val jsonArray: JsonArray by lazy { JsonArray() }

            //当key不为空,则需要在当前jsonObj下创建key,并在key下创建OBJECT或ARRAY
            //否则,直接根据pairs在当前jsonObj下创建 key - value
            if (key != null) {
                json = JsonObject()

                //若传入array为空,则在当前jsonObj下创建key - jsonOBJECT ,并加载数据到json中
                if (array == null) {
                    jsonObj.add(key, json)
                }

                //若传入array不为空,当前jsonObj下创建key - jsonARRAY ,并循环加载数据到jsonARRAY中
                if (array != null) {
                    jsonObj.add(key, jsonArray)
                }
            }


            do {

                if (array != null) {
                    //若是要加入array,则循环读取array的内容,并加入到json中
                    pairs.clear()
                    array[i].invoke()
                    json = JsonObject()
                    jsonArray.add(json)
                }

                pairs.forEach { entry ->

                    when (entry.value) {
                        is JsonElement -> json.add(entry.key, entry.value as JsonElement)

                        is String -> json.add(entry.key, JsonPrimitive(entry.value as String))

                        is Number -> json.add(entry.key, JsonPrimitive(entry.value as Number))

                        is Boolean -> json.add(entry.key, JsonPrimitive(entry.value as Boolean))

                        is Char -> json.add(entry.key, JsonPrimitive(entry.value as Char))

                        is Function<*> -> kotlin.run {
                            mapper.put(entry.key, (entry.value as () -> Unit))
                        }

                        is Array<*> -> run {
                            //尝试强转
                            if (Try { entry.value as Array<() -> Unit> }) {
                                objArrayMapper.put(entry.key, (entry.value as Array<() -> Unit>))
                            } else if (Try { (entry.value as Array<Any>) }) {
                                val tempArray = JsonArray()
                                (entry.value as Array<Any>).forEach {
                                    when (it) {
                                        is Number -> tempArray.add(it)
                                        is Boolean -> tempArray.add(it)
                                        is Char -> tempArray.add(it)
                                        is JsonPrimitive -> tempArray.add(it)
                                        is String -> tempArray.add(it)
                                    }
                                }
                                json.add(entry.key, tempArray)
                            } else {
                                throw RuntimeException("不支持的JSON格式")
                            }
                        }
                    }

                }

                i += 1
            } while (array != null && i < array.size)


            pairs.clear()

            for ((k, v) in mapper) {
                v.invoke()
                process(json, pairs, k)
            }

            pairs.clear()

            for ((k, v) in objArrayMapper) {
                process(json, pairs, k, v)
            }


        }

        private fun Try(run: () -> Unit): Boolean {
            try {
                run.invoke()
                return true
            } catch (e: Exception) {
                return false
            }
        }
    }



}
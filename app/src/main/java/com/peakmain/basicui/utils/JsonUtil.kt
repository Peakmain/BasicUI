package com.peakmain.basicui.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.peakmain.basicui.App
import com.peakmain.ui.utils.LogUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.*

/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：解析二次封装(以便后期方便更好解析框架替换和重构)
 */
object JsonUtil {
    private var gson: Gson? = null

    /**
     * 将string 解析成实体类
     *
     * @param jsonString 需要解析String
     * @param typeOfT    实体类类型或者Type类型
     *
     *
     * return T 解析后实体对象
     */
    fun <T> parseObject(jsonString: String, typeOfT: Type?): T? {
        LogUtils.d("JsonUtil", "jsonString:$jsonString")
        var t: T? = null
        try {
            if (jsonString == "null" || TextUtils.isEmpty(jsonString)) {
                LogUtils.e("jsonString 为空,不能解析")
            } else {
                t = gson!!.fromJson(jsonString, typeOfT)
            }
        } catch (e: Exception) {
            LogUtils.e("json解析异常" + e.message)
        }
        return t
    }

    /**
     * 将string 解析成 ArrList
     *
     * @param jsonString 需要解析String
     * @param cls        实体类
     * @return List <T> 解析后实体list
    </T> */
    fun <T> parseArrList(jsonString: String, cls: Class<T>?): List<T> {
        val list: MutableList<T> = ArrayList()
        try {
            if (jsonString == "null" || TextUtils.isEmpty(jsonString)) {
                LogUtils.e("jsonString 为空,不能解析")
            } else {
                val array = JsonParser().parse(jsonString).asJsonArray
                for (elem in array) {
                    list.add(Gson().fromJson(elem, cls))
                }
            }
        } catch (e: Exception) {
            LogUtils.e("json解析异常" + e.message)
        }
        return list
    }

    /**
     * 将json的外层剥离
     *
     * @param jsonString json
     * @param str        节点
     * @return
     */
    fun getJsonString(jsonString: String?, str: String?): String {
        try {
            val obj = JSONObject(jsonString)
            if (obj != null && obj.has(str)) {
                return obj.opt(str).toString()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 将object 转成 string
     *
     * @param jsonString 需要解析String
     * return jsonStrin
     */
    fun toJsonString(jsonString: String?): JsonObject? {
        var jsonStrin: JsonObject? = null
        if (jsonString == null) {
            LogUtils.e("jsonString 为空,不能解析")
        } else {
            jsonStrin = JsonParser().parse(jsonString).asJsonObject
        }
        return jsonStrin
    }

    /**
     * string解析成jsonObject
     */
    fun stringToJson(jsonStr: String?): JsonObject? {
        if (jsonStr == null) {
            LogUtils.e("jsonString 为空,不能解析")
            return null
        }
        return JsonParser().parse(jsonStr).asJsonObject
    }

    /**
     * 解析完成data
     */
    fun jsonData(jsonStr: String?): JsonObject? {
        var objStr: JsonObject? = null
        if (jsonStr == null) {
            LogUtils.e("jsonString 为空,不能解析")
            return null
        }
        try {
            objStr = JsonParser().parse(jsonStr).asJsonObject.asJsonObject.getAsJsonObject("data")
        } catch (e: Exception) {
            LogUtils.e(e.message)
        }
        return objStr
    }

    /**
     * 将对象转换为字符串
     *
     * @param object
     * @return
     */
    fun toJsonString(`object`: Any?): String? {
        var jsonstr: String? = null
        if (gson != null) {
            jsonstr = gson!!.toJson(`object`)
        }
        return jsonstr
    }

    /**
     * 将string 解析成实体类
     *
     * @param jsonString 需要解析String
     * @param cls        实体类
     *
     *
     * return T 解析后实体对象
     */
    fun <T> getObject(jsonString: String, cls: Class<T>?): T? {
        var t: T? = null
        try {
            if (jsonString == "null" || TextUtils.isEmpty(jsonString)) {
                LogUtils.e("jsonString 为空,不能解析")
            } else {
                val gson = Gson()
                t = gson.fromJson(jsonString, cls)
            }
        } catch (e: Exception) {
            LogUtils.e("json解析异常" + e.localizedMessage)
        }
        return t
    }

    /**
     * 比较两个json 是否相等 (包含顺序)
     *
     * @param json1
     * @param json2
     * @return
     */
    fun equalsJsonTree(json1: String?, json2: String?): Boolean {
        return if (gson != null) {
            try {
                val js1 = gson!!.toJsonTree(json1)
                val js2 = gson!!.toJsonTree(json2)
                js1 == js2
            } catch (e: Exception) {
                LogUtils.e("json解析异常" + e.message)
                false
            }
        } else false
    }

    /**
     * 获取Assets文件下的json数据
     *
     * @param fileName 文件名
     */
    fun getAssetsJson(fileName: String): String {
        //将json数据变成字符串
        val stringBuilder = StringBuilder()
        try {
            //获取assets资源管理器
            val assetManager = App.app!!.assets
            //通过管理器打开文件并读取
            val bf = BufferedReader(InputStreamReader(
                    assetManager.open(fileName)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    init {
        if (gson == null) {
            gson = Gson()
        }
    }
}
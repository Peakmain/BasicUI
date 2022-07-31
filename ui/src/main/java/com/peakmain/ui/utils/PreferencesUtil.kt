package com.peakmain.ui.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import com.peakmain.ui.constants.BasicUIUtils
import java.io.*
import java.text.SimpleDateFormat

/**
 * author ：Peakmain
 * createTime：2020/7/23
 * mail:2726449200@qq.com
 * describe：SharedPreferences常用封装工具类
 */
class PreferencesUtil private constructor() {
    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var `object`: Any? = null

    /**
     * 保存数据 , 所有的类型都适用
     *
     * @param key    key
     * @param object object
     */
    @Synchronized
    fun saveParam(key: String?, `object`: Any) {
        if (editor == null) editor = preferences!!.edit()
        // 得到object的类型
        val type = `object`.javaClass.simpleName
        when (type) {
            "String" -> {
                // 保存String 类型
                editor!!.putString(key, `object` as String)
            }
            "Integer" -> {
                // 保存integer 类型
                editor!!.putInt(key, (`object` as Int))
            }
            "Boolean" -> {
                // 保存 boolean 类型
                editor!!.putBoolean(key, (`object` as Boolean))
            }
            "Float" -> {
                // 保存float类型
                editor!!.putFloat(key, (`object` as Float))
            }
            "Long" -> {
                // 保存long类型
                editor!!.putLong(key, (`object` as Long))
            }
            else -> {
                require(`object` is Serializable) { `object`.javaClass.name + " 必须实现Serializable接口!" }

                // 不是基本类型则是保存对象
                val baos = ByteArrayOutputStream()
                try {
                    val oos = ObjectOutputStream(baos)
                    oos.writeObject(`object`)
                    val productBase64 = Base64.encodeToString(
                        baos.toByteArray(), Base64.DEFAULT)
                    editor!!.putString(key, productBase64)
                    Log.d(this.javaClass.simpleName, "save object success")
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(this.javaClass.simpleName, "save object error")
                }
            }
        }
        editor!!.apply()
    }

    /**
     * 移除信息
     */
    @Synchronized
    fun remove(key: String?) {
        if (editor == null) editor = preferences!!.edit()
        editor!!.remove(key).apply()
    }

    @Synchronized
    fun clear() {
        if (editor == null) editor = preferences!!.edit()
        editor?.clear()?.apply()
    }

    /**
     * 是否已经包含key
     */
    @Synchronized
    fun contains(key: String?): Boolean {
        return preferences!!.contains(key)
    }

    /**
     * 获取SharedPreferences所有的value
     */
    fun getAll(): Map<String?, *>? {
        return preferences!!.all
    }

    /**
     * 得到保存数据的方法，所有类型都适用
     *
     * @param key           key
     * @param defaultObject 默认值
     * @return 数据
     */
    fun getParam(key: String?, defaultObject: Any?): Any? {
        if (defaultObject == null) {
            return getObject(key)
        }
        when (defaultObject.javaClass.simpleName) {
            "String" -> {
                return preferences!!.getString(key, defaultObject as String?)
            }
            "Integer" -> {
                return preferences!!.getInt(key, (defaultObject as Int?)!!)
            }
            "Boolean" -> {
                return preferences!!.getBoolean(key, (defaultObject as Boolean?)!!)
            }
            "Float" -> {
                return preferences!!.getFloat(key, (defaultObject as Float?)!!)
            }
            "Long" -> {
                return preferences!!.getLong(key, (defaultObject as Long?)!!)
            }
            else -> return getObject(key)
        }
    }

    /**
     * Whether to use for the first time
     */
    /**
     * set user first use is false
     */
    var isFirst: Boolean
        get() = getParam("isFirst", true) as Boolean
        set(isFirst) {
            saveParam("isFirst", isFirst)
        }

    private val date: String
        get() {
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("MM-dd HH:mm")
            return sdf.format(System.currentTimeMillis())
        }

    fun saveRefreshTime() {
        saveParam("REFRESH_TIME", date)
    }

    val refreshTime: String?
        get() = getParam("REFRESH_TIME", date) as String?

    fun getObject(key: String?): Any? {
        val wordBase64 = preferences!!.getString(key, "") ?: return null
        val base64 = Base64.decode(wordBase64.toByteArray(), Base64.DEFAULT)
        val bais = ByteArrayInputStream(base64)
        try {
            val bis = ObjectInputStream(bais)
            `object` = bis.readObject()
            Log.d(this.javaClass.simpleName, "Get object success")
            return `object`
        } catch (e: Exception) {
        }
        Log.e(this.javaClass.simpleName, "Get object is error")
        return null
    }

    companion object {

        // 使用双重同步锁
        @JvmStatic
        val instance: PreferencesUtil by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            PreferencesUtil()
        }
    }

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(BasicUIUtils.application?.applicationContext)
    }
}
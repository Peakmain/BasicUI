package com.peakmain.ui.utils.database

import android.text.TextUtils
import java.util.*

class DaoUtil private constructor() {
    companion object {
        /**
         * 获得类名
         */
        @JvmStatic
        fun getTableName(clazz: Class<*>): String {
            return clazz.simpleName
        }

        /**
         * 数据库操作的时候根据类型进行转换
         */
        fun getColumnType(type: String): String? {
            var value: String? = null
            if (type.contains("String")) {
                value = " text"
            } else if (type.contains("int")) {
                value = " integer"
            } else if (type.contains("boolean")) {
                value = " boolean"
            } else if (type.contains("float")) {
                value = " float"
            } else if (type.contains("double")) {
                value = " double"
            } else if (type.contains("char")) {
                value = " varchar"
            } else if (type.contains("long")) {
                value = " long"
            }
            return value
        }

        /**
         * 查询数据：cursor.getInt();将int第一个字符进行转换成大写
         */
        @JvmStatic
        fun capitalize(string: String?): String? {
            if (!TextUtils.isEmpty(string)) {
                return string!!.substring(0, 1).toUpperCase(Locale.US) + string.substring(1)
            }
            return if (string == null) null else ""
        }
    }

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }
}
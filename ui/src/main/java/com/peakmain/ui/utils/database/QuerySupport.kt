package com.peakmain.ui.utils.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.peakmain.ui.utils.database.DaoUtil.Companion.capitalize
import com.peakmain.ui.utils.database.DaoUtil.Companion.getTableName
import java.lang.reflect.Method
import java.util.*

class QuerySupport<T>(private val mSQLiteDatabase: SQLiteDatabase, private val mClass: Class<T>) {
    // 查询的列
    private lateinit var mQueryColumns: Array<String>

    // 查询的条件
    private var mQuerySelection: String? = null

    // 查询的参数
    private lateinit var mQuerySelectionArgs: Array<String>

    // 查询分组
    private var mQueryGroupBy: String? = null

    // 查询对结果集进行过滤
    private var mQueryHaving: String? = null

    // 查询排序
    private var mQueryOrderBy: String? = null

    // 查询可用于分页
    private var mQueryLimit: String? = null
    fun columns(vararg columns: String): QuerySupport<*> {
        mQueryColumns = columns as Array<String>
        return this
    }

    fun selectionArgs(vararg selectionArgs: String): QuerySupport<*> {
        mQuerySelectionArgs = selectionArgs as Array<String>
        return this
    }

    fun having(having: String?): QuerySupport<*> {
        mQueryHaving = having
        return this
    }

    fun orderBy(orderBy: String?): QuerySupport<*> {
        mQueryOrderBy = orderBy
        return this
    }

    fun limit(limit: String?): QuerySupport<*> {
        mQueryLimit = limit
        return this
    }

    fun groupBy(groupBy: String?): QuerySupport<*> {
        mQueryGroupBy = groupBy
        return this
    }

    fun selection(selection: String?): QuerySupport<*> {
        mQuerySelection = selection
        return this
    }

    fun query(): List<T> {
        val cursor = mSQLiteDatabase.query(getTableName(mClass), mQueryColumns, mQuerySelection
                , mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy)
        clearQueryParams()
        return cursorToList(cursor)
    }

    fun queryAll(): List<T> {
        val cursor = mSQLiteDatabase.query(getTableName(mClass), null, null, null, null, null, null)
        return cursorToList(cursor)
    }

    /**
     * 通过Cursor封装成查找对象
     *
     * @return 对象集合列表
     */
    private fun cursorToList(cursor: Cursor?): List<T> {
        val list: MutableList<T> = ArrayList()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    val instance = mClass.newInstance()
                    val fields = mClass.declaredFields
                    for (field in fields) {
                        // 遍历属性
                        field.isAccessible = true
                        val name = field.name
                        //cursor.getInt(0);
                        // 获取角标
                        val index = cursor.getColumnIndex(name)
                        if (index == -1) {
                            continue
                        }
                        // 通过反射获取 游标的方法
                        val cursorMethod = cursorMethod(field.type)
                        if (cursorMethod != null) {
                            var value: Any? = cursorMethod.invoke(cursor, index) ?: continue

                            // 处理一些特殊的部分
                            if (field.type == Boolean::class.javaPrimitiveType || field.type == Boolean::class.java) {
                                if ("0" == value.toString()) {
                                    value = false
                                } else if ("1" == value.toString()) {
                                    value = true
                                }
                            } else if (field.type == Char::class.javaPrimitiveType || field.type == Char::class.java) {
                                value = (value as String)[0]
                            } else if (field.type == Date::class.java) {
                                val date = value as Long
                                value = if (date <= 0) {
                                    null
                                } else {
                                    Date(date)
                                }
                            }
                            field[instance] = value
                        }
                    }
                    // 加入集合
                    list.add(instance)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        cursor!!.close()
        return list
    }

    @Throws(Exception::class)
    private fun cursorMethod(type: Class<*>): Method {
        val methodName = getColumnMethodName(type)
        return Cursor::class.java.getMethod(methodName, Int::class.javaPrimitiveType)
    }

    private fun getColumnMethodName(fieldType: Class<*>): String {
        val typeName: String?
        typeName = if (fieldType.isPrimitive) { //是不是基本类型
            capitalize(fieldType.name) //Int
        } else {
            fieldType.simpleName //Integer
        }
        var methodName = "get$typeName"
        if ("getBoolean" == methodName) {
            methodName = "getInt"
        } else if ("getChar" == methodName || "getCharacter" == methodName) {
            methodName = "getString"
        } else if ("getDate" == methodName) {
            methodName = "getLong"
        } else if ("getInteger" == methodName) {
            methodName = "getInt"
        }
        return methodName
    }

    /**
     * 清空参数
     */
    private fun clearQueryParams() {
        mQueryColumns = emptyArray()
        mQuerySelection = null
        mQuerySelectionArgs = emptyArray()
        mQueryGroupBy = null
        mQueryHaving = null
        mQueryOrderBy = null
        mQueryLimit = null
    }

}
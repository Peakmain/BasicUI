package com.peakmain.ui.utils.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.ArrayMap
import com.peakmain.ui.utils.LogUtils
import java.lang.reflect.Method

class DaoSupport<T> : IDaoSupport<T> {
    private lateinit var mDatabase: SQLiteDatabase
    private lateinit var mClazz: Class<T>
    private val TAG = DaoSupport::class.java.simpleName
    private  var mQuerySupport: QuerySupport<T>?=null
    override fun init(sqLiteDatabase: SQLiteDatabase, clazz: Class<T>) {
        mDatabase = sqLiteDatabase
        mClazz = clazz
        val sb = StringBuffer()
        // 创建表
        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ")
        val fields = mClazz!!.declaredFields
        for (field in fields) {
            //设置权限
            field.isAccessible = true
            val name = field.name
            val type = field.type.simpleName
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ")
        }
        sb.replace(sb.length - 2, sb.length, ")")
        val createTableSql = sb.toString()
        LogUtils.d(createTableSql)
        //创建表
        mDatabase!!.execSQL(createTableSql)
    }

    override fun insert(t: T): Long {
        /*ContentValues values = new ContentValues();
        values.put("name",person.getName());
        values.put("age",person.getAge());
        values.put("flag",person.getFlag());
        db.insert("Person",null,values);*/
        val values = contentValuesByObj(t)
        return mDatabase!!.insert(DaoUtil.getTableName(mClazz), null, values)
    }

    private fun contentValuesByObj(obj: T): ContentValues {
        val values = ContentValues()
        val fields = mClazz!!.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                val key = field.name
                val value = field[obj]
                // put 第二个参数是类型  把它转换
                mPutMethodArgs[0] = key
                mPutMethodArgs[1] = value
                val filedTypeName = field.type.name
                // 还是使用反射  获取方法  put  缓存方法
                var putMethod = mPutMethods[filedTypeName]
                if (putMethod == null) {
                    putMethod = ContentValues::class.java.getDeclaredMethod("put",
                            String::class.java, value.javaClass)
                    mPutMethods[filedTypeName] = putMethod
                }
                // 通过反射执行
                putMethod!!.invoke(values, *mPutMethodArgs)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mPutMethodArgs[0] = null
                mPutMethodArgs[1] = null
            }
        }
        return values
    }

    override fun insert(datas: MutableList<T>) {
        mDatabase.beginTransaction()
        for (data in datas) {
            insert(data)
        }
        mDatabase.setTransactionSuccessful()
        mDatabase.endTransaction()
    }

    override fun querySupport(): QuerySupport<T> {
        if (mQuerySupport == null) {
            mQuerySupport = QuerySupport(mDatabase, mClazz)
        }
        return mQuerySupport!!
    }

    override fun delete(whereClause: String?, vararg whereArgs: String?): Int {
        return mDatabase!!.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs)
    }

    override fun update(obj: T, whereClause: String, vararg whereArgs: String): Int {
        val values = contentValuesByObj(obj)
        return mDatabase!!.update(DaoUtil.getTableName(mClazz), values, whereClause, whereArgs)
    }

    companion object {
        private val mPutMethodArgs = arrayOfNulls<Any>(2)
        private val mPutMethods: MutableMap<String, Method?> = HashMap()
    }



}
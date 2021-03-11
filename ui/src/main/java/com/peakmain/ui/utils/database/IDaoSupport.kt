package com.peakmain.ui.utils.database

import android.database.sqlite.SQLiteDatabase

interface IDaoSupport<T> {
    fun init(sqLiteDatabase: SQLiteDatabase?, clazz: Class<T>?)

    //插入数据
    fun insert(t: T): Long

    //批量插入
    fun insert(datas: List<T>?)
    fun querySupport(): QuerySupport<T>?
    fun delete(whereClause: String?, vararg whereArgs: String?): Int
    fun update(obj: T, whereClause: String?, vararg whereArgs: String?): Int
}
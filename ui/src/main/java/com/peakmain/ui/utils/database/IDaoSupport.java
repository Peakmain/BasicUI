package com.peakmain.ui.utils.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDaoSupport<T> {
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    //插入数据
    long insert(T t);

    //批量插入
    void insert(List<T> datas);

    QuerySupport<T> querySupport();

    int delete(String whereClause, String... whereArgs);

    int update(T obj, String whereClause, String... whereArgs);

}

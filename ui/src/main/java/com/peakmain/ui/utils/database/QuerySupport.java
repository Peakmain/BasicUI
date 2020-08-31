package com.peakmain.ui.utils.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuerySupport<T> {
    // 查询的列
    private String[] mQueryColumns;
    // 查询的条件
    private String mQuerySelection;
    // 查询的参数
    private String[] mQuerySelectionArgs;
    // 查询分组
    private String mQueryGroupBy;
    // 查询对结果集进行过滤
    private String mQueryHaving;
    // 查询排序
    private String mQueryOrderBy;
    // 查询可用于分页
    private String mQueryLimit;
    private Class<T> mClass;
    private SQLiteDatabase mSQLiteDatabase;

    public QuerySupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mClass = clazz;
        this.mSQLiteDatabase = sqLiteDatabase;
    }

    public QuerySupport columns(String... columns) {
        this.mQueryColumns = columns;
        return this;
    }

    public QuerySupport selectionArgs(String... selectionArgs) {
        this.mQuerySelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport having(String having) {
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport orderBy(String orderBy) {
        this.mQueryOrderBy = orderBy;
        return this;
    }

    public QuerySupport limit(String limit) {
        this.mQueryLimit = limit;
        return this;
    }

    public QuerySupport groupBy(String groupBy) {
        this.mQueryGroupBy = groupBy;
        return this;
    }

    public QuerySupport selection(String selection) {
        this.mQuerySelection = selection;
        return this;
    }

    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtil.getTableName(mClass), mQueryColumns, mQuerySelection
                , mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy);
        clearQueryParams();
        return cursorToList(cursor);
    }

    public List<T> queryAll() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtil.getTableName(mClass), null, null, null, null, null, null);
        return cursorToList(cursor);
    }

    /**
     * 通过Cursor封装成查找对象
     *
     * @return 对象集合列表
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    T instance = mClass.newInstance();
                    Field[] fields = mClass.getDeclaredFields();
                    for (Field field : fields) {
                        // 遍历属性
                        field.setAccessible(true);
                        String name = field.getName();
                        //cursor.getInt(0);
                        // 获取角标
                        int index = cursor.getColumnIndex(name);
                        if (index == -1) {
                            continue;
                        }
                        // 通过反射获取 游标的方法
                        Method cursorMethod = cursorMethod(field.getType());
                        if (cursorMethod != null) {
                            Object value = cursorMethod.invoke(cursor, index);
                            if (value == null) {
                                continue;
                            }

                            // 处理一些特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);
                            } else if (field.getType() == Date.class) {
                                long date = (Long) value;
                                if (date <= 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }
                            field.set(instance, value);
                        }
                    }
                    // 加入集合
                    list.add(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private Method cursorMethod(Class<?> type) throws Exception {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {//是不是基本类型
            typeName = DaoUtil.capitalize(fieldType.getName());//Int
        } else {
            typeName = fieldType.getSimpleName();//Integer
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }

    /**
     * 清空参数
     */
    private void clearQueryParams() {
        mQueryColumns = null;
        mQuerySelection = null;
        mQuerySelectionArgs = null;
        mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryOrderBy = null;
        mQueryLimit = null;
    }
}

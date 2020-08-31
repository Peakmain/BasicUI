package com.peakmain.ui.utils.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import com.peakmain.ui.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DaoSupport<T> implements IDaoSupport<T> {
    private SQLiteDatabase mDatabase;
    private Class<T> mClazz;
    private String TAG = DaoSupport.class.getSimpleName();
    private static final Object[] mPutMethodArgs = new Object[2];

    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();
    private QuerySupport<T> mQuerySupport;

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mDatabase = sqLiteDatabase;
        this.mClazz = clazz;
        StringBuffer sb = new StringBuffer();
        // 创建表
        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/
        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            //设置权限
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        String createTableSql = sb.toString();
        LogUtils.d(createTableSql);
        //创建表
        mDatabase.execSQL(createTableSql);
    }

    @Override
    public long insert(T t) {
        /*ContentValues values = new ContentValues();
        values.put("name",person.getName());
        values.put("age",person.getAge());
        values.put("flag",person.getFlag());
        db.insert("Person",null,values);*/
        ContentValues values = contentValuesByObj(t);
        return mDatabase.insert(DaoUtil.getTableName(mClazz), null, values);
    }

    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                // put 第二个参数是类型  把它转换
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                String filedTypeName = field.getType().getName();
                // 还是使用反射  获取方法  put  缓存方法
                Method putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put",
                            String.class, value.getClass());
                    mPutMethods.put(filedTypeName, putMethod);
                }
                // 通过反射执行
                putMethod.invoke(values, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return values;
    }

    @Override
    public void insert(List<T> datas) {
        mDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    @Override
    public QuerySupport<T> querySupport() {
        if (mQuerySupport == null) {
            mQuerySupport = new QuerySupport<>(mDatabase, mClazz);
        }
        return mQuerySupport;
    }

    @Override
    public int delete(String whereClause, String... whereArgs) {
        return mDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mDatabase.update(DaoUtil.getTableName(mClazz), values, whereClause, whereArgs);
    }
}

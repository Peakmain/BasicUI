package com.peakmain.ui.utils.reflect;

import com.peakmain.ui.utils.LogUtils;

import java.lang.reflect.Field;

/**
 * author ：Peakmain
 * createTime：2021/7/20
 * mail:2726449200@qq.com
 * describe：
 */
public class ReflectFiled<Type> {
    private Class<?> mClazz;
    private String mFieldName;
    private boolean mInit;
    private Field mField;

    public ReflectFiled(Class<?> clazz, String fieldName) {
        if (clazz == null || fieldName == null || fieldName.length() == 0) {
            throw new IllegalArgumentException("neither clazz nor fieldname can be empty");
        }
        this.mClazz = clazz;
        this.mFieldName = fieldName;
    }

    private synchronized void prepare() {
        if (mInit) {
            return;
        }
        Class<?> clazz = this.mClazz;
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(mFieldName);
                field.setAccessible(true);
                mField = field;
                break;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }
        mInit = true;
    }

    public synchronized Type get() throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        return get(false);
    }

    public synchronized Type get(boolean ignoreFieldNoExist)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (mField == null) {
            if (!ignoreFieldNoExist) {
                throw new NoSuchFieldException();
            }
            LogUtils.w(String.format("field %s is no exists.", mFieldName));
            return null;
        }
        Type fieldVal;
        try {
            fieldVal = (Type) mField.get(null);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
        return fieldVal;
    }

    public synchronized Type get(boolean ignoreFieldNoExist, Object instance)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (mField == null) {
            if (!ignoreFieldNoExist) {
                throw new NoSuchFieldException();
            }
            LogUtils.w(String.format("Field %s is no exists.", mFieldName));
            return null;
        }
        Type fieldVal;
        try {
            fieldVal = (Type) mField.get(instance);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
        return fieldVal;
    }

    public synchronized Type get(Object instance) throws NoSuchFieldException, IllegalAccessException {
        return get(false, instance);
    }
    public synchronized boolean set(Type val) throws NoSuchFieldException, IllegalAccessException {
        return set(null, val, false);
    }
    public synchronized boolean set(Object instance, Type val) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        return set(instance, val, false);
    }

    public synchronized boolean set(Object instance, Type val, boolean ignoreFieldNoExist)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (mField == null) {
            if (!ignoreFieldNoExist) {
                throw new NoSuchFieldException("Method " + mFieldName + " is not exists.");
            }
            LogUtils.w(String.format("Field %s is no exists.", mFieldName));
            return false;
        }
        mField.set(instance, val);
        return true;
    }
}

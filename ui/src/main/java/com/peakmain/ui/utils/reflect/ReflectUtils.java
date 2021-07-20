package com.peakmain.ui.utils.reflect;

import android.os.Build;

import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.crash.ExceptionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author ：Peakmain
 * createTime：2021/7/20
 * mail:2726449200@qq.com
 * describe：
 */
public class ReflectUtils {

    public static <T> T get(Class<?> clazz, String fieldName) throws Exception {
        return new ReflectFiled<T>(clazz, fieldName).get();
    }

    public static <T> T get(Class<?> clazz, String fieldName, Object instance) throws Exception {
        return new ReflectFiled<T>(clazz, fieldName).get(instance);
    }

    public static boolean set(Class<?> clazz, String fieldName, Object object) throws Exception {
        return new ReflectFiled(clazz, fieldName).set(object);
    }

    public static boolean set(Class<?> clazz, String fieldName, Object instance, Object value) throws Exception {
        return new ReflectFiled(clazz, fieldName).set(instance, value);
    }

    public static <T> T invoke(Class<?> clazz, String methodName, Object instance, Object... args) throws Exception {
        return new ReflectMethod(clazz, methodName).invoke(instance, args);
    }


    public static <T> T reflectObject(Object instance, String name, T defaultValue, boolean isHard) {
        if (null == instance) return defaultValue;
        if (isHard) {
            try {
                Method getDeclaredField = Class.class.getDeclaredMethod("getDeclaredField", String.class);
                Field field = (Field) getDeclaredField.invoke(instance.getClass(), name);
                field.setAccessible(true);
                return (T) field.get(instance);
            } catch (Exception e) {
                LogUtils.e(e.toString() + "isHard=%s\n%s", true, ExceptionUtils.printException(e));
            }
        } else {
            try {
                Field field = instance.getClass().getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(instance);
            } catch (Exception e) {
                LogUtils.e(e.toString() + "isHard=%s\n%s", false, ExceptionUtils.printException(e));
            }
        }
        return defaultValue;
    }

    public static <T> T reflectObject(Object instance, String name, T defaultValue) {
        return reflectObject(instance, name, defaultValue, true);
    }

    public static Method reflectMethod(Object instance, boolean isHard, String name, Class<?>... argTypes) {
        if (isHard) {
            try {
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Method method = (Method) getDeclaredMethod.invoke(instance.getClass(), name, argTypes);
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                LogUtils.e(e.toString() + "isHard=%s\n%s", true, ExceptionUtils.printException(e));
            }
        } else {
            try {
                Method method = instance.getClass().getDeclaredMethod(name, argTypes);
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                LogUtils.e(e.toString() + "isHard=%s\n%s", false, ExceptionUtils.printException(e));
            }

        }
        return null;
    }

    public static Method reflectMethod(Object instance, String name, Class<?>... argTypes) {
        boolean isHard = Build.VERSION.SDK_INT <= 29;
        return reflectMethod(instance, isHard, name, argTypes);
    }
}

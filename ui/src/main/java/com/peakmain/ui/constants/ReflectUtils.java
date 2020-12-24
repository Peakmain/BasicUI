package com.peakmain.ui.constants;

import android.app.Application;

import java.lang.reflect.Field;

/**
 * author ：Peakmain
 * createTime：2020/12/24
 * mail:2726449200@qq.com
 * describe：
 */
 class ReflectUtils {

    public static Application getApplicationByReflect() {
        try {
            //类
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            //activityThread对象获取
            Object activityThreadStaticField = getActivityThread();
            Object application = activityThreadClass.getMethod("getApplication").invoke(activityThreadStaticField);
            if (application == null) return null;
            return (Application) application;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static Object getActivityThread() {
        Object object = getActivityThreadStaticField();
        if (object != null) return object;
        object = getActivityThreadByStaticMethod();
        if (object != null) return object;
        return getActivityThreadByLoadedApkField();

    }
    private static Object getActivityThreadByLoadedApkField() {
        try {
            Field mLoadedApkField = Application.class.getDeclaredField("mLoadedApk");
            mLoadedApkField.setAccessible(true);
            Object mLoadedApk = mLoadedApkField.get(BasicUIUtils.getApplication());
            Field mActivityThread = mLoadedApk.getClass().getDeclaredField("mActivityThread");
            mActivityThread.setAccessible(true);
            return mActivityThread.get(mLoadedApk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getActivityThreadByStaticMethod() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            return activityThreadClass.getMethod("currentActivityThread").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getActivityThreadStaticField() {
        //类
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThread =
                    activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThread.setAccessible(true);
            return sCurrentActivityThread.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

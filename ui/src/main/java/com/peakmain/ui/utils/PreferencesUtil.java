package com.peakmain.ui.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.peakmain.ui.constants.BasicUIUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * author ：Peakmain
 * createTime：2020/7/23
 * mail:2726449200@qq.com
 * describe：SharedPreferences常用封装工具类
 */
public class PreferencesUtil {
    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;
    private Object object;
    public static volatile PreferencesUtil preferencesUtil;

    public static PreferencesUtil getInstance() {
        if (preferencesUtil == null) {
            synchronized (PreferencesUtil.class) {
                if (preferencesUtil == null) {
                    // 使用双重同步锁
                    preferencesUtil = new PreferencesUtil();
                }
            }
        }
        return preferencesUtil;
    }

    private PreferencesUtil() {
        preferences = PreferenceManager.getDefaultSharedPreferences(BasicUIUtils.getApplication()
                .getApplicationContext());
    }

    /**
     * 保存数据 , 所有的类型都适用
     *
     * @param key    key
     * @param object object
     */
    public synchronized void saveParam(String key, Object object) {
        if (editor == null)
            editor = preferences.edit();
        // 得到object的类型
        String type = object.getClass().getSimpleName();
        if ("String".equals(type)) {
            // 保存String 类型
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            // 保存integer 类型
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            // 保存 boolean 类型
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            // 保存float类型
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            // 保存long类型
            editor.putLong(key, (Long) object);
        } else {
            if (!(object instanceof Serializable)) {
                throw new IllegalArgumentException(object.getClass().getName() + " 必须实现Serializable接口!");
            }

            // 不是基本类型则是保存对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                String productBase64 = Base64.encodeToString(
                        baos.toByteArray(), Base64.DEFAULT);
                editor.putString(key, productBase64);
                Log.d(this.getClass().getSimpleName(), "save object success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(this.getClass().getSimpleName(), "save object error");
            }
        }
        editor.apply();
    }

    /**
     * 移除信息
     */
    public synchronized void remove(String key) {
        if (editor == null)
            editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


    /**
     * 得到保存数据的方法，所有类型都适用
     *
     * @param key           key
     * @param defaultObject 默认值
     * @return 数据
     */
    public Object getParam(String key, Object defaultObject) {
        if (defaultObject == null) {
            return getObject(key);
        }

        String type = defaultObject.getClass().getSimpleName();

        if ("String".equals(type)) {
            return preferences.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return preferences.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return preferences.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return preferences.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return preferences.getLong(key, (Long) defaultObject);
        }
        return getObject(key);
    }

    /**
     * Whether to use for the first time
     */
    public boolean isFirst() {
        return (Boolean) getParam("isFirst", true);
    }

    /**
     * set user first use is false
     */
    public void setFirst(Boolean isFirst) {
        saveParam("isFirst", isFirst);
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(System.currentTimeMillis());
    }

    public void saveRefreshTime() {
        saveParam("REFRESH_TIME", getDate());
    }

    public String getRefreshTime() {
        return (String) getParam("REFRESH_TIME", getDate());
    }

    public Object getObject(String key) {
        String wordBase64 = preferences.getString(key, "");
        byte[] base64 = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            ObjectInputStream bis = new ObjectInputStream(bais);
            object = bis.readObject();
            Log.d(this.getClass().getSimpleName(), "Get object success");
            return object;
        } catch (Exception e) {

        }
        Log.e(this.getClass().getSimpleName(), "Get object is error");
        return null;
    }
}

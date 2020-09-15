package com.peakmain.basicui.utils;


import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.peakmain.basicui.App;
import com.peakmain.ui.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：解析二次封装(以便后期方便更好解析框架替换和重构)
 */
public class JsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 将string 解析成实体类
     *
     * @param jsonString 需要解析String
     * @param typeOfT    实体类类型或者Type类型
     *                   <p/>
     *                   return T 解析后实体对象
     */
    public static <T> T parseObject(String jsonString, Type typeOfT) {
        LogUtils.d("JsonUtil", "jsonString:" + jsonString);
        T t = null;
        try {
            if (jsonString.equals("null") || TextUtils.isEmpty(jsonString)) {
                LogUtils.e("jsonString 为空,不能解析");
            } else {
                t = gson.fromJson(jsonString, typeOfT);
            }
        } catch (Exception e) {
            LogUtils.e("json解析异常" + e.getMessage());
        }
        return t;
    }

    /**
     * 将string 解析成 ArrList
     *
     * @param jsonString 需要解析String
     * @param cls        实体类
     * @return List <T> 解析后实体list
     */
    public static <T> List<T> parseArrList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            if (jsonString.equals("null") || TextUtils.isEmpty(jsonString)) {
                LogUtils.e("jsonString 为空,不能解析");
            } else {
                JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();
                for (final JsonElement elem : array) {
                    list.add(new Gson().fromJson(elem, cls));
                }
            }
        } catch (Exception e) {
            LogUtils.e("json解析异常" + e.getMessage());
        }
        return list;
    }

    /**
     * 将json的外层剥离
     *
     * @param jsonString json
     * @param str        节点
     * @return
     */
    public static String getJsonString(String jsonString, String str) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj != null && obj.has(str)) {
                return obj.opt(str).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将object 转成 string
     *
     * @param jsonString 需要解析String
     *                   return jsonStrin
     */
    public static JsonObject toJsonString(String jsonString) {
        JsonObject jsonStrin = null;
        if (jsonString == null) {
            LogUtils.e("jsonString 为空,不能解析");
        } else {
            jsonStrin = new JsonParser().parse(jsonString).getAsJsonObject();
        }
        return jsonStrin;
    }

    /**
     * string解析成jsonObject
     */
    public static JsonObject stringToJson(String jsonStr) {
        if (jsonStr == null) {
            LogUtils.e("jsonString 为空,不能解析");
            return null;
        }
        return new JsonParser().parse(jsonStr).getAsJsonObject();
    }

    /**
     * 解析完成data
     */
    public static JsonObject jsonData(String jsonStr) {
        JsonObject objStr = null;
        if (jsonStr == null) {
            LogUtils.e("jsonString 为空,不能解析");
            return null;
        }
        try {
            objStr = new JsonParser().parse(jsonStr).getAsJsonObject().getAsJsonObject().getAsJsonObject("data");
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return objStr;
    }

    /**
     * 将对象转换为字符串
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        String jsonstr = null;
        if (gson != null) {
            jsonstr = gson.toJson(object);
        }
        return jsonstr;
    }

    /**
     * 将string 解析成实体类
     *
     * @param jsonString 需要解析String
     * @param cls        实体类
     *                   <p/>
     *                   return T 解析后实体对象
     */
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            if (jsonString.equals("null") || TextUtils.isEmpty(jsonString)) {
                LogUtils.e("jsonString 为空,不能解析");
            } else {
                Gson gson = new Gson();
                t = gson.fromJson(jsonString, cls);
            }
        } catch (Exception e) {
            LogUtils.e("json解析异常" + e.getLocalizedMessage());
        }
        return t;
    }

    /**
     * 比较两个json 是否相等 (包含顺序)
     *
     * @param json1
     * @param json2
     * @return
     */
    public static boolean equalsJsonTree(String json1, String json2) {
        if (gson != null) {
            try {
                JsonElement js1 = gson.toJsonTree(json1);
                JsonElement js2 = gson.toJsonTree(json2);
                return js1.equals(js2);
            } catch (Exception e) {
                LogUtils.e("json解析异常" + e.getMessage());
                return false;
            }
        }
        return false;
    }
    /**
     * 获取Assets文件下的json数据
     *
     * @param fileName 文件名
     */
    public static String getAssetsJson(String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = App.getApp().getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}

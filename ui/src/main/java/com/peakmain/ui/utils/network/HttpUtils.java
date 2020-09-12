package com.peakmain.ui.utils.network;

import android.content.Context;

import com.peakmain.ui.utils.network.callback.DownloadCallback;
import com.peakmain.ui.utils.network.callback.EngineCallBack;
import com.peakmain.ui.utils.network.callback.ProgressEngineCallBack;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：网络请求的工具类
 */
public class HttpUtils {
    //url
    private String mUrl;
    //请求方式
    private int mType = GET_TYPE;
    private static final int GET_TYPE = 0x0010;
    private static final int POST_TYPE = 0x0011;
    private static final int UPLOAD_TYPE = 0x0012;
    private static final int DOWNLOAD_SINGLE_TYPE = 0x0013;
    private static final int DOWNLOAD_MULTI_TYPE = 0x0014;

    private Context mContext;
    private Map<String, Object> mParams;
    private File mFile;

    //不允许外部去调用
    private HttpUtils(Context context) {
        this.mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    //url
    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    //get请求
    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    /**
     * post请求
     */
    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    /**
     * 上传
     */
    public HttpUtils upload() {
        mType = UPLOAD_TYPE;
        return this;
    }

    /**
     * 单线程下载
     */
    public HttpUtils downloadSingle() {
        mType = DOWNLOAD_SINGLE_TYPE;
        return this;
    }

    /**
     * 多线程下载
     */
    public HttpUtils downloadMutil() {
        mType = DOWNLOAD_MULTI_TYPE;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   key
     * @param value string类型
     */
    public HttpUtils addParams(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    /**
     * 添加参数
     *
     * @param key  key
     * @param file 文件类型
     */
    public HttpUtils addParams(String key, File file) {
        mParams.put(key, file);
        return this;
    }

    /**
     * 参数集合
     *
     * @param params 参数集合
     */
    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    public HttpUtils file(File file) {
        this.mFile = file;
        return this;
    }

    /**
     * 自定义okhttpClinet
     *
     * @param okHttpClient okHttpClient
     */
    public HttpUtils okHttpClient(OkHttpClient okHttpClient) {
        if (mHttpEngine instanceof OkHttpEngine) {
            OkHttpEngine okHttpEngine = (OkHttpEngine) mHttpEngine;
            okHttpEngine.setOkHttpClient(okHttpClient);
        }
        return this;
    }

    /**
     * 执行
     *
     * @param callBack 回掉
     */
    public void execture(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }
        if (mType == GET_TYPE) {
            get(mUrl, mParams, callBack);
        } else if (mType == POST_TYPE) {
            post(mUrl, mParams, callBack);
        } else if (mType == UPLOAD_TYPE) {
            uploadFile(mUrl, mFile, callBack);
        }

    }

    /**
     * 下载回掉
     */
    public void exectureDownload(DownloadCallback callback) {
        if (mType == DOWNLOAD_SINGLE_TYPE) {
            downloadSingleManager(mUrl, mFile, callback);
        } else if (mType == DOWNLOAD_MULTI_TYPE) {
            downloadMultiManager(mUrl, mFile, callback);
        }
    }


    public void execture() {
        execture(null);
    }

    //默认是OkHttp引擎
    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    public HttpUtils() {
    }

    //Application中调用
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    /**
     * 切换引擎
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
        return this;
    }


    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext, url, params, callBack);
    }


    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext, url, params, callBack);
    }

    private void uploadFile(String url, File file, EngineCallBack callBack) {
        if (callBack instanceof ProgressEngineCallBack) {
            mHttpEngine.uploadFile(mContext, url, file, (ProgressEngineCallBack) callBack);
        }

    }

    private void downloadSingleManager(String url, File file, DownloadCallback callback) {
        mHttpEngine.downloadSingleManager(mContext, url, file, callback);
    }

    private void downloadMultiManager(String url, File file, DownloadCallback callback) {
        mHttpEngine.downloadMultiManager(mContext, url, file, callback);
    }

    /**
     * 拼接参数
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    /**
     * 反射获取泛型参数
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();//获得带有泛型的父类
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}

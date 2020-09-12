package com.peakmain.ui.utils;

import com.peakmain.ui.utils.network.intercept.CacheRequestInterceptor;
import com.peakmain.ui.utils.network.intercept.CacheResponseInterceptor;
import com.peakmain.ui.utils.network.intercept.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：okhttp的管理类
 */
public class OkHttpManager {
    //连接超时
    private static long CONNECT_TIMEOUT = 60L;
    //阅读超时
    private static long READ_TIMEOUT = 10L;
    //写入超时
    private static long WRITE_TIMEOUT = 10L;
    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为only-if-cached时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    private static volatile OkHttpClient mOkHttpClient;


    /**
     * 下载文件的okHttp实例
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpManager.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient().newBuilder()
                            .addInterceptor(new LoggerInterceptor())
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .addNetworkInterceptor(new CacheResponseInterceptor())
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

}

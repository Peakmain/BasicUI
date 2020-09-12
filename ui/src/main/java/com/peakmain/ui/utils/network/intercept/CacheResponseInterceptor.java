package com.peakmain.ui.utils.network.intercept;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：设置缓存时间的拦截器 默认是30s
 */
public class CacheResponseInterceptor implements Interceptor {
    private int mCacheTime = 30;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        response = response.newBuilder().removeHeader("Cache-Control")
                .addHeader("Cache-Control", "max-age=" + mCacheTime)
                .build();
        return response;
    }

    /**
     * 设置缓存时间
     *
     * @param mCacheTime 缓存的时间 单位s
     */
    public void setCacheTime(int mCacheTime) {
        this.mCacheTime = mCacheTime;
    }
}

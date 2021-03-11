package com.peakmain.ui.utils.network.intercept

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：设置缓存时间的拦截器 默认是30s
 */
class CacheResponseInterceptor : Interceptor {
    private var mCacheTime = 30

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        response = response.newBuilder().removeHeader("Cache-Control")
                .addHeader("Cache-Control", "max-age=$mCacheTime")
                .build()
        return response
    }

    /**
     * 设置缓存时间
     *
     * @param mCacheTime 缓存的时间 单位s
     */
    fun setCacheTime(mCacheTime: Int) {
        this.mCacheTime = mCacheTime
    }
}
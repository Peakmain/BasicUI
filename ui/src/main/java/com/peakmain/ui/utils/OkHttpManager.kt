package com.peakmain.ui.utils

import com.peakmain.ui.utils.network.intercept.CacheResponseInterceptor
import com.peakmain.ui.utils.network.intercept.LoggerInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：okhttp的管理类
 */
object OkHttpManager {
    //连接超时
    private const val CONNECT_TIMEOUT = 60L

    //阅读超时
    private const val READ_TIMEOUT = 10L

    //写入超时
    private const val WRITE_TIMEOUT = 10L

    //设缓存有效期为1天
    private const val CACHE_STALE_SEC = 60 * 60 * 24 * 1.toLong()

    //查询缓存的Cache-Control设置，为only-if-cached时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    const val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"

    @Volatile
    private var mOkHttpClient: OkHttpClient? = null

    /**
     * 下载文件的okHttp实例
     *
     * @return OkHttpClient
     */
    @JvmStatic
    val okHttpClient: OkHttpClient?
        get() {
            if (mOkHttpClient == null) {
                synchronized(OkHttpManager::class.java) {
                    if (mOkHttpClient == null) {
                        mOkHttpClient = OkHttpClient().newBuilder()
                                .addInterceptor(LoggerInterceptor())
                                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                                .addNetworkInterceptor(CacheResponseInterceptor())
                                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                                .build()
                    }
                }
            }
            return mOkHttpClient
        }
}
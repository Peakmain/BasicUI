package com.peakmain.ui.utils.network.intercept

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：缓存的拦截器 没有网络的情况下只读取缓存数据
 */
class CacheRequestInterceptor(private val mContext: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (isNetWork) {
            request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(request)
    }

    val isNetWork: Boolean
        get() {
            val info = getNetworkInfo(mContext)
            return info?.isAvailable ?: false
        }

    companion object {
        private fun getNetworkInfo(context: Context): NetworkInfo {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }
    }

}
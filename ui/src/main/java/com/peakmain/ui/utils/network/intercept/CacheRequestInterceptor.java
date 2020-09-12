package com.peakmain.ui.utils.network.intercept;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：缓存的拦截器 没有网络的情况下只读取缓存数据
 */
public class CacheRequestInterceptor implements Interceptor {
    private Context mContext;

    public CacheRequestInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (isNetWork()) {
            request.newBuilder().cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(request);
    }

    public boolean isNetWork() {
        NetworkInfo info = getNetworkInfo(mContext);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
}

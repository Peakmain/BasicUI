package com.peakmain.ui.imageLoader.glide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.peakmain.ui.imageLoader.ILoader;
import com.peakmain.ui.utils.LogUtils;

import java.io.File;

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：Glide的
 */
public class GlideLoader implements ILoader {

    /**
     * 返回一个请求的配置
     */
    private RequestOptions getRequestOptions(int resId) {

        return getRequestOptions(resId, false);
    }

    @SuppressLint("CheckResult")
    private RequestOptions getRequestOptions(int resId, boolean isSkipCache) {
        RequestOptions options = new RequestOptions();
        if (isSkipCache) {
            options.skipMemoryCache(true);
            options.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        //设置一张占位图
        options.placeholder(resId);
        return options;
    }


    @Override
    public void displayImage(Context context, String url, ImageView view, int desId) {
        RequestOptions options = getRequestOptions(desId);
        loadImage(context, url, view, options);
    }

    @Override
    public void displayImage(Context context, String url, ImageView view, int desId, boolean isSkipCache) {
        RequestOptions options = getRequestOptions(desId, isSkipCache);
        loadImage(context, url, view, options);
    }

    @Override
    public void displayImageRound(Context context, String url, ImageView view, int corner, int desId) {
        RequestOptions options = getRequestOptions(desId).transform(new RoundedCorners(corner));
        loadImage(context, url, view, options);
    }

    @Override
    public void displayImage(Context context, Uri url, ImageView view, int desId) {
        RequestOptions options = getRequestOptions(desId);
        loadImage(context, url, view, options);
    }

    @SuppressLint("CheckResult")
    @Override
    public void displayImage(Context context, String url, ImageView view, int height, int width, int desId) {
        RequestOptions options = getRequestOptions(desId);
        options.override(width, height);
        loadImage(context, url, view, options);
    }

    @SuppressLint("CheckResult")
    @Override
    public void displayImage(Context context, String url, ImageView view, int height, int width, float sizeMultiplier, int desId) {
        RequestOptions options = getRequestOptions(desId);
        options.sizeMultiplier(sizeMultiplier);
        options.override(width, height);
        loadImage(context, url, view, options);
    }

    @Override
    public void displayLocalImage(Context context, String url, ImageView view, int desId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestOptions options = getRequestOptions(desId);
        File file = new File(url);
        if (file.exists()) {
            loadImage(context, file, view, options);
        }
    }

    @Override
    public void displayImage(Context context, Uri url, SimpleTarget simpleTarget) {
        try {
            if (context != null) {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (!isDestroy(activity)) {
                        Glide.with(activity).load(url).into(simpleTarget);
                    }
                } else {
                    Glide.with(context).load(url).into(simpleTarget);
                }
            }
        } catch (Exception e) {
            LogUtils.d("加载图片出错:" + e.getMessage());
        }
    }

    @Override
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 程序在内存清理的时候执行
     */

    @Override
    public void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    @Override
    public void clearAllMemoryCaches(Context context) {
        Glide.get(context).onLowMemory();
    }

    /**
     * 恢复请求
     */
    @Override
    public void resumeRequest(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void pauseRequest(Context context) {
        Glide.with(context).pauseRequests();
    }

    private void loadImage(Context context, Object url, ImageView view, RequestOptions options) {
        try {
            if (context != null) {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (!isDestroy(activity)) {
                        Glide.with(activity).load(url).apply(options).into(view);
                    }
                } else {
                    Glide.with(context).load(url).apply(options).into(view);
                }
            }
        } catch (Exception e) {
            LogUtils.d("加载图片出错：" + e.getMessage());
        }
    }

    //判断activity有没有被销毁
    private boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}

package com.peakmain.ui.imageLoader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.peakmain.ui.imageLoader.glide.GlideLoader;

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：
 */
public class ImageLoader {
    private static volatile ImageLoader mInstance;
    private ILoader mLoader;

    private ImageLoader() {
        mLoader = new GlideLoader();
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    /**
     * 返回一个图片加载器
     *
     * @return Gilde的加载工厂
     */
    private ILoader getLoader() {
        return mLoader;
    }

    /**
     * 切换图片的ImageLoader
     *
     * @param loader 默认是glideLoader
     */
    public ImageLoader exchangeImageLoader(ILoader loader) {
        this.mLoader = loader;
        return this;
    }

    /**
     * 加载网路图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    public void displayImage(Context context, String url, ImageView view, int desId) {
        getLoader().displayImage(context, url, view, desId);
    }

    /**
     * 加载网路图片 圆角
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    public void displayImageRound(Context context, String url, ImageView view, int corner, int desId) {
        getLoader().displayImageRound(context, url, view, corner, desId);
    }

    /**
     * 加载Uri图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    public void displayImage(Context context, Uri url, ImageView view, int desId) {
        getLoader().displayImage(context, url, view, desId);
    }

    /**
     * 指定图片大小加载
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param height  指定图片高度
     * @param width   指定图片宽度
     * @param desId   默认图片Id
     */
    public void displayImage(Context context, String url, ImageView view, int height, int width, int desId) {
        getLoader().displayImage(context, url, view, height, width, desId);
    }

    /**
     * 按照指定大小的缩略图形式加载
     */
    public void displayImage(Context context, String url, ImageView view, int height, int width, float sizeMultiplier, int desId) {
        getLoader().displayImage(context, url, view, height, width, sizeMultiplier, desId);
    }

    /**
     * 加载本地图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    public void displayLocalImage(Context context, String url, ImageView view, int desId) {
        getLoader().displayLocalImage(context, url, view, desId);
    }

    /**
     * 加载图片
     *
     * @param context 上下文
     */
    public void displayImage(Context context, Uri url, SimpleTarget simpleTarget) {
        getLoader().displayImage(context, url, simpleTarget);
    }


    /**
     * 清除图片缓存
     *
     * @param context 上下文
     */
    public void clearImageCache(Context context) {
        getLoader().clearMemory(context);
        getLoader().clearDiskCache(context);
    }

    /**
     * 程序在内存清理的时候执行
     *
     * @param context
     * @param level
     */
    public void trimMemory(Context context, int level) {
        getLoader().trimMemory(context, level);
    }

    /**
     * 低内存的时候执行
     *
     * @param context
     */
    public void clearAllMemoryCaches(Context context) {
        getLoader().clearAllMemoryCaches(context);
    }

    /**
     * 恢复请求
     *
     * @param context
     */
    public void resumeRequest(Context context) {
        if (context != null) {
            getLoader().resumeRequest(context);
        }
    }

    /**
     * 停止请求
     *
     * @param context
     */
    public void pauseRequest(Context context) {
        if (context != null) {
            getLoader().pauseRequest(context);
        }
    }

}

package com.peakmain.ui.imageLoader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：
 */
public interface ILoader {
    /**
     * 加载网路图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    void displayImage(Context context, String url, ImageView view, int desId);

    /**
     * 加载网路图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param corner  显示圆角的图片
     * @param desId   默认图片的Id
     */
    void displayImageRound(Context context, String url, ImageView view, int corner, int desId);

    /**
     * 加载Uri的图片
     * @param context 上下文
     * @param url Uri
     * @param view 显示图片的view
     * @param desId 默认的图片
     */
    void displayImage(Context context, Uri url, ImageView view, int desId);
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
    void displayImage(Context context, String url, ImageView view, int height, int width, int desId);

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
    void displayImage(Context context, String url, ImageView view, int height, int width, float sizeMultiplier, int desId);

    /**
     * 加载本地图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    void displayLocalImage(Context context, String url, ImageView view, int desId);

    /**
     * 加载图片 使用SimpleTarget
     */
    void displayImage(Context context, Uri url, SimpleTarget simpleTarget);

    /**
     * 清除内存缓存
     *
     * @param context 上下文
     */
    void clearMemory(Context context);

    /**
     * 清理磁盘缓存
     *
     * @param context 上下文
     */
    void clearDiskCache(Context context);

    /**
     * 程序在内存清理的时候执行
     * @param context
     * @param level
     */
    void trimMemory(Context context, int level);

    /**
     * 低内存的时候执行
     * @param context
     */
    void clearAllMemoryCaches(Context context);

    /**
     * 恢复请求
     * @param context
     */
    void resumeRequest(Context context);

    /**
     * 停止请求
     * @param context
     */
    void pauseRequest(Context context);

}

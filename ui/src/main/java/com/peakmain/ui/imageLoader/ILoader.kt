package com.peakmain.ui.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：
 */
interface ILoader {
    /**
     * 加载网路图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImage(context: Context?, url: String?, view: ImageView?, desId: Int)

    /**
     * 加载网路图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param corner  显示圆角的图片
     * @param desId   默认图片的Id
     */
    fun displayImageRound(context: Context?, url: String?, view: ImageView?, corner: Int, desId: Int)

    /**
     * 加载Uri的图片
     *
     * @param context 上下文
     * @param url     Uri
     * @param view    显示图片的view
     * @param desId   默认的图片
     */
    fun displayImage(context: Context?, url: Uri?, view: ImageView?, desId: Int)

    /**
     * 加载URL图片，设置是否跳过缓存
     *
     * @param context     上下文
     * @param url         url
     * @param view        显示图片的view
     * @param desId       默认的图片
     * @param isSkipCache 是否跳过缓存， 默认是false
     */
    fun displayImage(context: Context?, url: String?, view: ImageView?, desId: Int, isSkipCache: Boolean)

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
    fun displayImage(context: Context?, url: String?, view: ImageView?, height: Int, width: Int, desId: Int)

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
    fun displayImage(context: Context?, url: String?, view: ImageView?, height: Int, width: Int, sizeMultiplier: Float, desId: Int)

    /**
     * 加载本地图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayLocalImage(context: Context?, url: String?, view: ImageView?, desId: Int)

    /**
     * 加载图片 使用SimpleTarget
     */
    fun displayImage(context: Context?, url: Uri?, simpleTarget: SimpleTarget<Bitmap>)

    /**
     * 清除内存缓存
     *
     * @param context 上下文
     */
    fun clearMemory(context: Context?)

    /**
     * 清理磁盘缓存
     *
     * @param context 上下文
     */
    fun clearDiskCache(context: Context?)

    /**
     * 程序在内存清理的时候执行
     *
     * @param context
     * @param level
     */
    fun trimMemory(context: Context?, level: Int)

    /**
     * 低内存的时候执行
     *
     * @param context
     */
    fun clearAllMemoryCaches(context: Context?)

    /**
     * 恢复请求
     *
     * @param context
     */
    fun resumeRequest(context: Context?)

    /**
     * 停止请求
     *
     * @param context
     */
    fun pauseRequest(context: Context?)
}
package com.peakmain.ui.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.peakmain.ui.imageLoader.glide.GlideLoader

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：
 */
class ImageLoader private constructor() {
    /**
     * 返回一个图片加载器
     *
     * @return Gilde的加载工厂
     */
    private var loader: ILoader? = null

    /**
     * 切换图片的ImageLoader
     *
     * @param loader 默认是glideLoader
     */
    fun exchangeImageLoader(loader: ILoader?): ImageLoader {
        this.loader = loader
        return this
    }

    /**
     * 加载网络图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     */
    fun displayImage(context: Context, url: String, view: ImageView?) {
        loader!!.displayImage(context, url, view, 0)
    }

    /**
     * 加载网络图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImage(context: Context, url: String, view: ImageView?, desId: Int) {
        loader!!.displayImage(context, url, view, desId)
    }

    /**
     * 加载URL图片，设置是否跳过缓存
     *
     * @param context     上下文
     * @param url         url
     * @param view        显示图片的view
     * @param desId       默认的图片
     * @param isSkipCache 是否跳过缓存， 默认是false
     */
    fun displayImage(context: Context, url: String, view: ImageView, desId: Int, isSkipCache: Boolean) {
        loader!!.displayImage(context, url, view, desId, isSkipCache)
    }

    /**
     * 加载网络图片 圆角
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImageRound(context: Context, url: String, view: ImageView, corner: Int, desId: Int) {
        loader!!.displayImageRound(context, url, view, corner, desId)
    }

    /**
     * 加载Uri图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImage(context: Context, url: Uri, view: ImageView, desId: Int) {
        loader!!.displayImage(context, url, view, desId)
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
    fun displayImage(context: Context, url: String, view: ImageView, height: Int, width: Int, desId: Int) {
        loader!!.displayImage(context, url, view, height, width, desId)
    }

    /**
     * 按照指定大小的缩略图形式加载
     */
    fun displayImage(context: Context, url: String, view: ImageView, height: Int, width: Int, sizeMultiplier: Float, desId: Int) {
        loader!!.displayImage(context, url, view, height, width, sizeMultiplier, desId)
    }

    /**
     * 加载本地图片
     *
     * @param context 上下文
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayLocalImage(context: Context, url: String, view: ImageView, desId: Int) {
        loader!!.displayLocalImage(context, url, view, desId)
    }

    /**
     * 加载图片
     *
     * @param context 上下文
     */
    fun displayImage(context: Context, url: Uri, simpleTarget: SimpleTarget<Bitmap>) {
        loader!!.displayImage(context, url, simpleTarget)
    }

    /**
     * 清除图片缓存
     *
     * @param context 上下文
     */
    fun clearImageCache(context: Context) {
        loader!!.clearMemory(context)
        loader!!.clearDiskCache(context)
    }

    /**
     * 程序在内存清理的时候执行
     *
     * @param context
     * @param level
     */
    fun trimMemory(context: Context, level: Int) {
        loader!!.trimMemory(context, level)
    }

    /**
     * 低内存的时候执行
     *
     * @param context
     */
    fun clearAllMemoryCaches(context: Context) {
        loader!!.clearAllMemoryCaches(context)
    }

    /**
     * 恢复请求
     *
     * @param context
     */
    fun resumeRequest(context: Context) {
        if (context != null) {
            loader!!.resumeRequest(context)
        }
    }

    /**
     * 停止请求
     *
     * @param context
     */
    fun pauseRequest(context: Context) {
        if (context != null) {
            loader!!.pauseRequest(context)
        }
    }

    companion object {
        @Volatile
        private var mInstance: ImageLoader? = null
        @JvmStatic
        val instance: ImageLoader?
            get() {
                if (mInstance == null) {
                    synchronized(ImageLoader::class.java) {
                        if (mInstance == null) {
                            mInstance = ImageLoader()
                        }
                    }
                }
                return mInstance
            }
    }

    init {
        if (loader == null) loader = GlideLoader()
    }
}
package com.peakmain.ui.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.peakmain.ui.imageLoader.factory.AbstractLoaderFactory
import com.peakmain.ui.imageLoader.factory.GlideLoaderFactory
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
    private var loaderFactory: AbstractLoaderFactory? = null

    init {
        loaderFactory = GlideLoaderFactory()
    }

    /**
     * 切换图片的ImageLoader
     *
     * @param loader 默认是glideLoader
     */
    fun exchangeImageLoaderFactory(loaderFactory: AbstractLoaderFactory): ImageLoader {
        this.loaderFactory = loaderFactory
        loader = loaderFactory.createLoader()
        return this
    }

    /**
     * 加载网络图片
     *
     * @param url     图片地址
     * @param view    显示图片的View
     */
    fun displayImage(url: String, view: ImageView?) {
        loader!!.displayImage(view?.context, url, view, 0)
    }

    /**
     * 设置UserAgent
     */
    fun userAgent(userAgent: String?) {
        loader!!.userAgent(userAgent)
    }

    /**
     * 加载网络图片
     *
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImage(url: String, view: ImageView?, desId: Int) {
        loader!!.displayImage(view?.context, url, view, desId)
    }

    /**
     * 加载URL图片，设置是否跳过缓存
     *
     * @param url         url
     * @param view        显示图片的view
     * @param desId       默认的图片
     * @param isSkipCache 是否跳过缓存， 默认是false
     */
    fun displayImage(
        url: String,
        view: ImageView?,
        desId: Int,
        isSkipCache: Boolean
    ) {
        loader!!.displayImage(view?.context, url, view, desId, isSkipCache)
    }

    /**
     * 加载URL图片
     *
     * @param url         url
     * @param imageView        显示图片的view
     * @param requestOptions       RequestOptions
     */
    fun displayImage(
        url: String?,
        imageView: ImageView?,
        requestOptions: RequestOptions
    ) {
        loader!!.displayImage(imageView?.context, url, imageView, requestOptions)
    }

    /**
     * 加载网络图片 圆角
     *
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImageRound(url: String, view: ImageView?, corner: Int, @DrawableRes desId: Int) {
        loader!!.displayImageRound(view?.context, url, view, corner, desId)
    }

    /**
     * 加载Uri图片
     *
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayImage(url: Uri, view: ImageView?, desId: Int) {
        loader!!.displayImage(view?.context, url, view, desId)
    }

    /**
     * 指定图片大小加载
     *
     * @param url     图片地址
     * @param view    显示图片的View
     * @param height  指定图片高度
     * @param width   指定图片宽度
     * @param desId   默认图片Id
     */
    fun displayImage(
        url: String,
        view: ImageView?,
        height: Int,
        width: Int,
        desId: Int
    ) {
        loader!!.displayImage(view?.context, url, view, height, width, desId)
    }

    /**
     * 按照指定大小的缩略图形式加载
     */
    fun displayImage(
        url: String,
        view: ImageView?,
        height: Int,
        width: Int,
        sizeMultiplier: Float,
        desId: Int
    ) {
        loader!!.displayImage(view?.context, url, view, height, width, sizeMultiplier, desId)
    }

    /**
     * 加载本地图片
     *
     * @param url     图片地址
     * @param view    显示图片的View
     * @param desId   默认图片的Id
     */
    fun displayLocalImage(url: String, view: ImageView?, desId: Int) {
        loader!!.displayLocalImage(view?.context, url, view, desId)
    }

    /**
     * 加载图片
     *
     * @param context 上下文
     */
    fun displayImage(context: Context?, url: Uri, simpleTarget: CustomTarget<Bitmap>) {
        loader!!.displayImage(context, url, simpleTarget)
    }

    /**
     * 设置ImageView的圆角
     */
    fun displayImageRound(
        url: String?,
        view: ImageView?,
        @DrawableRes desId: Int,
        leftTop: Boolean,
        rightTop: Boolean,
        leftBottom: Boolean,
        rightBottom: Boolean,
        roundRadius: Float
    ) {
        loader!!.displayImageRound(
            view?.context,
            url,
            view,
            desId,
            leftTop,
            rightTop,
            leftBottom,
            rightBottom,
            roundRadius
        )
    }

    /**
     * 清除图片缓存
     *
     * @param context 上下文
     */
    fun clearImageCache(context: Context?) {
        loader!!.clearMemory(context)
        loader!!.clearDiskCache(context)
    }

    fun clearImageCache(view: ImageView?) {
        loader!!.clearMemory(view?.context)
        loader!!.clearDiskCache(view?.context)
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

    fun trimMemory(view: ImageView?, level: Int) {
        loader!!.trimMemory(view?.context, level)
    }

    /**
     * 低内存的时候执行
     *
     * @param context
     */
    fun clearAllMemoryCaches(context: Context?) {
        loader!!.clearAllMemoryCaches(context)
    }

    fun clearAllMemoryCaches(view: ImageView?) {
        loader!!.clearAllMemoryCaches(view?.context)
    }

    /**
     * 恢复请求
     *
     * @param context
     */
    fun resumeRequest(context: Context?) {
        loader!!.resumeRequest(context)
    }

    fun resumeRequest(view: ImageView?) {
        loader!!.resumeRequest(view?.context)
    }

    /**
     * 停止请求
     *
     * @param context
     */
    fun pauseRequest(context: Context?) {
        loader!!.pauseRequest(context)
    }

    companion object {

        @JvmStatic
        val instance: ImageLoader by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoader()
        }
    }

    init {
        if (loader == null) loader = GlideLoader()
    }
}
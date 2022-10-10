package com.peakmain.ui.imageLoader.glide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.peakmain.ui.imageLoader.ILoader
import com.peakmain.ui.utils.LogUtils
import java.io.File
import java.net.MalformedURLException
import java.net.URL

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：Glide的
 */
class GlideLoader : ILoader {
    private var userAgent: String? = null

    /**
     * 返回一个请求的配置
     */
    private fun getRequestOptions(resId: Int): RequestOptions {
        return getRequestOptions(resId, false)
    }

    @SuppressLint("CheckResult")
    private fun getRequestOptions(resId: Int, isSkipCache: Boolean): RequestOptions {
        val options = RequestOptions()
        if (isSkipCache) {
            options.skipMemoryCache(true)
            options.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        //设置一张占位图
        options.placeholder(resId)
        return options
    }

    override fun userAgent(userAgent: String?) {
        this.userAgent = userAgent
    }

    override fun displayImage(context: Context?, url: String?, view: ImageView?, desId: Int) {
        val options = getRequestOptions(desId)
        loadImage(context, url, view, options)
    }

    override fun displayImage(
        context: Context?,
        url: String?,
        view: ImageView?,
        desId: Int,
        isSkipCache: Boolean
    ) {
        val options = getRequestOptions(desId, isSkipCache)
        loadImage(context, url, view, options)
    }

    override fun displayImageRound(
        context: Context?,
        url: String?,
        view: ImageView?,
        corner: Int,
        desId: Int
    ) {
        val options = getRequestOptions(desId).transform(RoundedCorners(corner))
        loadImage(context, url, view, options)
    }

    override fun displayImage(context: Context?, url: Uri?, view: ImageView?, desId: Int) {
        val options = getRequestOptions(desId)
        loadImage(context, url, view, options)
    }

    @SuppressLint("CheckResult")
    override fun displayImage(
        context: Context?,
        url: String?,
        view: ImageView?,
        height: Int,
        width: Int,
        desId: Int
    ) {
        val options = getRequestOptions(desId)
        options.override(width, height)
        loadImage(context, url, view, options)
    }

    @SuppressLint("CheckResult")
    override fun displayImage(
        context: Context?,
        url: String?,
        view: ImageView?,
        height: Int,
        width: Int,
        sizeMultiplier: Float,
        desId: Int
    ) {
        val options = getRequestOptions(desId)
        options.sizeMultiplier(sizeMultiplier)
        options.override(width, height)
        loadImage(context, url, view, options)
    }

    override fun displayImage(context: Context?, url: Uri?, simpleTarget: CustomTarget<Bitmap>) {
        if (context == null) return
        try {
            Glide.with(context).asBitmap().load(url).into(simpleTarget)
        } catch (e: Exception) {
            LogUtils.d("加载图片出错:" + e.message)
        }
    }


    override fun displayLocalImage(context: Context?, url: String?, view: ImageView?, desId: Int) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val options = getRequestOptions(desId)
        val file = File(url!!)
        if (file.exists()) {
            loadImage(context, file, view, options)
        }
    }

    private fun displayImage(url: String, imageView: ImageView, options: RequestOptions) {
        var url1: URL? = null
        try {
            url1 = URL(url)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        url1?.let {
            val glideUrl =
                GlideUrl(it, LazyHeaders.Builder().addHeader("User-Agent", userAgent!!).build())
            Glide.with(imageView.context.applicationContext).asDrawable().load(glideUrl)
                .apply(options)
                .into(imageView)
        }
    }

    override fun clearMemory(context: Context?) {
        if (context == null) return
        Glide.get(context).clearMemory()
    }

    override fun clearDiskCache(context: Context?) {
        if (context == null) return
        Glide.get(context).clearDiskCache()
    }

    /**
     * 程序在内存清理的时候执行
     */
    override fun trimMemory(context: Context?, level: Int) {
        context?.let {
            Glide.get(it).trimMemory(level)
        }
    }

    override fun clearAllMemoryCaches(context: Context?) {
        context?.let {
            Glide.get(it).onLowMemory()
        }
    }

    /**
     * 恢复请求
     */
    override fun resumeRequest(context: Context?) {
        context?.let {
            Glide.with(it).resumeRequests()
        }
    }

    override fun pauseRequest(context: Context?) {
        context?.let {
            Glide.with(it).pauseRequests()
        }
    }

    private fun loadImage(context: Context?, url: Any?, view: ImageView?, options: RequestOptions) {
        if (context == null || view == null) return
        try {
            if (url is String && !TextUtils.isEmpty(userAgent)) {
                displayImage(url, view, options)
            } else {
                Glide.with(context).load(url).apply(options).into(view)
            }
        } catch (e: Exception) {
            LogUtils.e("加载图片出错：" + e.message)
        }
    }

}
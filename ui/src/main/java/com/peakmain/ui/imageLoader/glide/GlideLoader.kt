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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.peakmain.ui.imageLoader.ILoader
import com.peakmain.ui.utils.LogUtils
import java.io.File

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：Glide的
 */
class GlideLoader : ILoader {
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

    override fun displayImage(context: Context?, url: String?, view: ImageView?, desId: Int) {
        val options = getRequestOptions(desId)
        loadImage(context, url, view, options)
    }

    override fun displayImage(context: Context?, url: String?, view: ImageView?, desId: Int, isSkipCache: Boolean) {
        val options = getRequestOptions(desId, isSkipCache)
        loadImage(context, url, view, options)
    }

    override fun displayImageRound(context: Context?, url: String?, view: ImageView?, corner: Int, desId: Int) {
        val options = getRequestOptions(desId).transform(RoundedCorners(corner))
        loadImage(context, url, view, options)
    }

    override fun displayImage(context: Context?, url: Uri?, view: ImageView?, desId: Int) {
        val options = getRequestOptions(desId)
        loadImage(context, url, view, options)
    }

    @SuppressLint("CheckResult")
    override fun displayImage(context: Context?, url: String?, view: ImageView?, height: Int, width: Int, desId: Int) {
        val options = getRequestOptions(desId)
        options.override(width, height)
        loadImage(context, url, view, options)
    }

    @SuppressLint("CheckResult")
    override fun displayImage(context: Context?, url: String?, view: ImageView?, height: Int, width: Int, sizeMultiplier: Float, desId: Int) {
        val options = getRequestOptions(desId)
        options.sizeMultiplier(sizeMultiplier)
        options.override(width, height)
        loadImage(context, url, view, options)
    }

    override fun displayImage(context: Context?, url: Uri?, simpleTarget: SimpleTarget<Bitmap>) {
        try {
            if (context != null) {
                if (context is Activity) {
                    if (!isDestroy(context)) {
                        Glide.with(context).asBitmap().load(url).into(simpleTarget)
                    }
                } else {
                    Glide.with(context).asBitmap().load(url).into(simpleTarget)
                }
            }
        } catch (e: Exception) {
            LogUtils.d("加载图片出错:" + e.message)
        }
    }


    override fun displayLocalImage(context: Context?, url: String?, view: ImageView?, desId: Int) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val options = getRequestOptions(desId)
        val file = File(url)
        if (file.exists()) {
            loadImage(context, file, view, options)
        }
    }

    override fun clearMemory(context: Context?) {
        Glide.get(context!!).clearMemory()
    }

    override fun clearDiskCache(context: Context?) {
        Glide.get(context!!).clearDiskCache()
    }

    /**
     * 程序在内存清理的时候执行
     */
    override fun trimMemory(context: Context?, level: Int) {
        Glide.get(context!!).trimMemory(level)
    }

    override fun clearAllMemoryCaches(context: Context?) {
        Glide.get(context!!).onLowMemory()
    }

    /**
     * 恢复请求
     */
    override fun resumeRequest(context: Context?) {
        Glide.with(context!!).resumeRequests()
    }

    override fun pauseRequest(context: Context?) {
        Glide.with(context!!).pauseRequests()
    }

    private fun loadImage(context: Context?, url: Any?, view: ImageView?, options: RequestOptions) {
        try {
            if (context != null) {
                if (context is Activity) {
                    val activity = context
                    if (!isDestroy(activity)) {
                        Glide.with(activity).load(url).apply(options).into(view!!)
                    }
                } else {
                    Glide.with(context).load(url).apply(options).into(view!!)
                }
            }
        } catch (e: Exception) {
            LogUtils.e("加载图片出错：" + e.message)
        }
    }

    //判断activity有没有被销毁
    private fun isDestroy(activity: Activity?): Boolean {
        return if (activity == null || activity.isFinishing ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            true
        } else {
            false
        }
    }
}
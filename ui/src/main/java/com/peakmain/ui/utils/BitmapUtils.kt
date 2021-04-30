package com.peakmain.ui.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView

/**
 * author ：Peakmain
 * createTime：2021/4/30
 * mail:2726449200@qq.com
 * describe：Bitmap工具类
 */
object BitmapUtils {
    private val mCanvas = Canvas()
    fun createBitmapFromView(view: View): Bitmap? {
        return createBitmapFromView(view, 1f)
    }

    fun createBitmapFromView(view: View, scale: Float): Bitmap? {
        if (view is ImageView) {
            val drawable = view.drawable
            if (drawable != null && drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        view.clearFocus()
        val bitmap = createBitmapSafely((view.width * scale.toInt()), view.height * scale.toInt(), Bitmap.Config.ARGB_8888, 1)
        if (bitmap != null) {
            synchronized(mCanvas) {
                val canvas = mCanvas
                canvas.setBitmap(bitmap)
                canvas.save()
                canvas.drawColor(Color.WHITE)
                canvas.scale(scale, scale)
                view.draw(canvas)
                canvas.restore()
                canvas.setBitmap(null)
            }
        }
        return bitmap
    }


    fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
        try {
            return Bitmap.createBitmap(width, height, config)
        } catch (e: Exception) {
            e.printStackTrace()
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(width, height, config, retryCount)
            }
        }
        return null
    }
}
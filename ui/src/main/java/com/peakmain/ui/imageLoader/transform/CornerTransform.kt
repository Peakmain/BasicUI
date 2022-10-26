package com.peakmain.ui.imageLoader.transform

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import java.security.MessageDigest

/**
 * author ：Peakmain
 * createTime：2022/10/25
 * mail:2726449200@qq.com
 * describe：图片圆角的Transform
 */
internal class CornerTransform(context: Context, radius: Float) : Transformation<Bitmap?> {
    private val mBitmapPool: BitmapPool
    private var radius: Float
    private var isLeftTop = false
    private var isRightTop = false
    private var isLeftBottom = false
    private var isRightBotoom = false

    /**
     * 需要设置圆角的部分
     *
     * @param leftTop     左上角
     * @param rightTop    右上角
     * @param leftBottom  左下角
     * @param rightBottom 右下角
     */
    fun setCorner(leftTop: Boolean, rightTop: Boolean, leftBottom: Boolean, rightBottom: Boolean) {
        isLeftTop = leftTop
        isRightTop = rightTop
        isLeftBottom = leftBottom
        isRightBotoom = rightBottom
    }

    override fun transform(
        context: Context,
        resource: Resource<Bitmap?>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap?> {
        val source = resource.get()
        var finalWidth: Int
        var finalHeight: Int
        var scale: Float
        if (outWidth > outHeight) {
            scale = outHeight.toFloat() / outWidth.toFloat()
            finalWidth = source.width
            finalHeight = (source.width.toFloat() * scale).toInt()
            if (finalHeight > source.height) {
                scale = outWidth.toFloat() / outHeight.toFloat()
                finalHeight = source.height
                finalWidth = (source.height.toFloat() * scale).toInt()
            }
        } else if (outWidth < outHeight) {
            scale = outWidth.toFloat() / outHeight.toFloat()
            finalHeight = source.height
            finalWidth = (source.height.toFloat() * scale).toInt()
            if (finalWidth > source.width) {
                scale = outHeight.toFloat() / outWidth.toFloat()
                finalWidth = source.width
                finalHeight = (source.width.toFloat() * scale).toInt()
            }
        } else {
            finalHeight = source.height
            finalWidth = finalHeight
        }

        //修正圆角
        radius *= finalHeight.toFloat() / outHeight.toFloat()
        var outBitmap: Bitmap? = mBitmapPool[finalWidth, finalHeight, Bitmap.Config.ARGB_8888]
        if (outBitmap == null) {
            outBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(outBitmap!!)
        val paint = Paint()
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val width = (source.width - finalWidth) / 2
        val height = (source.height - finalHeight) / 2
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true
        val rectF = RectF(0.0f, 0.0f, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)

        if (!isLeftTop) {
            canvas.drawRect(0f, 0f, radius, radius, paint)
        }
        if (!isRightTop) {
            canvas.drawRect(canvas.width - radius, 0f, canvas.width.toFloat(), radius, paint)
        }
        if (!isLeftBottom) {
            canvas.drawRect(0f, canvas.height - radius, radius, canvas.height.toFloat(), paint)
        }
        if (!isRightBotoom) {
            canvas.drawRect(
                canvas.width - radius,
                canvas.height - radius,
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                paint
            )
        }
        return BitmapResource.obtain(outBitmap, mBitmapPool)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}

    init {
        mBitmapPool = Glide.get(context).bitmapPool
        this.radius = radius
    }
}
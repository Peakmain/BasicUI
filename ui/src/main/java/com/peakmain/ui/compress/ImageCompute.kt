package com.peakmain.ui.compress

import android.graphics.Bitmap
import android.graphics.Matrix
import kotlin.math.ceil

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：图片等比例压缩  参考luban
 */
object ImageCompute {
    @JvmStatic
    fun computeSize(width: Int, height: Int): Int {
        var srcWidth = width
        var srcHeight = height
        srcWidth = if (srcWidth % 2 == 1) srcWidth + 1 else srcWidth
        srcHeight = if (srcHeight % 2 == 1) srcHeight + 1 else srcHeight
        //拿到最大边和最小边
        val longSide = srcWidth.coerceAtLeast(srcHeight)
        val shortSide = srcWidth.coerceAtMost(srcHeight)
        val scale = shortSide.toFloat() / longSide
        return if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                1
            } else if (longSide < 4990) {
                2
            } else if (longSide in 4991..10239) {
                4
            } else {
                longSide / 1280
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (longSide / 1280 == 0) 1 else longSide / 1280
        } else {
            ceil(longSide / (1280.0 / scale)).toInt()
        }
    }

    @JvmStatic
    fun rotatingImage(bitmap: Bitmap?, angle: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return if (bitmap == null) {
            null
        } else Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
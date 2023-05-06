package com.peakmain.ui.compress

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.peakmain.ui.compress.ImageCompute.computeSize
import com.peakmain.ui.compress.ImageCompute.rotatingImage
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：ndk实现图片压缩
 */
class CompressUtils {
    private external fun compressBitmap(bitmap: Bitmap, quality: Int, fileName: String): Int

    companion object {
        private const val TAG = "CompressUtils"
        init {
            System.loadLibrary("compress-lib")
        }
    }

    private var srcWidth = 0
    private var srcHeight = 0
    fun compressImage(bitmap: Bitmap, quality: Int, fileName: String): Int {
        return compressBitmap(bitmap, quality, fileName)
    }

    fun decodeFile(path: String?): Bitmap {
        //先获取宽度
        val options = BitmapFactory.Options()
        //不加载图片内存只拿宽高
        options.inJustDecodeBounds = true
        options.inSampleSize = 1
        BitmapFactory.decodeFile(path, options)
        srcWidth = options.outWidth
        srcHeight = options.outHeight
        options.inSampleSize = computeSize(srcWidth, srcHeight)
        options.inJustDecodeBounds = false
        var tagBitmap = BitmapFactory.decodeFile(path, options)
        val stream = ByteArrayOutputStream()
        Log.e(TAG, "srcWidth:$srcWidth,srcHeight:$srcHeight")
        try {
            if (Checker.instance.isJPG(FileInputStream(path))) {
                tagBitmap =
                    rotatingImage(tagBitmap, Checker.instance.getOrientation(FileInputStream(path)))
            }
            tagBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return tagBitmap
    }
}
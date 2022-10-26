package com.peakmain.ui.compress

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import androidx.annotation.UiThread
import com.peakmain.ui.utils.FileUtils
import com.peakmain.ui.utils.FileUtils.getFileByPath
import com.peakmain.ui.utils.FileUtils.writeFileFromIS
import com.peakmain.ui.utils.LogUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

/**
 * author ：Peakmain
 * createTime：2020/3/28
 * mail:2726449200@qq.com
 * describe：图片压缩工具
 */
class ImageCompressUtils(builder: Builder) : Handler.Callback {
    //所有图片的路径
    private var mPaths: List<String>?
    private val mLeastCompressSize: Int
    private val mOutFileDir: String?
    private val mCompressListener: OnCompressListener?
    private val mHandler: Handler
    private val mQuality: Int

    @UiThread
    private fun startCompress() {
        if (mPaths == null || mPaths?.size == 0 && mCompressListener != null) {
            mCompressListener!!.onError(NullPointerException("image file cannot be null"))
        }
        //存储压缩后的图片集合
        val result: MutableList<String> = ArrayList()
        //原始图片的集合
        val originalPath: List<String> = ArrayList(mPaths)
        mHandler.sendMessage(mHandler.obtainMessage(COMPRESS_START))
        if (TextUtils.isEmpty(mOutFileDir)) {
            compressFile(result, originalPath)
        } else {
           if( FileUtils.createOrExistsDir(mOutFileDir)){
               LogUtils.d("文件目录存在或创建成功$mOutFileDir")
           }
            val compressFiles: MutableList<String> = ArrayList()
            for (path in mPaths!!) {
                val file = getFileByPath(path)
                val destFile = File(mOutFileDir + File.separator + file!!.name)
                if (file.isFile) {
                    //拷贝到指定的目录
                    try {
                        writeFileFromIS(destFile, FileInputStream(file))
                        compressFiles.add(destFile.absolutePath)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
            //进行的是拷贝之后路径的图片压缩
            compressFile(result, compressFiles)
        }
    }

    private fun compressFile(result: MutableList<String>, paths: List<String>) {
        for (path in paths) {
            if (Checker.isImage(path)) {
                //如果是图片
                //保证 同一时间只有一个Task在后台运行
                AsyncTask.SERIAL_EXECUTOR.execute {
                    val isNeedCompress = Checker.isNeedCompress(mLeastCompressSize, path)
                    if (isNeedCompress) {
                        val compressUtils = CompressUtils()
                        val bitmap = compressUtils.decodeFile(path)
                        /* file = getImageCacheFile(context, Checker.checkSuffix(path));*/compressUtils
                                .compressImage(bitmap, mQuality, path)
                    }
                    if (!result.contains(path)) {
                        //添加到已经缓存的集合中
                        result.add(path)
                    }
                    //判断压缩是否已经结束
                    if (mPaths!!.size == result.size) {
                        mHandler.sendMessage(mHandler.obtainMessage(COMPRESS_SUCCESS, result))
                    }
                }
            } else {
                mCompressListener!!.onError(IllegalArgumentException("This path is not a picture and cannot be read..."))
                break
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        if (mCompressListener == null) {
            return false
        }
        when (msg.what) {
            COMPRESS_START -> mCompressListener.onStart()
            COMPRESS_ERROR -> mCompressListener.onError(msg.obj as Throwable)
            COMPRESS_SUCCESS -> mCompressListener.onSuccess(msg.obj as List<String?>)
            else -> {
            }
        }
        return false
    }

    class Builder(private val mContext: Context) {
        val mPaths: MutableList<String>
        var mLeastCompressSize = 100
        var mCompressListener: OnCompressListener? = null

        //输出文件目录
        var mOutFileDir: String? = null

        //默认100
        var mQuality = 100
        fun load(path: String): Builder {
            mPaths.add(path)
            return this
        }

        fun setQuality(quality: Int): Builder {
            mQuality = quality
            return this
        }

        fun load(paths: List<String>): Builder {
            mPaths.addAll(paths)
            return this
        }

        fun setCompressListener(listener: OnCompressListener?): Builder {
            mCompressListener = listener
            return this
        }

        fun setOutFileDir(outFileDir: String?): Builder {
            mOutFileDir = outFileDir
            return this
        }

        //小于100，忽略图片压缩
        fun ignoreCompress(size: Int): Builder {
            mLeastCompressSize = size
            return this
        }

        private fun build(): ImageCompressUtils {
            return ImageCompressUtils(this)
        }

        fun startCompress() {
            build().startCompress()
        }

        init {
            mPaths = ArrayList()
        }
    }

    companion object {
        //开始
        private const val COMPRESS_START = 101

        //成功
        private const val COMPRESS_SUCCESS = 102

        //失败
        private const val COMPRESS_ERROR = 103
        private const val DEFAULT_DISK_CACHE_DIR = "basic_disk_cache"

        @JvmStatic
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    init {
        mPaths = builder.mPaths
        mLeastCompressSize = builder.mLeastCompressSize
        mOutFileDir = builder.mOutFileDir
        mCompressListener = builder.mCompressListener
        mQuality = builder.mQuality
        mHandler = Handler(Looper.getMainLooper(), this)
    }
}
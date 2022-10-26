package com.peakmain.ui.utils.network.download

import com.peakmain.ui.utils.FileUtils.closeIO
import com.peakmain.ui.utils.HandlerUtils.runOnUiThreadDelay
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.network.callback.DownloadCallback
import com.peakmain.ui.utils.network.model.DownloadProgress
import com.peakmain.ui.utils.network.model.DownloadProgress.Companion.changeProgress
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.*

internal class DownloadRunnable(private val mUrl: String, private val mFile: File?, private val mProgress: DownloadProgress, private val mThreadId: Int, private val mStart: Long, private val mEnd: Long, private val mCallback: DownloadCallback?) : Runnable {
    override fun run() {
        if (mFile == null) {
            LogUtils.e("文件不能为空")
            postFailure(FileNotFoundException("File cannot be empty"))
            return
        }
        if (mCallback == null) {
            LogUtils.e("callback回掉为空")
            return
        }
        val request = Request.Builder()
                .url(mUrl).addHeader("Range", "btyes=$mStart-$mEnd").build()
        val response: Response = try {
            OkHttpClient().newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtils.e("下载异常:" + e.message)
            postFailure(e)
            return
        }
        val code = response.code()
        if (code == 404 || code > 500) {
            postFailure(IllegalStateException("Server exception"))
            return
        }
        val body = response.body()
        if (body == null) {
            postFailure(NullPointerException("body be empty"))
            return
        }
        var inputStream: InputStream? = null
        inputStream = body.byteStream()
        download(inputStream, response.body())
    }

    private fun download(inputStream: InputStream?, body: ResponseBody?) {
        //只读写我们自己的内容
        var accessFile: RandomAccessFile? = null
        mProgress.status = DownloadProgress.LOADING
        try {
            accessFile = RandomAccessFile(mFile, "rwd")
            //从什么位置开始写
            accessFile.seek(mStart)
            var len: Int
            val buffer = ByteArray(1024 * 100)
            while (inputStream!!.read(buffer).also { len = it } != -1 && mProgress.status == DownloadProgress.LOADING) {
                accessFile.write(buffer, 0, len)
                changeProgress(mProgress, len * 2.toLong(), mProgress.totalSize, object : DownloadProgress.Action {
                    override fun call(progress: DownloadProgress?) {
                        mCallback!!.onProgress((progress!!.fraction * 100).toInt())
                    }
                })
            }
            mCallback!!.onSucceed(mFile)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeIO(inputStream)
            closeIO(accessFile)
        }
    }

    private fun postFinish(file: File) {
        mProgress.fraction = 1.0f
        mProgress.status = DownloadProgress.FINISH
        if (mCallback != null) {
            runOnUiThreadDelay(Runnable { mCallback.onSucceed(file) }, 100)
        }
    }

    private fun postFailure(e: Exception) {
        mProgress.status = DownloadProgress.FAILURE
        mCallback!!.onFailure(e)
    }

    fun stop() {
        mProgress.status = DownloadProgress.PAUSE
    }

}
package com.peakmain.ui.utils.network.body

import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.network.callback.UploadProgressListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：上传的RequestBody
 */
class ExMultipartBody(body: MultipartBody, uploadProgressListener: UploadProgressListener?) : RequestBody() {
    private val mRequestBody: RequestBody
    private var mCurrentLength: Long = 0
    private val mProgressListener: UploadProgressListener?
    override fun contentType(): MediaType? {
        return mRequestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mRequestBody.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        //获取总的长度
        val contentLength = contentLength()
        //写了多少条数据
        val forwardingSink: ForwardingSink = object : ForwardingSink(sink) {
            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                mCurrentLength += byteCount
                mProgressListener?.onProgress(contentLength, mCurrentLength)
                LogUtils.d("正在上传数据：当前的长度:$mCurrentLength :总的长度 $mCurrentLength")
                super.write(source, byteCount)
            }
        }
        val bufferedSink = forwardingSink.buffer()
        mRequestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    init {
        mRequestBody = body
        mProgressListener = uploadProgressListener
    }
}
package com.peakmain.ui.utils.network.body;

import android.util.Log;

import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.network.callback.UploadProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：上传的RequestBody
 */
public class ExMultipartBody extends RequestBody {
    private RequestBody mRequestBody;
    private long mCurrentLength;
    private UploadProgressListener mProgressListener;

    public ExMultipartBody(MultipartBody body, UploadProgressListener uploadProgressListener) {
        this.mRequestBody = body;
        this.mProgressListener = uploadProgressListener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        //获取总的长度
        final long contentLength = contentLength();
        //写了多少条数据
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                mCurrentLength += byteCount;
                if (mProgressListener != null) {
                    mProgressListener.onProgress(contentLength, mCurrentLength);
                }
                LogUtils.d("正在上传数据：当前的长度:"+mCurrentLength + " :总的长度 " + mCurrentLength);
                super.write(source, byteCount);
            }
        };
        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

}

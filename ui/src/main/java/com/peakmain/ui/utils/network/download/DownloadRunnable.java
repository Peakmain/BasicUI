package com.peakmain.ui.utils.network.download;

import android.util.Log;


import com.peakmain.ui.utils.FileUtils;
import com.peakmain.ui.utils.HandlerUtils;
import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.network.callback.DownloadCallback;
import com.peakmain.ui.utils.network.model.DownloadProgress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class DownloadRunnable implements Runnable {
   private final File mFile;
   private String mUrl;
   private long mStart;
   private long mEnd;
   private int mThreadId;
   private DownloadCallback mCallback;
   private DownloadProgress mProgress;

   public DownloadRunnable(String url, File file, DownloadProgress progress, int threadId, long start, long end, DownloadCallback callback) {
       this.mUrl = url;
       this.mStart = start;
       this.mEnd = end;
       this.mThreadId = threadId;
       this.mCallback = callback;
       this.mFile = file;
       mProgress = progress;
   }

   @Override
   public void run() {
       if (mFile == null) {
          LogUtils.e( "文件不能为空");
           postFailure(new FileNotFoundException("File cannot be empty"));
           return;
       }
       if (mCallback == null) {
           LogUtils.e("callback回掉为空");
           return;
       }


       Request request = new Request.Builder()
               .url(mUrl).addHeader("Range", "btyes=" + mStart + "-" + mEnd).build();
       Response response;
       try {
           response = new OkHttpClient().newCall(request).execute();
       } catch (IOException e) {
           e.printStackTrace();
          LogUtils.e( "下载异常:" + e.getMessage());
           postFailure(e);
           return;
       }
       int code = response.code();
       if (code == 404 || code > 500) {
           postFailure(new IllegalStateException("Server exception"));
           return;
       }
       ResponseBody body = response.body();
       if (body == null) {
           postFailure(new NullPointerException("body be empty"));
           return;
       }
       InputStream inputStream = null;
       inputStream = body.byteStream();
       download(inputStream,response.body());

   }


   private void download(InputStream inputStream, ResponseBody body) {
       //只读写我们自己的内容
       RandomAccessFile accessFile = null;
       mProgress.status = DownloadProgress.LOADING;
       try {
           accessFile = new RandomAccessFile(mFile, "rwd");
           //从什么位置开始写
           accessFile.seek(mStart);
           int len ;
           byte[] buffer = new byte[1024 * 100];
           while ((len = inputStream.read(buffer)) != -1 && mProgress.status == DownloadProgress.LOADING) {
               accessFile.write(buffer, 0, len);
               DownloadProgress.changeProgress(mProgress, len*2, mProgress.totalSize, new DownloadProgress.Action() {
                   @Override
                   public void call(DownloadProgress progress) {
                       mCallback.onProgress((int) (progress.fraction * 100));
                   }
               });
           }
           mCallback.onSucceed(mFile);

       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           FileUtils.closeIO(inputStream);
           FileUtils.closeIO(accessFile);
       }
   }

   private void postFinish(final File file) {
       mProgress.fraction = 1.0f;
       mProgress.status = DownloadProgress.FINISH;
       if (mCallback != null) {
           HandlerUtils.runOnUiThreadDelay(new Runnable() {
               @Override
               public void run() {
                   mCallback.onSucceed(file);
               }
           }, 100);
       }
   }

   private void postFailure(Exception e) {
       mProgress.status = DownloadProgress.FAILURE;
       mCallback.onFailure(e);
   }

   public void stop() {
       mProgress.status = DownloadProgress.PAUSE;
   }
}

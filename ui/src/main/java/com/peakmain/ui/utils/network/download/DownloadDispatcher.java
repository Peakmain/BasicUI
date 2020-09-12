package com.peakmain.ui.utils.network.download;


import com.peakmain.ui.utils.network.callback.DownloadCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadDispatcher {
   private static final DownloadDispatcher dispatcher = new DownloadDispatcher();
   /**
    * Ready async calls in the order they'll be run.
    */
   private final Deque<DownloadTask> readyTask = new ArrayDeque<>();

   /**
    * Running asynchronous calls. Includes canceled calls that haven't finished yet.
    */
   private final Deque<DownloadTask> runningTask = new ArrayDeque<>();

   /**
    * Running synchronous calls. Includes canceled calls that haven't finished yet.
    */
   private final Deque<DownloadTask> stopTask = new ArrayDeque<>();

   public static DownloadDispatcher getInstance() {
       return dispatcher;
   }

   //1.多线程下载，使用线程池
   public void startDownload(final String url, final File file, final DownloadCallback callback) {
       //2.获取文件大小
       Request request = new Request.Builder().url(url).build();
       Call call = new OkHttpClient().newCall(request);
       call.enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               callback.onFailure(e);
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               long contentLength = response.body().contentLength();

               if (contentLength <= -1) {
                   return;
               }
               //每个线程负责每一块
               DownloadTask downloadTask = new DownloadTask(url, file,contentLength, callback);
               downloadTask.init();
               runningTask.add(downloadTask);
           }
       });
   }
   public void recyclerTask(DownloadTask downloadTask) {
       runningTask.remove(downloadTask);
   }
}

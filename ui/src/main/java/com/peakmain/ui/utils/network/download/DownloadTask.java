package com.peakmain.ui.utils.network.download;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.peakmain.ui.utils.HandlerUtils;
import com.peakmain.ui.utils.network.callback.DownloadCallback;
import com.peakmain.ui.utils.network.model.DownloadProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class DownloadTask {
    private final File mFile;
    private String mUrl;
    private long mContentLength;
    private List<DownloadRunnable> mRunnables;//有多个线程下载
    private
    @Nullable
    ExecutorService executorService;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //有四个线程下载
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private DownloadCallback mCallback;
    private volatile int mSucceedNumber = 0;
    private DownloadProgress mProgress;

    public DownloadTask(String url, File file, long contentLength, DownloadCallback callback) {
        this.mContentLength = contentLength;
        this.mUrl = url;
        mRunnables = new ArrayList<>();
        mCallback = callback;
        this.mFile = file;
        mProgress = new DownloadProgress();
        mProgress.status = DownloadProgress.NONE;
        if (mProgress.totalSize == -1)
            mProgress.totalSize = contentLength;
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable runnable) {
                    Thread thread = new Thread(runnable, "peakmain-okHttp");
                    thread.setDaemon(false);//无守护线程
                    return thread;
                }
            });
        }
        return executorService;
    }

    public void init() {
        for (int i = 0; i < THREAD_SIZE; i++) {
            //每个线程要下载的内容，假设一共4M，每个线程负责1M
            long threadsize = mContentLength / THREAD_SIZE;//每个线程负责下载的大小
            long start = i * threadsize;
            long end = i + threadsize - 1;
            //最后一个线程可能字节没有threadsize大小
            if (i == THREAD_SIZE - 1) {
                end = mContentLength - 1;
            }
            DownloadRunnable runnable = new DownloadRunnable(mUrl, mFile, mProgress, i, start, end, new DownloadCallback() {

                @Override
                public void onFailure(Exception e) {
                    mCallback.onFailure(e);
                }

                @Override
                public void onSucceed(final File file) {
                    synchronized (DownloadTask.class) {
                        mSucceedNumber++;
                        if (mSucceedNumber == THREAD_SIZE) {

                            HandlerUtils.runOnUiThreadDelay(new Runnable() {
                                @Override
                                public void run() {
                                    mCallback.onSucceed(file);
                                    mProgress.status = DownloadProgress.FINISH;
                                }
                            }, 100);
                            //完成之后回收
                            DownloadDispatcher.getInstance().recyclerTask(DownloadTask.this);
                        }
                    }
                }

                @Override
                public void onProgress(final int progress) {
                   HandlerUtils.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           mCallback.onProgress(progress);
                       }
                   });
                }

            });
            //通过线程池去执行
            executorService().execute(runnable);
        }
    }

    public void stop(String url) {
        for (DownloadRunnable runnable : mRunnables) {
            runnable.stop();
        }
    }
}

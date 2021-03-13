package com.peakmain.ui.utils.network.download

import com.peakmain.ui.utils.HandlerUtils.runOnUiThread
import com.peakmain.ui.utils.HandlerUtils.runOnUiThreadDelay
import com.peakmain.ui.utils.network.callback.DownloadCallback
import com.peakmain.ui.utils.network.download.DownloadDispatcher
import com.peakmain.ui.utils.network.model.DownloadProgress
import java.io.File
import java.util.*
import java.util.concurrent.*

class DownloadTask(private val mUrl: String, file: File, private val mContentLength: Long, callback: DownloadCallback) {
    private val mFile: File
    private val mRunnables //有多个线程下载
            : List<DownloadRunnable>
    private var executorService: ExecutorService? = null
    private val mCallback: DownloadCallback

    @Volatile
    private var mSucceedNumber = 0
    private val mProgress: DownloadProgress

    @Synchronized
    fun executorService(): ExecutorService {
        if (executorService == null) {
            executorService = ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
                    SynchronousQueue(), ThreadFactory { runnable ->
                val thread = Thread(runnable, "peakmain-okHttp")
                thread.isDaemon = false //无守护线程
                thread
            })
        }
        return executorService!!
    }

    fun init() {
        for (i in 0 until THREAD_SIZE) {
            //每个线程要下载的内容，假设一共4M，每个线程负责1M
            val threadsize = mContentLength / THREAD_SIZE //每个线程负责下载的大小
            val start = i * threadsize
            var end = i + threadsize - 1
            //最后一个线程可能字节没有threadsize大小
            if (i == THREAD_SIZE - 1) {
                end = mContentLength - 1
            }
            val runnable = DownloadRunnable(mUrl, mFile, mProgress, i, start, end, object : DownloadCallback {
                override fun onFailure(e: Exception?) {
                    mCallback.onFailure(e)
                }

                override fun onSucceed(file: File?) {
                    synchronized(DownloadTask::class.java) {
                        mSucceedNumber++
                        if (mSucceedNumber == THREAD_SIZE) {
                            runOnUiThreadDelay(Runnable {
                                mCallback.onSucceed(file)
                                mProgress.status = DownloadProgress.FINISH
                            }, 100)
                            //完成之后回收
                            DownloadDispatcher.instance.recyclerTask(this@DownloadTask)
                        }
                    }
                }

                override fun onProgress(progress: Int) {
                    runOnUiThread(Runnable { mCallback.onProgress(progress) })
                }
            })
            //通过线程池去执行
            executorService().execute(runnable)
        }
    }

    fun stop(url: String?) {
        for (runnable in mRunnables) {
            runnable.stop()
        }
    }

    companion object {
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

        //有四个线程下载
        private val THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
    }

    init {
        mRunnables = ArrayList()
        mCallback = callback
        mFile = file
        mProgress = DownloadProgress()
        mProgress.status = DownloadProgress.NONE
        if (mProgress.totalSize == -1L) mProgress.totalSize = mContentLength
    }
}
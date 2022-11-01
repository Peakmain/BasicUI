package com.peakmain.ui.utils.network.download

import com.peakmain.ui.utils.network.callback.DownloadCallback
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*

class DownloadDispatcher {
    /**
     * Ready async calls in the order they'll be run.
     */
    private val readyTask: Deque<DownloadTask> = ArrayDeque()

    /**
     * Running asynchronous calls. Includes canceled calls that haven't finished yet.
     */
    private val runningTask: Deque<DownloadTask> = ArrayDeque()

    /**
     * Running synchronous calls. Includes canceled calls that haven't finished yet.
     */
    private val stopTask: Deque<DownloadTask> = ArrayDeque()

    //1.多线程下载，使用线程池
    fun startDownload(url: String, file: File, callback: DownloadCallback) {
        //2.获取文件大小
        val request = Request.Builder().url(url).build()
        val call = OkHttpClient().newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val contentLength = response.body!!.contentLength()
                if (contentLength <= -1) {
                    return
                }
                //每个线程负责每一块
                val downloadTask = DownloadTask(url, file, contentLength, callback)
                downloadTask.init()
                runningTask.add(downloadTask)
            }
        })
    }

    fun recyclerTask(downloadTask: DownloadTask) {
        runningTask.remove(downloadTask)
    }

    companion object {
        val instance = DownloadDispatcher()
    }
}
package com.peakmain.ui.utils.network.model

import android.os.SystemClock

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：下载进度回掉
 */
class DownloadProgress {
    //下载的进度，0-1
    @JvmField
    var totalSize: Long

    //总字节长度, byte
    @JvmField
    var fraction = 0f

    //本次下载的大小, byte
    var currentSize: Long = 0

    //最后一次刷新的时间
    @Transient
    private var lastRefreshTime: Long
    var priority //任务优先级
            : Int

    //当前状态
    @JvmField
    var status = 0
    var tag //下载的标识键
            : String? = null

    interface Action {
        fun call(progress: DownloadProgress?)
    }

    override fun hashCode(): Int {
        return if (tag != null) tag.hashCode() else 0
    }

    companion object {
        //无状态
        const val NONE = 0

        //下载中
        const val LOADING = 1

        //暂停
        const val PAUSE = 2

        //失败
        const val FAILURE = 3

        //完成
        const val FINISH = 4
        fun changeProgress(progress: DownloadProgress, writeSize: Long, action: Action?): DownloadProgress {
            return changeProgress(progress, writeSize, progress.totalSize, action)
        }

        @JvmStatic
        fun changeProgress(progress: DownloadProgress, writeSize: Long, totalSize: Long, action: Action?): DownloadProgress {
            progress.totalSize = totalSize
            progress.currentSize += writeSize
            val currentTime = SystemClock.elapsedRealtime()
            val isNotify = currentTime - progress.lastRefreshTime >= 300
            if (isNotify || progress.currentSize == totalSize) {
                progress.fraction = progress.currentSize * 1.0f / totalSize
                progress.lastRefreshTime = currentTime
                action?.call(progress)
            }
            return progress
        }
    }

    init {
        lastRefreshTime = SystemClock.elapsedRealtime()
        totalSize = -1
        priority = 0
    }
}
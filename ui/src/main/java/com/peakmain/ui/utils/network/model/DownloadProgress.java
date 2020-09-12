package com.peakmain.ui.utils.network.model;

import android.os.SystemClock;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：下载进度回掉
 */
public class DownloadProgress {
    //无状态
    public static final int NONE = 0;
    //下载中
    public static final int LOADING = 1;
    //暂停
    public static final int PAUSE = 2;
    //失败
    public static final int FAILURE = 3;
    //完成
    public static final int FINISH = 4;
    //下载的进度，0-1
    public long totalSize;
    //总字节长度, byte
    public float fraction;
    //本次下载的大小, byte
    public long currentSize;
    //最后一次刷新的时间
    private transient long lastRefreshTime;
    public int priority;                            //任务优先级
    //当前状态
    public int status;
    public String tag;                              //下载的标识键

    public DownloadProgress() {
        lastRefreshTime = SystemClock.elapsedRealtime();
        totalSize = -1;
        priority = 0;
    }

    public static DownloadProgress changeProgress(DownloadProgress progress, long writeSize, Action action) {
        return changeProgress(progress, writeSize, progress.totalSize, action);
    }

    public static DownloadProgress changeProgress(final DownloadProgress progress, long writeSize, long totalSize, final Action action) {
        progress.totalSize = totalSize;
        progress.currentSize += writeSize;
        long currentTime = SystemClock.elapsedRealtime();
        boolean isNotify = (currentTime - progress.lastRefreshTime) >= 300;
        if (isNotify || progress.currentSize == totalSize) {
            progress.fraction = progress.currentSize * 1.0f / totalSize;
            progress.lastRefreshTime = currentTime;
            if (action != null) {
                action.call(progress);
            }
        }
        return progress;
    }


    public interface Action {
        void call(DownloadProgress progress);
    }


    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }
}

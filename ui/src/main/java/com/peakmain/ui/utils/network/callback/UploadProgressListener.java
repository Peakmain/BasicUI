package com.peakmain.ui.utils.network.callback;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：上传文件的回掉接口
 */
public interface UploadProgressListener {
    void onProgress(long total, long current);
}

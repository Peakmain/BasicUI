package com.peakmain.ui.utils.network.callback;

import java.io.File;

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：下载方法的回掉
 */
public interface DownloadCallback {
    void onFailure(Exception e);

    void onSucceed(File file);

    void onProgress(int progress);

}

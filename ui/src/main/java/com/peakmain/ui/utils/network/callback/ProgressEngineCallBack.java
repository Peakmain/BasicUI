package com.peakmain.ui.utils.network.callback;

/**
 * author ：Peakmain
 * createTime：2020/9/10
 * mail:2726449200@qq.com
 * describe：带进度的回掉
 */
public interface ProgressEngineCallBack extends EngineCallBack {
    void onProgress(long total, long current);
}

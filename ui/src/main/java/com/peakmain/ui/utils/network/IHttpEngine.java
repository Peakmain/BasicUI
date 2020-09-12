package com.peakmain.ui.utils.network;

import android.content.Context;

import com.peakmain.ui.utils.network.callback.DownloadCallback;
import com.peakmain.ui.utils.network.callback.EngineCallBack;
import com.peakmain.ui.utils.network.callback.ProgressEngineCallBack;

import java.io.File;
import java.util.Map;

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：网络引擎接口
 */
public interface IHttpEngine {
    /**
     * get方法
     * @param context  上下文
     * @param url 方法的url
     * @param params 方法参数的集合
     * @param callBack 回掉
     */
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    /**
     * post方法
     * @param context  上下文
     * @param url 方法的url
     * @param params 方法参数的集合
     * @param callBack 回掉
     */
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    /**
     * 上传文件
     * @param context  上下文
     * @param url 方法的url
     * @param file 上传的文件
     * @param callBack 回掉
     */
    void uploadFile(Context context, String url, File file, ProgressEngineCallBack callBack);

    /**
     * 单线程下载
     * @param context  上下文
     * @param url 下载的URL
     * @param outFile 保存文件的目录
     * @param callback 下载的回掉
     */
    void downloadSingleManager(Context context, String url, File outFile, DownloadCallback callback);

    /**
     * 多线程下载
     * @param context 上下文
     * @param url 下载的URL
     * @param outFile 保存文件的目录
     * @param callback 下载的回掉
     */
    void downloadMultiManager(Context context, String url, File outFile, DownloadCallback callback);
}

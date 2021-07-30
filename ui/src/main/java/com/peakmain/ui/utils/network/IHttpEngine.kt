package com.peakmain.ui.utils.network

import android.content.Context
import com.peakmain.ui.utils.network.callback.DownloadCallback
import com.peakmain.ui.utils.network.callback.EngineCallBack
import com.peakmain.ui.utils.network.callback.ProgressEngineCallBack
import java.io.File

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：网络引擎接口
 */
interface IHttpEngine {
    /**
     * get方法
     * @param context  上下文
     * @param url 方法的url
     * @param params 方法参数的集合
     * @param callBack 回掉
     */
    operator fun get(context: Context, url: String, params: LinkedHashMap<String, Any>, callBack: EngineCallBack)

    /**
     * post方法
     * @param context  上下文
     * @param url 方法的url
     * @param params 方法参数的集合
     * @param callBack 回掉
     */
    fun post(context: Context, url: String, params: LinkedHashMap<String, Any>, callBack: EngineCallBack)

    /**
     * 上传文件
     * @param context  上下文
     * @param url 方法的url
     * @param file 上传的文件
     * @param callBack 回掉
     */
    fun uploadFile(context: Context, url: String, file: File, callBack: ProgressEngineCallBack)

    /**
     * 单线程下载
     * @param context  上下文
     * @param url 下载的URL
     * @param outFile 保存文件的目录
     * @param callback 下载的回掉
     */
    fun downloadSingleManager(context: Context, url: String, outFile: File, callback: DownloadCallback)

    /**
     * 多线程下载
     * @param context 上下文
     * @param url 下载的URL
     * @param outFile 保存文件的目录
     * @param callback 下载的回掉
     */
    fun downloadMultiManager(context: Context, url: String, outFile: File, callback: DownloadCallback)
}
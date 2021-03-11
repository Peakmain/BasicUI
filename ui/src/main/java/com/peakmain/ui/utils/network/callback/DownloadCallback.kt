package com.peakmain.ui.utils.network.callback

import java.io.File

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：下载方法的回掉
 */
interface DownloadCallback {
    fun onFailure(e: Exception?)
    fun onSucceed(file: File?)
    fun onProgress(progress: Int)
}
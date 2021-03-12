package com.peakmain.ui.compress

/**
 * author ：Peakmain
 * createTime：2020/3/28
 * mail:2726449200@qq.com
 * describe：压缩接口
 */
interface OnCompressListener {
    fun onStart()
    fun onSuccess(list: List<String?>?)
    fun onError(e: Throwable?)
}
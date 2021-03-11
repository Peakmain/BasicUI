package com.peakmain.ui.utils.network.callback

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：get,post方法默认回掉方法
 */
interface EngineCallBack {
    //错误
    fun onError(e: Exception?)

    //成功    data{"",""} 成功 失败 data ""
    fun onSuccess(result: String?)

    companion object {
        //默认回调接口
        val DEFAULT_CALL_BACK: EngineCallBack = object : EngineCallBack {
            override fun onError(e: Exception?) {}
            override fun onSuccess(result: String?) {}
        }
    }
}
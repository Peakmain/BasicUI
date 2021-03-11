package com.peakmain.ui.adapter.flow

import com.peakmain.ui.adapter.BaseAdapter

/**
 * author: peakmain
 * createdata：2019/7/16
 * mail: 2726449200@qq.com
 * desiption:流式布局的基本适配器
 */
abstract class BaseFlowAdapter : BaseAdapter() {
    private var mObserver: FlowObserver? = null

    //3.观察者模式及时更新
    fun unregisterDataSetObserver(observer: FlowObserver?) {
        mObserver = null
    }

    fun registerDataSetObserver(observer: FlowObserver?) {
        mObserver = observer
    }

    fun notifyDataChange() {
        if (mObserver != null) {
            mObserver!!.notifyDataChange()
        }
    }
}
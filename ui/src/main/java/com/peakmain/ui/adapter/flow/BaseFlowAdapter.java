package com.peakmain.ui.adapter.flow;

import com.peakmain.ui.adapter.BaseAdapter;

/**
 * author: peakmain
 * createdata：2019/7/16
 * mail: 2726449200@qq.com
 * desiption:流式布局的基本适配器
 */
public abstract class BaseFlowAdapter extends BaseAdapter {

    private FlowObserver mObserver;

    //3.观察者模式及时更新
    public void unregisterDataSetObserver(FlowObserver observer) {
        mObserver=null;
    }

    public void registerDataSetObserver(FlowObserver observer) {
        this.mObserver=observer;
    }
    public void notifyDataChange(){
        if(mObserver!=null){
            mObserver.notifyDataChange();
        }
    }
}

package com.peakmain.ui.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: peakmain
 * createdata：2019/7/16
 * mail: 2726449200@qq.com
 * desiption:流式布局的基本适配器
 */
public abstract class BaseFlowAdapter {
    //1.多少item数量
    public abstract int getCount();
    //2.获取view通过position
    public abstract View getView(int position, ViewGroup parent);
    //3.观察者模式及时更新
    public void unregisterDataSetObserver(DataSetObserver observer){

    }

    public void registerDataSetObserver(DataSetObserver observer){

    }
}

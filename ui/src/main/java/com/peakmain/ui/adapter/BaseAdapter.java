package com.peakmain.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
public abstract class BaseAdapter {
    /**
     *   1.多少item数量
     */
    public abstract int getCount();

    /**
     * 2.获取view通过position
     */
    public abstract View getView(int position, ViewGroup parent);
}

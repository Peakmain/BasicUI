package com.peakmain.ui.recyclerview.group;

import android.content.Context;

import com.peakmain.ui.R;
import com.peakmain.ui.recyclerview.adapter.ViewHolder;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/24
 * mail:2726449200@qq.com
 * describe：GridLayoutManager悬浮的基本适配器
 */
public abstract class BaseGridGroupRecyclerAdapter<T extends GroupRecyclerBean<T>> extends BaseGroupRecyclerAdapter<T> {
    public BaseGridGroupRecyclerAdapter(Context context, List<T> data, int layoutResId) {
        super(context, data, layoutResId, R.layout.ui_item_recycle_group_head);
    }

    @Override
    protected void convertHead(ViewHolder holder, T item) {

    }
}

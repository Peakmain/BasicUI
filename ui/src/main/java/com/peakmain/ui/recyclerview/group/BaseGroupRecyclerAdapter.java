package com.peakmain.ui.recyclerview.group;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter;
import com.peakmain.ui.recyclerview.adapter.ViewHolder;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：分组的recycleview适配器
 */
public abstract class BaseGroupRecyclerAdapter<T extends GroupRecyclerBean> extends CommonRecyclerAdapter<T> {
    public int GROUP_HEADER_VIEW;

    public BaseGroupRecyclerAdapter(Context context, List<T> data, final int layoutResId, final int groupHeadResId) {
        super(context, data, new GroupMultiType(layoutResId, groupHeadResId));
        GROUP_HEADER_VIEW = groupHeadResId;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == GROUP_HEADER_VIEW) {
            convertHead(holder, getItem(position));
        } else {
            super.onBindViewHolder(holder, holder.getAdapterPosition());
        }
    }

    /**
     * 解决GridLayoutManager添加不占用一行的问题
     */
    public void adjustSpanSize(RecyclerView recyclerView) {
        final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = getItemViewType(position);
                return itemViewType == GROUP_HEADER_VIEW ? layoutManager.getSpanCount() : 1;
            }
        });
    }

    protected abstract void convertHead(ViewHolder holder, T item);
}

package com.peakmain.ui.recyclerview.group;

import com.peakmain.ui.recyclerview.adapter.MultiTypeSupport;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
 class GroupMultiType<T extends GroupRecyclerBean> implements MultiTypeSupport<T> {
    private final int mGroupHeadResId;
    private final int mLayoutResId;

    public GroupMultiType(int layoutResId, int groupHeadResId) {
        this.mGroupHeadResId = groupHeadResId;
        this.mLayoutResId = layoutResId;
    }

    @Override
    public int getLayoutId(T item, int position) {
        if (item.isHeader) {
            return mGroupHeadResId;
        }
        return mLayoutResId;
    }

}

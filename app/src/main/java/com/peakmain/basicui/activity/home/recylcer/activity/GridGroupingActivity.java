package com.peakmain.basicui.activity.home.recylcer.activity;

import android.support.v7.widget.GridLayoutManager;

import com.peakmain.basicui.R;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
public class GridGroupingActivity extends BaseRecyclerAcitvity {


    @Override
    protected int getLayoutId() {
        return R.layout.basic_grid_recycler_view;
    }

    @Override
    protected void initData() {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mGroupAdapter.getItemViewType(position);
                if (itemViewType == mGroupAdapter.GROUP_HEADER_VIEW)
                    return 4;
                else
                    return 1;
            }
        });

        mRecyclerView.setAdapter(mGroupAdapter);
    }


}

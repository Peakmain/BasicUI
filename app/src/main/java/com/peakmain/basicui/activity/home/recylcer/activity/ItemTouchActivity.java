package com.peakmain.basicui.activity.home.recylcer.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.peakmain.basicui.BuildConfig;
import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.ItemTouchHelper.GridItemTouchHelper;
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;

/**
 * author ：Peakmain
 * createTime：2020/3/25
 * mail:2726449200@qq.com
 * describe：
 */
public class ItemTouchActivity extends BaseRecyclerAcitvity {

    @Override
    protected int getLayoutId() {
        return R.layout.basic_grid_recycler_view;
    }

    @Override
    protected void initData() {

        mRecyclerView.setAdapter(mGroupAdapter);
        GridItemTouchHelper itemTouchHelper = new GridItemTouchHelper(mGroupAdapter, mGroupBeans);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mGroupAdapter.getItemViewType(position);
                if (itemViewType == mGroupAdapter.GROUP_HEADER_VIEW)
                    return gridLayoutManager.getSpanCount();
                else
                    return 1;
            }
        });
        //获取更新后的数据
        itemTouchHelper.setOnDataUpdatedListener(datas -> {
            for (GroupBean data : datas) {
                Log.e(BuildConfig.TAG,data.isHeader?"head":data.getUrl());
            }
        }).setGridDragFlags(ItemTouchHelper.UP);

    }
}

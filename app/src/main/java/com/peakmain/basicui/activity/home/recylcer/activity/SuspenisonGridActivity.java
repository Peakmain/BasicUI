package com.peakmain.basicui.activity.home.recylcer.activity;

import android.support.v7.widget.GridLayoutManager;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.itemDecoration.SuspenisonItemDecoration;
import com.peakmain.basicui.adapter.GroupGridAdapter;
import com.peakmain.ui.recyclerview.itemdecoration.BaseSuspenisonItemDecoration;

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
public class SuspenisonGridActivity extends BaseRecyclerAcitvity {
    private GroupGridAdapter mGroupAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.basic_grid_recycler_view;
    }



    @Override
    protected void initData() {
        mNavigationBuilder.setTitleText("GridLayoutManager实现悬浮").create();
        BaseSuspenisonItemDecoration itemDecoration = new SuspenisonItemDecoration.Builder(this, mGroupBeans).create();
        mRecyclerView.addItemDecoration(itemDecoration);
        mGroupAdapter=new GroupGridAdapter(this,mGroupBeans);
        mRecyclerView.setAdapter(mGroupAdapter);
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
    }
}

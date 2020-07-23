package com.peakmain.basicui.activity.home.recylcer.activity;

import android.os.Handler;
import android.util.Log;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.refreshload.BestMissRefreshCreator;
import com.peakmain.basicui.activity.home.recylcer.refreshload.LoadMoreCreator;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView;
import com.peakmain.ui.recyclerview.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/7/22
 * mail:2726449200@qq.com
 * describe：Recycleriew封装的下拉刷新和加载更多
 */
public class PullDownActivity2 extends BaseActivity implements LoadRefreshRecyclerView.OnLoadMoreListener, RefreshRecyclerView.OnRefreshListener {
    LoadRefreshRecyclerView mRecyclerView;
    List<String> list;
    private int index = 20;
    private int lastIndex = index + 10;
    private BaseRecyclerStringAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.basic_load_refresh_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData() {
        List<String> data = getData();
        mAdapter = new BaseRecyclerStringAdapter(this, data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addLoadViewCreator(new LoadMoreCreator());
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addRefreshViewCreator(new BestMissRefreshCreator());
        mRecyclerView.setOnRefreshListener(this);
    }

    private List<String> getData() {
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("数据:" + i);
        }
        return list;
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(() -> {
            List<String> moreData = getMoreData();
            mAdapter.addData(moreData);
            mRecyclerView.onStopLoad();
        }, 2000);

    }

    @Override
    public boolean isLoadMore() {
        Log.e("TAG", "数据的大小:" + mAdapter.getData().size());
        return mAdapter.getDataSize() < 25;
    }

    private List<String> getMoreData() {
        list = new ArrayList<>();
        for (; index < lastIndex; index++) {
            list.add("新数据:" + index);
        }
        lastIndex = index + 10;
        return list;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            List<String> data = getData();
            mAdapter.setData(data);
            mRecyclerView.onStopRefresh();
        }, 2000);
    }
}

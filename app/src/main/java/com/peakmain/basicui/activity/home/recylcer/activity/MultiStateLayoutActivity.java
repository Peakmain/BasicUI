package com.peakmain.basicui.activity.home.recylcer.activity;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/7/23
 * mail:2726449200@qq.com
 * describe：
 */
public class MultiStateLayoutActivity extends BaseActivity {
    LoadRefreshRecyclerView mRecyclerView;
    List<String> list;
    private int index = 20;
    private int lastIndex = index + 10;
    private BaseRecyclerStringAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_state;
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
        findViewById(R.id.button1).setOnClickListener(v -> {
            mRecyclerView.showNoNetwork();
        });
        findViewById(R.id.button2).setOnClickListener(v -> {
            list = new ArrayList<>();
            mAdapter.setData(list);
            mRecyclerView.showEmptyView();
        });
        findViewById(R.id.button3).setOnClickListener(v -> {
            mRecyclerView.showLoading();
        });
        findViewById(R.id.button4).setOnClickListener(v -> {
            mRecyclerView.showError();
        });
    }

    private List<String> getData() {
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("数据:" + i);
        }
        return list;
    }


    private List<String> getMoreData() {
        list = new ArrayList<>();
        for (; index < lastIndex; index++) {
            list.add("新数据:" + index);
        }
        lastIndex = index + 10;
        return list;
    }

}

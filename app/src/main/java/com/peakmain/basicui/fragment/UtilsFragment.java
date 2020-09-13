package com.peakmain.basicui.fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.utils.OkHttpActivity;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseFragmnet;
import com.peakmain.basicui.utils.ActivityUtil;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.recyclerview.listener.OnItemClickListener;
import com.peakmain.ui.utils.network.OkHttpEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/9/13
 * mail:2726449200@qq.com
 * describe：
 */
public class UtilsFragment extends BaseFragmnet {
    private RecyclerView mRecyclerView;
    private List<String> mUtilsBean;
    private BaseRecyclerStringAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_utils;
    }

    @Override
    protected void initView(View view) {
        new DefaultNavigationBar.Builder(getContext(), view.findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("工具类")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create();
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData() {
        mUtilsBean = new ArrayList<>();
        mUtilsBean.add("okhttp网络引擎切换工具类封装");
        mAdapter = new BaseRecyclerStringAdapter(getContext(), mUtilsBean);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    ActivityUtil.gotoActivity(getContext(), OkHttpActivity.class);
                    break;
            }
        });
    }
}

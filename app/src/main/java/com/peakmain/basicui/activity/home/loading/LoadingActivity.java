package com.peakmain.basicui.activity.home.loading;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.utils.ActivityUtil;
import com.peakmain.ui.loading.CircleLoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：
 */
public class LoadingActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<String> mLoadingBean;
    private BaseRecyclerStringAdapter mAdapter;
    private CircleLoadingView mLoadingView;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getLayoutId() {
        return R.layout.basic_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mNavigationBuilder.setTitleText("加载loading").create();
    }

    @Override
    protected void initData() {
        mLoadingBean = new ArrayList<>();
        mLoadingBean.add("花束加载loading");
        mLoadingBean.add("视察动画的loading");
        mAdapter = new BaseRecyclerStringAdapter(this, mLoadingBean);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    mLoadingView = new CircleLoadingView(this);
                    mLoadingView.show();
                    mHandler.postDelayed(() -> mLoadingView.hide(), 2000);
                    break;
                case 1:
                    ActivityUtil.gotoActivity(this,InspectLoadingActvivity.class);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        });
    }
}

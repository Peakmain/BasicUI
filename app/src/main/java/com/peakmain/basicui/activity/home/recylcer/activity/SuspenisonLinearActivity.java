package com.peakmain.basicui.activity.home.recylcer.activity;

import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData;
import com.peakmain.basicui.activity.home.recylcer.itemDecoration.SuspenisonItemDecoration;
import com.peakmain.basicui.adapter.GroupLinearAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.recyclerview.itemdecoration.BaseSuspenisonItemDecoration;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
public class SuspenisonLinearActivity extends BaseActivity {
    protected List<GroupBean> mGroupBeans;
    protected GroupLinearAdapter mGroupAdapter;
    protected RecyclerView mRecyclerView;
    @Override
    protected int getLayoutId() {
        return R.layout.basic_linear_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData() {
        mNavigationBuilder.setTitleText("LinearLayoutManager实现悬浮").create();
        mGroupBeans= PesudoImageData.getInstance().getData();
        BaseSuspenisonItemDecoration itemDecoration = new SuspenisonItemDecoration
                .Builder(this, mGroupBeans).create();
        SuspenisonItemDecoration suspenisonItemDecoration = new SuspenisonItemDecoration(this, mGroupBeans);
        mRecyclerView.addItemDecoration(itemDecoration);
        mGroupAdapter=new GroupLinearAdapter(this,mGroupBeans);
        mRecyclerView.setAdapter(mGroupAdapter);
    }
}

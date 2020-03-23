package com.peakmain.basicui.activity.home.recylcer;

import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData;
import com.peakmain.basicui.adapter.GroupAdapter;
import com.peakmain.basicui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
public class LinearGroupingActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<GroupBean> mGroupBeans;

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
        mGroupBeans = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        //设置假数据
        List<GroupBean> groupBeans = PesudoImageData.getInstance().getData();
        for (GroupBean groupBean : groupBeans) {
            if (!headers.contains(groupBean.getTime())) {
                headers.add(groupBean.getTime());
            }
        }
        for (String header : headers) {
            mGroupBeans.add(new GroupBean(true, header));
            for (GroupBean groupBean : groupBeans) {
                if (header.equals(groupBean.getTime())) {
                    mGroupBeans.add(groupBean);
                }
            }
        }
        GroupAdapter groupAdapter = new GroupAdapter(this, mGroupBeans);
        mRecyclerView.setAdapter(groupAdapter);
    }
}

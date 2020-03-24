package com.peakmain.basicui.activity.home.recylcer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData;
import com.peakmain.basicui.adapter.GroupLinearAdapter;
import com.peakmain.basicui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
public abstract class BaseRecyclerAcitvity extends BaseActivity {
    protected List<GroupBean> mGroupBeans;
    protected GroupLinearAdapter mGroupAdapter;
    protected RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        mGroupAdapter = new GroupLinearAdapter(this, mGroupBeans);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }
}

package com.peakmain.basicui.activity.home;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.activity.home.keyboard.CustomIdKeyboardActivity;
import com.peakmain.basicui.activity.home.keyboard.CustomPasswordKeyboardActivity;
import com.peakmain.basicui.activity.home.keyboard.CustomPointKeyboardActivity;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2021/1/11
 * mail:2726449200@qq.com
 * describe：
 */
public class KeyboardActivity extends BaseActivity {
    private BaseRecyclerStringAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected int getLayoutId() {
        return R.layout.basic_linear_recycler_view;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mNavigationBuilder.setTitleText("自定义keyboard的使用").create();
    }

    @Override
    protected void initData() {
        mData = new ArrayList<>();
        mData.add("自定义支付键盘");
        mData.add("自定义身份证键盘");
        mData.add("自定义小数点键盘");
        mAdapter = new BaseRecyclerStringAdapter(this, mData);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(position -> {
            switch (position){
                case 0:
                    ActivityUtil.gotoActivity(this, CustomPasswordKeyboardActivity.class);
                    break;
                case 1:
                    ActivityUtil.gotoActivity(this, CustomIdKeyboardActivity.class);
                    break;
                case 2:
                    ActivityUtil.gotoActivity(this, CustomPointKeyboardActivity.class);
                    break;
                default:
                    break;
            }
        });
    }
}

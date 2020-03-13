package com.peakmain.basicui.fragment;

import android.view.View;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseFragmnet;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public class MineFragment extends BaseFragmnet {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        new DefaultNavigationBar.Builder(getContext(),view.findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("我的")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create();
    }

    @Override
    protected void initData() {

    }
}

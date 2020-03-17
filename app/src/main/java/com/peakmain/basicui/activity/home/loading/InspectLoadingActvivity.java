package com.peakmain.basicui.activity.home.loading;

import android.os.Handler;
import android.os.Looper;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.loading.InspectLoadingView;

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：
 */
public class InspectLoadingActvivity extends BaseActivity {
    private InspectLoadingView mInspectLoadingView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inspect_loading;
    }

    @Override
    protected void initView() {
        mInspectLoadingView = findViewById(R.id.inspect_loading_view);
        mNavigationBuilder.setTitleText("视察动画的loading").create();
        mInspectLoadingView.show();
    }

    @Override
    protected void initData() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mInspectLoadingView.hide();
            }
        },2000);
    }
}

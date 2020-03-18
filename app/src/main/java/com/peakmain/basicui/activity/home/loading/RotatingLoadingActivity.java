package com.peakmain.basicui.activity.home.loading;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.loading.RotatingLoadingView;

/**
 * author ：Peakmain
 * createTime：2020/3/18
 * mail:2726449200@qq.com
 * describe：
 */
public class RotatingLoadingActivity extends BaseActivity {
    private RotatingLoadingView mLoadingView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rotating_loading;
    }

    @Override
    protected void initView() {
        mLoadingView = findViewById(R.id.rotating_loading_view);
        mNavigationBuilder.setTitleText("仿钉钉的loading").create();
    }

    @Override
    protected void initData() {

    }

    public void start(View view) {
        RotatingLoadingView rotatingLoadingView = new RotatingLoadingView(this);
        rotatingLoadingView.show();
        mHandler.postDelayed(rotatingLoadingView::hide, 2000);
    }

    public void click(View view) {
        mLoadingView.hide();
    }
}

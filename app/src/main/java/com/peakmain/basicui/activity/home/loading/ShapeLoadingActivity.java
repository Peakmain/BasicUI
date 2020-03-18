package com.peakmain.basicui.activity.home.loading;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.peakmain.basicui.R;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.ui.loading.ShapeLoadingView;

/**
 * author ：Peakmain
 * createTime：2020/3/18
 * mail:2726449200@qq.com
 * describe：
 */
public class ShapeLoadingActivity extends BaseActivity {
    private ShapeLoadingView mShapeLoadingView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shape_loading;
    }

    @Override
    protected void initView() {
        mShapeLoadingView = findViewById(R.id.shape_loading_view);
        mNavigationBuilder.setTitleText("仿老版58同城加载loading").create();
    }

    @Override
    protected void initData() {
      mShapeLoadingView.setLoadingName("数据正在加载....");
    }

    public void start(View view) {
        ShapeLoadingView shapeLoadingView = new ShapeLoadingView(this);
        shapeLoadingView.show();
        mHandler.postDelayed(shapeLoadingView::hide, 2000);
    }

    public void click(View view) {
        mShapeLoadingView.hide();
    }
}

package com.peakmain.basicui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.peakmain.basicui.R;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public abstract class BaseActivity extends AppCompatActivity {

    public DefaultNavigationBar.Builder mNavigationBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        setContentView(layoutId);
        mNavigationBuilder = new DefaultNavigationBar.Builder(this, findViewById(android.R.id.content))
                .hideLeftText()
                .setDisplayHomeAsUpEnabled(true)
                .setNavigationOnClickListener(v -> finish())
                .hideRightView()
                .setToolbarBackgroundColor(R.color.colorAccent);
        initView();
        initData();
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorStatus)
                .fitsSystemWindows(true)
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(false) //导航栏图标是深色，不写默认为亮色
                .init();


    }


    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

}

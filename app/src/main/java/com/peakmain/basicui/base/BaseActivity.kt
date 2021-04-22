package com.peakmain.basicui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.peakmain.basicui.R
import com.peakmain.ui.navigationbar.DefaultNavigationBar

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
abstract class BaseActivity : AppCompatActivity() {
    @JvmField
    var mNavigationBuilder: DefaultNavigationBar.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        mNavigationBuilder = DefaultNavigationBar.Builder(this, findViewById(android.R.id.content))
                .hideLeftText()
                .setDisplayHomeAsUpEnabled(true)
                .setNavigationOnClickListener(View.OnClickListener { v: View? -> finish() })
                .hideRightView()
                .setToolbarBackgroundColor(R.color.colorAccent)
        initView()
        initData()
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorStatus)
                .fitsSystemWindows(true)
                .statusBarAlpha(0.3f) //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f) //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f) //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(false) //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(false) //导航栏图标是深色，不写默认为亮色
                .init()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()
}
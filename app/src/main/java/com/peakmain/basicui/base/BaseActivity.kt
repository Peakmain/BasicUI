package com.peakmain.basicui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
                .setToolbarBackgroundColor(R.color.ui_color_01a8e3)
        initView()
        initData()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()
}
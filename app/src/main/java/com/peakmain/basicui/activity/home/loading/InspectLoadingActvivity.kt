package com.peakmain.basicui.activity.home.loading

import android.os.Handler
import android.os.Looper
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.loading.InspectLoadingView

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：
 */
class InspectLoadingActvivity : BaseActivity() {
    private var mInspectLoadingView: InspectLoadingView? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_inspect_loading
    }

    override fun initView() {
        mInspectLoadingView = findViewById(R.id.inspect_loading_view)
        mNavigationBuilder.setTitleText("视察动画的loading").create()
        mInspectLoadingView?.show()
    }

    override fun initData() {
        Handler(Looper.getMainLooper()).postDelayed({ mInspectLoadingView!!.hide() }, 2000)
    }
}
package com.peakmain.basicui.activity.home.loading

import android.os.Handler
import android.os.Looper
import android.view.View
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.loading.RotatingLoadingView

/**
 * author ：Peakmain
 * createTime：2020/3/18
 * mail:2726449200@qq.com
 * describe：
 */
class RotatingLoadingActivity : BaseActivity() {
    private var mLoadingView: RotatingLoadingView? = null
    private val mHandler = Handler(Looper.getMainLooper())
    override fun getLayoutId(): Int {
        return R.layout.activity_rotating_loading
    }

    override fun initView() {
        mLoadingView = findViewById(R.id.rotating_loading_view)
        mNavigationBuilder.setTitleText("仿钉钉的loading").create()
    }

    override fun initData() {}
    fun start(view: View?) {
        val rotatingLoadingView = RotatingLoadingView(this)
        rotatingLoadingView.show()
        mHandler.postDelayed({ rotatingLoadingView.hide() }, 2000)
    }

    fun click(view: View?) {
        mLoadingView!!.hide()
    }
}
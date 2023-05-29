package com.peakmain.basicui.activity.home.loading

import android.os.Handler
import android.os.Looper
import android.view.View
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.loading.ShapeLoadingView

/**
 * author ：Peakmain
 * createTime：2020/3/18
 * mail:2726449200@qq.com
 * describe：
 */
class ShapeLoadingActivity : BaseActivity() {
    private var mShapeLoadingView: ShapeLoadingView? = null
    private val mHandler = Handler(Looper.getMainLooper())
    override fun getLayoutId(): Int {
        return R.layout.activity_shape_loading
    }

    override fun initView() {
        mShapeLoadingView = findViewById(R.id.shape_loading_view)
        mNavigationBuilder!!.setTitleText("仿老版58同城加载loading").create()
    }

    override fun initData() {
        mShapeLoadingView!!.setLoadingName("数据正在加载....")
    }

    fun start(view: View?) {
        val shapeLoadingView = ShapeLoadingView(this)
        shapeLoadingView.show()
        mHandler.postDelayed({ shapeLoadingView.hide() }, 2000)
    }

    fun click(view: View?) {
        mShapeLoadingView!!.hide()
    }
}
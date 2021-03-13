package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class ShapeTextViewActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_shape_text_view
    }

    override fun initView() {
        mNavigationBuilder!!.setTitleText("ShapeTextView的使用").create()
    }

    override fun initData() {}
}
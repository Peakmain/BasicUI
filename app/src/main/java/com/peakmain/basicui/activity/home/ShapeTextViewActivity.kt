package com.peakmain.basicui.activity.home

import android.graphics.Color
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.widget.ShapeTextView

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
        findViewById<ShapeTextView>(R.id.stv_pressed_color)
            .setPressedColor(Color.GRAY)

        findViewById<ShapeTextView>(R.id.stv_enable_color)
            .setUnEnabledColor(Color.BLUE)
    }

    override fun initData() {}
}
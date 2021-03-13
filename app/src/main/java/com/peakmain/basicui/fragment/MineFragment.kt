package com.peakmain.basicui.fragment

import android.view.View
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseFragmnet
import com.peakmain.ui.navigationbar.DefaultNavigationBar

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class MineFragment : BaseFragmnet() {
    override val layoutId: Int
        protected get() = R.layout.fragment_mine

    override fun initView(view: View?) {
        DefaultNavigationBar.Builder(context, view!!.findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("我的")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create()
    }

    override fun initData() {}
}
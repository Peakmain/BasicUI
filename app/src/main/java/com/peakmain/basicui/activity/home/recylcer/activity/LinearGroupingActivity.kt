package com.peakmain.basicui.activity.home.recylcer.activity

import com.peakmain.basicui.R

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
class LinearGroupingActivity : BaseRecyclerAcitvity() {
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initData() {
        mNavigationBuilder!!.setTitleText("LinearLayoutManager实现分组").create()
        mRecyclerView!!.adapter = mGroupAdapter
    }
}
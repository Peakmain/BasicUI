package com.peakmain.basicui.activity.home.recylcer.activity

import com.peakmain.basicui.R

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
class GridGroupingActivity : BaseRecyclerAcitvity() {
    override fun getLayoutId(): Int {
        return R.layout.basic_grid_recycler_view
    }

    override fun initData() {
        mNavigationBuilder!!.setTitleText("GridLayoutManager实现分组").create()
        mGroupAdapter!!.adjustSpanSize(mRecyclerView!!)
        mRecyclerView!!.adapter = mGroupAdapter
    }
}
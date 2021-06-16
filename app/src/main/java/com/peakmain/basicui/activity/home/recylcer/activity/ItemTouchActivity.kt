package com.peakmain.basicui.activity.home.recylcer.activity

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import com.peakmain.basicui.BuildConfig
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.itemTouchHelper.GridItemTouchHelper

/**
 * author ：Peakmain
 * createTime：2020/3/25
 * mail:2726449200@qq.com
 * describe：
 */
class ItemTouchActivity : BaseRecyclerAcitvity() {
    override fun getLayoutId(): Int {
        return R.layout.basic_grid_recycler_view
    }

    override fun initData() {
        mRecyclerView!!.adapter = mGroupAdapter
        val itemTouchHelper = GridItemTouchHelper(mGroupAdapter, mGroupBeans)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
        mGroupAdapter!!.adjustSpanSize(mRecyclerView!!)
        //获取更新后的数据
        itemTouchHelper.setOnDataUpdatedListener {
            for (data in it) {
                Log.e(BuildConfig.TAG, if (data!!.isHeader) "head" else data.url)
            }
        }.setGridDragFlags(ItemTouchHelper.UP)
    }
}
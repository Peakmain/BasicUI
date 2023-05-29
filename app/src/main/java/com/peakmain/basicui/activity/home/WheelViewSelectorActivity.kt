package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.activity.BaseRecyclerAcitvity
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.wheelview.view.GenderWheelView
import com.peakmain.ui.wheelview.view.TimePickerWheelView

/**
 * author ：Peakmain
 * createTime：2021/9/10
 * mail:2726449200@qq.com
 * describe：
 */
class WheelViewSelectorActivity : BaseRecyclerAcitvity() {
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initData() {
        val mData = ArrayList<String>()
        mData.add("时间选择器")
        mData.add("性别选择器")
        mNavigationBuilder?.setTitleText("选择器的使用")?.create()
        val adapter = BaseRecyclerStringAdapter(this, mData)
        mRecyclerView?.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        TimePickerWheelView(this@WheelViewSelectorActivity) {
                            ToastUtils.showLong("time:$it")
                        }.setTitle("时间选择器").show()
                    }
                    1->{
                        GenderWheelView(this@WheelViewSelectorActivity){
                            ToastUtils.showLong(it)
                        }.show()
                    }
                }
            }

        })
    }

}
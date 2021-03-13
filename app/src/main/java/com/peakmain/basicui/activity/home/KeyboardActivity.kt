package com.peakmain.basicui.activity.home

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.keyboard.CustomIdKeyboardActivity
import com.peakmain.basicui.activity.home.keyboard.CustomPasswordKeyboardActivity
import com.peakmain.basicui.activity.home.keyboard.CustomPointKeyboardActivity
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.utils.ActivityUtil
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2021/1/11
 * mail:2726449200@qq.com
 * describe：
 */
class KeyboardActivity : BaseActivity() {
    private var mAdapter: BaseRecyclerStringAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mData: MutableList<String>
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        mNavigationBuilder!!.setTitleText("自定义keyboard的使用").create()
    }

    override fun initData() {
        mData = ArrayList()
        mData.add("自定义支付键盘")
        mData.add("自定义身份证键盘")
        mData.add("自定义小数点键盘")
        mAdapter = BaseRecyclerStringAdapter(this, mData)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRecyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> ActivityUtil.gotoActivity(this@KeyboardActivity, CustomPasswordKeyboardActivity::class.java)
                    1 -> ActivityUtil.gotoActivity(this@KeyboardActivity, CustomIdKeyboardActivity::class.java)
                    2 -> ActivityUtil.gotoActivity(this@KeyboardActivity, CustomPointKeyboardActivity::class.java)
                    else -> {
                    }
                }
            }
        })
    }
}
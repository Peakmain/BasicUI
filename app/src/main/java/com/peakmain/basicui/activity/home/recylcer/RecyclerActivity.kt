package com.peakmain.basicui.activity.home.recylcer

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.activity.*
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.utils.ActivityUtil
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
class RecyclerActivity : BaseActivity() {
    private var mAdapter: BaseRecyclerStringAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mData: MutableList<String>
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        mNavigationBuilder!!.setTitleText("Recycleview的使用").create()
    }

    override fun initData() {
        mData = ArrayList()
        mData.add("GridLayout分组")
        mData.add("LinearLayout分组")
        mData.add("LinearLayout的悬浮")
        mData.add("GridLayout分组的悬浮")
        mData.add("itemTouchHelp方法实现删除和拖拽")
        mData.add("Recycleriew封装的下拉刷新和加载更多1")
        mData.add("Recycleriew封装的下拉刷新和加载更多2")
        mData.add("多状态布局")
        mData.add("多类型布局")
        mData.add("添加头部和尾部")
        mAdapter = BaseRecyclerStringAdapter(this, mData)
        mRecyclerView!!.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mRecyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        GridGroupingActivity::class.java
                    )
                    1 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        LinearGroupingActivity::class.java
                    )
                    2 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        SuspenisonLinearActivity::class.java
                    )
                    3 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        SuspenisonGridActivity::class.java
                    )
                    4 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        ItemTouchActivity::class.java
                    )
                    5 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        PullDownActivity1::class.java
                    )
                    6 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        PullDownActivity2::class.java
                    )
                    7 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        MultiStateLayoutActivity::class.java
                    )
                    8 -> ActivityUtil.gotoActivity(
                        this@RecyclerActivity,
                        MultiTypeLayoutActivity::class.java
                    )
                    9 -> {
                        ActivityUtil.gotoActivity(
                            this@RecyclerActivity,
                            RecyclerViewHeadFooterActivity::class.java
                        )
                    }
                    else -> {
                    }
                }
            }
        })
    }
}
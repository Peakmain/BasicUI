package com.peakmain.basicui.activity.home.recylcer.activity

import android.os.Handler
import android.util.Log
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.refreshload.BestMissRefreshCreator
import com.peakmain.basicui.activity.home.recylcer.refreshload.LoadMoreCreator
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView.OnLoadMoreListener
import com.peakmain.ui.recyclerview.view.RefreshRecyclerView
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/7/22
 * mail:2726449200@qq.com
 * describe：Recycleriew封装的下拉刷新和加载更多
 */
class PullDownActivity2 : BaseActivity(), OnLoadMoreListener, RefreshRecyclerView.OnRefreshListener {
    var mRecyclerView: LoadRefreshRecyclerView? = null
    lateinit var list: MutableList<String>
    private var index = 20
    private var lastIndex = index + 10
    private var mAdapter: BaseRecyclerStringAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.basic_load_refresh_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {
        val data = data
        mAdapter = BaseRecyclerStringAdapter(this, data)
        mRecyclerView!!.adapter= mAdapter
        mRecyclerView!!.addLoadViewCreator(LoadMoreCreator())
        mRecyclerView!!.setOnLoadMoreListener(this)
        mRecyclerView!!.addRefreshViewCreator(BestMissRefreshCreator())
        mRecyclerView!!.setOnRefreshListener(this)
    }

    private val data: MutableList<String>
        private get() {
            list = ArrayList()
            for (i in 0..19) {
                list.add("数据:$i")
            }
            return list
        }

    override fun onLoad() {
        Handler().postDelayed({
            val moreData = moreData
            mAdapter!!.addData(moreData)
            mRecyclerView!!.onStopLoad()
        }, 2000)
    }

    override val isLoadMore: Boolean
        get() {
            Log.e("TAG", "数据的大小:" + mAdapter!!.data.size)
            return mAdapter!!.dataSize < 25
        }

    private val moreData: List<String>
        private get() {
            list = ArrayList()
            while (index < lastIndex) {
                list.add("新数据:$index")
                index++
            }
            lastIndex = index + 10
            return list
        }

    override fun onRefresh() {
        Handler().postDelayed({
            val data = data
            mAdapter!!.setData(data)
            mRecyclerView!!.onStopRefresh()
        }, 2000)
    }
}
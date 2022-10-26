package com.peakmain.basicui.activity.home.recylcer.activity

import android.view.View
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView
import com.peakmain.ui.utils.ToastUtils
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/7/23
 * mail:2726449200@qq.com
 * describe：
 */
class MultiStateLayoutActivity : BaseActivity() {
    var mRecyclerView: LoadRefreshRecyclerView? = null
    lateinit var list: MutableList<String>
    private var index = 20
    private var lastIndex = index + 10
    private var mAdapter: BaseRecyclerStringAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_multi_state
    }

    override fun initView() {
        mNavigationBuilder!!.setTitleText("RecyclerView实现多状态布局").create()
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {
        val data = data
        mAdapter = BaseRecyclerStringAdapter(this, data)
        mRecyclerView!!.adapter = mAdapter
        findViewById<View>(R.id.button1).setOnClickListener { mRecyclerView!!.showNoNetwork() }
        findViewById<View>(R.id.button2).setOnClickListener {
            list = ArrayList()
            mAdapter!!.setData(list)
            mRecyclerView!!.showEmptyView()
        }
        findViewById<View>(R.id.button3).setOnClickListener { v -> mRecyclerView!!.showLoading() }
        findViewById<View>(R.id.button4).setOnClickListener { v: View? -> mRecyclerView!!.showError() }
        findViewById<View>(R.id.button5).setOnClickListener { v: View? -> mRecyclerView!!.showContentView() }
        findViewById<View>(R.id.button6).setOnClickListener { v: View? -> mRecyclerView!!.hideLoading() }
        mRecyclerView!!.setOnRetryClickListener(View.OnClickListener { v: View? -> ToastUtils.showShort("正在重新请求接口...") })
    }

    private val data: List<String>
        get() {
            list = ArrayList()
            for (i in 0..19) {
                list.add("数据:$i")
            }
            return list
        }

    private val moreData: List<String>
        get() {
            list = ArrayList()
            while (index < lastIndex) {
                list.add("新数据:$index")
                index++
            }
            lastIndex = index + 10
            return list
        }
}
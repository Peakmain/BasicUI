package com.peakmain.basicui.activity.home.recylcer.activity

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.refreshload.BestMissRefreshCreator
import com.peakmain.basicui.activity.home.recylcer.refreshload.LoadMoreCreator
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView
import com.peakmain.ui.utils.SizeUtils
import java.util.ArrayList

/**
 * author ：Peakmain
 * createTime：2022/10/27
 * mail:2726449200@qq.com
 * describe：
 */
class RecyclerViewHeadFooterActivity : BaseActivity() {
    var mRecyclerView: LoadRefreshRecyclerView? = null
    lateinit var list: MutableList<String>
    private var index = 20
    private var lastIndex = index + 10
    private var mAdapter: BaseRecyclerStringAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.basic_head_foot_recycler_view
    }

    override fun initView() {
        mNavigationBuilder!!.setTitleText("Recyclerview封装的添加头部和尾部").create()
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {
        val data = data
        mAdapter = BaseRecyclerStringAdapter(this, data)
        mRecyclerView!!.adapter = mAdapter
        val view =
            LayoutInflater.from(this).inflate(R.layout.view_header_footer, mRecyclerView, false)
        view.setOnClickListener{
            mRecyclerView?.removeHeaderView(view)
        }
        mRecyclerView?.addHeaderView(view)
        mRecyclerView?.addFooterView(view)
        val view1 =
            LayoutInflater.from(this).inflate(R.layout.view_header_footer, mRecyclerView, false)
        view1.setBackgroundColor(ContextCompat.getColor(this,R.color.red_100))
        view1.setOnClickListener{
            mRecyclerView?.removeFooterView(view1)
        }
        mRecyclerView?.addHeaderView(view1)
        mRecyclerView?.addFooterView(view1)
    }

    private val data: MutableList<String>
        get() {
            list = ArrayList()
            for (i in 0..19) {
                list.add("数据:$i")
            }
            return list
        }


}
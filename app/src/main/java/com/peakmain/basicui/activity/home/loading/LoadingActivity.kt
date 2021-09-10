package com.peakmain.basicui.activity.home.loading

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.utils.ActivityUtil
import com.peakmain.ui.loading.CircleLoadingView
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/17
 * mail:2726449200@qq.com
 * describe：
 */
class LoadingActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mLoadingBean: MutableList<String>
    private var mAdapter: BaseRecyclerStringAdapter? = null
    private var mLoadingView: CircleLoadingView? = null
    private val mHandler = Handler(Looper.getMainLooper())
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        mNavigationBuilder!!.setTitleText("加载loading").create()
    }

    override fun initData() {
        mLoadingBean = ArrayList()
        mLoadingBean.add("花束加载loading")
        mLoadingBean.add("仿老版58同城加载loading")
        mAdapter = BaseRecyclerStringAdapter(this, mLoadingBean)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRecyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        mLoadingView = CircleLoadingView(this@LoadingActivity)
                        mLoadingView!!.show()
                        mHandler.postDelayed({ mLoadingView!!.hide() }, 2000)
                    }
                    1 -> ActivityUtil.gotoActivity(this@LoadingActivity, ShapeLoadingActivity::class.java)
                    else -> {
                    }
                }
            }
        })
    }
}
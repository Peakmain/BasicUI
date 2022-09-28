package com.peakmain.basicui.activity.home.recylcer.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.adapter.MultiTypeRecyclerAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.utils.ToastUtils
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView
import java.util.*

/**
 * author:Peakmain
 * createTime:2021/9/27
 * mail:2726449200@qq.com
 * describe：
 */
class MultiTypeLayoutActivity : BaseActivity() {
    var mRecyclerView: RecyclerView? = null
    lateinit var list: MutableList<String>
    private var index = 20
    private var lastIndex = index + 10
    private var mAdapter: MultiTypeRecyclerAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mNavigationBuilder!!.setTitleText("RecyclerView实现多类型布局").create()
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {
        val data = data
        mAdapter = MultiTypeRecyclerAdapter(this, data.toMutableList())
        mRecyclerView!!.adapter = mAdapter
    }

    private val data: List<String>
        private get() {
            list = ArrayList()

            for (i in 0..19) {
                if ( i % 3 == 0) {
                    list.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Flmg.jj20.com%2Fup%2Fallimg%2F1111%2F03041Q50536%2F1P304150536-7-1200.jpg&refer=http%3A%2F%2Flmg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1666945985&t=1541c1c1e1dbc052d4310809aedec13a")
                }else{
                    list.add("数据:$i")
                }

            }
            return list
        }

}
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
                    list.add("https://gank.io/images/882afc997ad84f8ab2a313f6ce0f3522")
                }else{
                    list.add("数据:$i")
                }

            }
            return list
        }

}
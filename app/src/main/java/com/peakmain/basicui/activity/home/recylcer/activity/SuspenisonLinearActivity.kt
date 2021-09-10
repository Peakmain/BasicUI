package com.peakmain.basicui.activity.home.recylcer.activity

import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData.Companion.instance
import com.peakmain.basicui.activity.home.recylcer.itemDecoration.SuspenisonItemDecoration
import com.peakmain.basicui.adapter.GroupLinearAdapter
import com.peakmain.basicui.base.BaseActivity


/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
class SuspenisonLinearActivity : BaseActivity() {
    protected lateinit var mGroupBeans: MutableList<GroupBean>
    protected var mGroupAdapter: GroupLinearAdapter? = null
    protected var mRecyclerView: RecyclerView? = null
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {
        mNavigationBuilder!!.setTitleText("LinearLayoutManager实现悬浮").create()
        mGroupBeans = instance.data.toMutableList()
        val itemDecoration = SuspenisonItemDecoration.Builder(this, mGroupBeans)
                .setTextCenter(true).create()
        val suspenisonItemDecoration = SuspenisonItemDecoration(this, mGroupBeans)
        mRecyclerView!!.addItemDecoration(itemDecoration!!)
        mGroupAdapter = GroupLinearAdapter(this, mGroupBeans)
        mRecyclerView!!.adapter = mGroupAdapter
    }
}
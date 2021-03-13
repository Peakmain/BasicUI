package com.peakmain.basicui.activity.home.recylcer.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData
import com.peakmain.basicui.adapter.GroupLinearAdapter
import com.peakmain.basicui.base.BaseActivity
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
abstract class BaseRecyclerAcitvity : BaseActivity() {
    protected lateinit var mGroupBeans: MutableList<GroupBean>
    @JvmField
    protected var mGroupAdapter: GroupLinearAdapter? = null
    @JvmField
    protected var mRecyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mGroupBeans = ArrayList()
        val headers: MutableList<String> = ArrayList()
        //设置假数据
        val groupBeans = PesudoImageData.instance.data
        for (groupBean in groupBeans) {
            if (!headers.contains(groupBean.time)) {
                headers.add(groupBean.time!!)
            }
        }
        for (header in headers) {
            mGroupBeans.add(GroupBean(true, header))
            for (groupBean in groupBeans) {
                if (header == groupBean.time) {
                    mGroupBeans.add(groupBean)
                }
            }
        }
        mGroupAdapter = GroupLinearAdapter(this, mGroupBeans)
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
    }
}
package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.ListMenuAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.widget.menu.ListMenuView
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class MultiMenuActivity : BaseActivity() {
    private var mAdapter: ListMenuAdapter? = null
    private var mMenuView: ListMenuView? = null
    private lateinit var mData: MutableList<String>
    override fun getLayoutId(): Int {
        return R.layout.activity_menu_list
    }

    override fun initView() {
        mMenuView = findViewById(R.id.list_data_screen_view)
        mNavigationBuilder!!.setTitleText("仿58同城多条目菜单删选").create()
    }

    override fun initData() {
        mData = ArrayList()
        mData.add("类型")
        mData.add("品牌")
        mData.add("价格")
        mData.add("更多")
        mAdapter = ListMenuAdapter(this, mData)
        mMenuView!!.setAdapter(mAdapter)
    }
}
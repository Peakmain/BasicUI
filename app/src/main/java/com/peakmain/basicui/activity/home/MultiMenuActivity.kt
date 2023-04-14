package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.ListMenuAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.widget.menu.ListMenuView
import java.util.*
import kotlin.collections.ArrayList

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
        mData.add("推荐排序")
        mData.add("酒店品牌")
        mData.add("热门地点")
        mData.add("综合筛选")

        //初始化推荐排序
        val recommendSortList = ArrayList<String>()
        recommendSortList.add("推荐排序")
        recommendSortList.add("距离优先")
        recommendSortList.add("好评优先")

        //品牌
        val brandList = arrayOf(
            "高品质人文酒店品牌", "精品主题酒店品牌",
            "高品质非标酒店品牌", "高品质商旅酒店品牌", "豪华生活方式品牌", "专注青年商旅酒店品牌", "Z时代生活方式品牌"
        ).toMutableList()


        mAdapter = ListMenuAdapter(this, mData, recommendSortList,brandList)
        mMenuView!!.setAdapter(mAdapter)
    }
}
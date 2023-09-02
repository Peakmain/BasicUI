package com.peakmain.basicui.activity.home

import android.app.KeyguardManager
import android.content.Context
import com.google.gson.Gson
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.ListMenuAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.bean.CategoryRightBean
import com.peakmain.ui.widget.menu.ListMenuView

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
        mNavigationBuilder!!.setTitleText("多条目菜单删选").create()
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

        //热门城市
        val cityList = arrayListOf<String>(
            "上海站",
            "上海南站",
            "上海西站",
            "上海南站(B1西北进站口)",
            "虹桥国际机场",
            "张江",
            "陆家嘴",
            "洋泾",
            "徐家汇",
            "华漕",
            "口袋公园",
            "上海四行仓库抗战纪念馆",
            "城市金融文化广场",
            "四行仓库抗战纪念馆",
            "等觉堂",
            "上海站",
            "上海南站",
            "上海西站",
            "上海南站(B1西北进站口)",
            "虹桥国际机场",
            "张江",
            "陆家嘴",
            "洋泾",
            "徐家汇",
            "华漕",
            "口袋公园",
            "上海四行仓库抗战纪念馆",
            "城市金融文化广场",
            "四行仓库抗战纪念馆",
            "等觉堂",
            "上海站",
            "上海南站",
            "上海西站",
            "上海南站(B1西北进站口)",
            "虹桥国际机场",
            "张江",
            "陆家嘴",
            "洋泾",
            "徐家汇",
            "华漕11",
            "口袋公园11",
            "上海四行仓库抗战纪念馆111",
            "城市金融文化广场111",
            "四行仓库抗战纪念馆11",
            "等觉堂11"
        )

        //左边
        val leftMenuList = arrayOf("优惠活动", "房间类型", "点评分数", "设施服务").toMutableList()
        //右边
        val json =
            "[{\"title\":\"优惠活动\",\"categoryRightBeans\":[{\"subTitle\":\"酒店活动\",\"activityList\":[\"积分换房\",\"3倍积分\",\"新店8折\",\"连住优惠\",\"注册会员专享\"]}]},{\"title\":\"房间类型\",\"categoryRightBeans\":[{\"subTitle\":\"特色主题\",\"activityList\":[\"虎扑\",\"QQ会员\",\"上海美影\",\"网易云音乐\",\"网易严选\",\"知乎\"]}]},{\"title\":\"点评分数\",\"categoryRightBeans\":[{\"subTitle\":\"点评分\",\"activityList\":[\"4分以下\",\"4-4.5分\",\"4.5分以上\"]},{\"subTitle\":\"点评数\",\"activityList\":[\"50条以上\",\"200条以上\",\"500条以上\",\"1千条以上\",\"1万以上\"]}]},{\"title\":\"设施服务\",\"categoryRightBeans\":[{\"subTitle\":\"酒店设施\",\"activityList\":[\"早餐\",\"停车场\",\"会议室\",\"健身房\",\"接收机\",\"洗衣房\"]},{\"subTitle\":\"客房设施\",\"activityList\":[\"直播投屏\",\"浴缸\",\"迷你吧\",\"自动窗帘\",\"有窗\"]},{\"subTitle\":\"特色服务\",\"activityList\":[\"安心酒店\",\"无接触入住\",\"深睡配方\",\"智能机器人\",\"30s入住\",\"在线选房\"]}]}]"
        val categoryRightList = Gson().fromJson(json, Array<CategoryRightBean>::class.java).toList()
        mAdapter =
            ListMenuAdapter(
                this,
                mData,
                recommendSortList,
                brandList,
                cityList,
                leftMenuList,
                categoryRightList
            )
        mMenuView!!.setAdapter(mAdapter)
    }
    override fun onPause() {
        super.onPause()
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardLocked && mMenuView != null && mMenuView!!.isMenuOpen) {
            mAdapter?.closeMenu()
        }

    }
}
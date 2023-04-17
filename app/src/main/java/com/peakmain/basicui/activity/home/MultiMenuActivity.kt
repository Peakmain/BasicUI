package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.ListMenuAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.bean.CategoryRightBean
import com.peakmain.basicui.bean.CategoryRightSubBean
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
        val categoryRightBeans = ArrayList<CategoryRightBean>()
        var categoryRightSubBeans = ArrayList<CategoryRightSubBean>()
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "酒店活动",
                arrayOf("积分换房", "3倍积分", "新店8折", "连住优惠", "注册会员专享").toMutableList()
            )
        )
        categoryRightBeans.add(CategoryRightBean("优惠活动",categoryRightSubBeans))
        categoryRightSubBeans = ArrayList()
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "特色主题",
                arrayOf("虎扑", "QQ会员", "上海美影", "网易云音乐", "网易严选", "知乎").toMutableList()
            )
        )
        categoryRightBeans.add(CategoryRightBean("特色主题",categoryRightSubBeans))
        categoryRightSubBeans = ArrayList()
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "点评分",
                arrayOf("4分以下", "4-4.5分", "4.5分以上").toMutableList()
            )
        )
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "点评数",
                arrayOf("50条以上", "200条以上", "500条以上", "1千条以上", "1万以上").toMutableList()
            )
        )
        categoryRightBeans.add(CategoryRightBean("点评分数",categoryRightSubBeans))
        categoryRightSubBeans = ArrayList()
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "酒店设施",
                arrayOf("早餐", "停车场", "会议室","健身房","接收机","洗衣房").toMutableList()
            )
        )
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "客房设施",
                arrayOf("直播投屏", "浴缸", "迷你吧","自动窗帘","有窗").toMutableList()
            )
        )
        categoryRightSubBeans.add(
            CategoryRightSubBean(
                "特色服务",
                arrayOf("安心酒店", "无接触入住", "深睡配方","智能机器人","30s入住","在线选房").toMutableList()
            )
        )
        categoryRightBeans.add(CategoryRightBean("设施服务",categoryRightSubBeans))
        mAdapter =
            ListMenuAdapter(
                this,
                mData,
                recommendSortList,
                brandList,
                cityList,
                leftMenuList,
                categoryRightBeans
            )
        mMenuView!!.setAdapter(mAdapter)
    }
}
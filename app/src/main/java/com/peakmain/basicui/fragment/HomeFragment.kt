package com.peakmain.basicui.fragment

import android.support.v7.widget.RecyclerView
import android.view.View
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.*
import com.peakmain.basicui.activity.home.loading.LoadingActivity
import com.peakmain.basicui.activity.home.recylcer.RecyclerActivity
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseFragmnet
import com.peakmain.basicui.utils.ActivityUtil
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.recyclerview.itemdecoration.DividerGridItemDecoration
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class HomeFragment : BaseFragmnet() {
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mHomeDataBean: MutableList<String>
    private var mAdapter: BaseRecyclerStringAdapter? = null
    override val layoutId: Int
        protected get() = R.layout.fragment_home

    override fun initView(view: View?) {
        mRecyclerView = view!!.findViewById(R.id.recycler_view)
        DefaultNavigationBar.Builder(context, view.findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("首页")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create()
    }

    override fun initData() {
        mHomeDataBean = ArrayList()
        mHomeDataBean.add("Dialog")
        mHomeDataBean.add("NavigationBar")
        mHomeDataBean.add("PopupWindow+流式布局")
        mHomeDataBean.add("AudioDeleteEditText")
        mHomeDataBean.add("ShapeTextView")
        mHomeDataBean.add("仿今日头条的TableLayout")
        mHomeDataBean.add("loading效果")
        mHomeDataBean.add("仿58同城多条目菜单删选")
        mHomeDataBean.add("自定义键盘")
        mHomeDataBean.add("九宫格解锁")
        mHomeDataBean.add("RecyclerView的使用")
        mHomeDataBean.add("图片压缩")
        mAdapter = BaseRecyclerStringAdapter(context, mHomeDataBean)
        mRecyclerView!!.addItemDecoration(DividerGridItemDecoration(context!!))
        mRecyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 ->                     //dialog
                        ActivityUtil.gotoActivity(context, DialogActivity::class.java)
                    1 ->                     //navigationBar
                        ActivityUtil.gotoActivity(context, NaviagtionBarActivity::class.java)
                    2 -> ActivityUtil.gotoActivity(context, PopWindowAndFlowActivity::class.java)
                    3 -> ActivityUtil.gotoActivity(context, AutoEditTextActivity::class.java)
                    4 -> ActivityUtil.gotoActivity(context, ShapeTextViewActivity::class.java)
                    5 -> ActivityUtil.gotoActivity(context, TableLayoutActivity::class.java)
                    6 -> ActivityUtil.gotoActivity(context, LoadingActivity::class.java)
                    7 -> ActivityUtil.gotoActivity(context, MultiMenuActivity::class.java)
                    8 -> ActivityUtil.gotoActivity(context, KeyboardActivity::class.java)
                    9 -> ActivityUtil.gotoActivity(context, LockScreenActivity::class.java)
                    10 -> ActivityUtil.gotoActivity(context, RecyclerActivity::class.java)
                    11 -> ActivityUtil.gotoActivity(context, ImageCompressActivity::class.java)
                    else -> {
                    }
                }
            }
        })
    }
}
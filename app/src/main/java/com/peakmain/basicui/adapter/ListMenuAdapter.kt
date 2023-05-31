package com.peakmain.basicui.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.bean.CategoryRightBean
import com.peakmain.ui.adapter.menu.BaseListMenuAdapter
import com.peakmain.ui.recyclerview.listener.OnItemClickListener

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
open class ListMenuAdapter(
    private val mContext: Context,
    titles: List<String>,
    private val mRecommendSortList: List<String>,
    private val mBrandList: MutableList<String>,
    private val mCityList: MutableList<String>,
    private val mLeftMenuList: MutableList<String>,
    private val mCategoryRightBeans: List<CategoryRightBean>
) : BaseListMenuAdapter(mContext, titles) {
    override fun openMenu(menuTabView: LinearLayout?,tabView: View) {
        if (menuTabView == null) return

        menuTabView.children.forEach {
            val textView = it.findViewById<TextView>(R.id.tv_menu_tab_title)
            val imageView = it.findViewById<ImageView>(R.id.iv_down)
            imageView.setImageResource(R.drawable.ic_triangle_down)
            textView.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.color_272A2B
                )
            )
        }
        val textView = tabView.findViewById<TextView>(R.id.tv_menu_tab_title)
        textView.setTextColor(Color.parseColor("#6CBD9B"))
        (tabView.findViewById<View>(R.id.iv_down) as ImageView).setImageResource(R.drawable.ic_triangle_up)
    }

    override fun closeMenu(
        menuTabView: LinearLayout,
        tabView: View,
        position: Int,
        isSwitch: Boolean
    ) {
        val textView = tabView.findViewById<TextView>(R.id.tv_menu_tab_title)
        val imageView = tabView.findViewById<ImageView>(R.id.iv_down)
        imageView.setImageResource(R.drawable.ic_triangle_down)
        textView.setTextColor(
            ContextCompat.getColor(
                mContext,
                R.color.color_272A2B
            )
        )
    }

    override val contentViewId: Int
        get() = R.layout.ui_list_menu_content
    override val titleLayoutId: Int
        get() = R.layout.ui_list_data_screen_tab

    override fun setMenuContent(menuView: View?, position: Int) {
        if (menuView == null) return
        when (position) {
            0 -> {
                val recyclerView = menuView.findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(mContext)
                recyclerView.adapter = MenuRecommendSortAdapter(mContext, mRecommendSortList.toMutableList())
            }
            1 -> {
                val tvBrandTitle = menuView.findViewById<TextView>(R.id.tv_brand_title)
                tvBrandTitle.text = "品牌偏好"
                val recyclerView = menuView.findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.layoutManager = GridLayoutManager(mContext, 3)
                recyclerView.adapter = MenuBrandAdapter(mContext, mBrandList)
            }
            2 -> {
                val tvBrandTitle = menuView.findViewById<TextView>(R.id.tv_brand_title)
                tvBrandTitle.text = "热门住宿地"
                val recyclerView = menuView.findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.layoutManager = GridLayoutManager(mContext, 3)
                recyclerView.adapter = MenuHotCityAdapter(mContext, mCityList)
            }
            else -> {
                val rvLeft = menuView.findViewById<RecyclerView>(R.id.rv_left)
                rvLeft.layoutManager = LinearLayoutManager(mContext)
                val leftRecyclerAdapter = MenuLeftRecyclerAdapter(mContext, mLeftMenuList)
                rvLeft.adapter = leftRecyclerAdapter
                val rvRight = menuView.findViewById<RecyclerView>(R.id.rv_right)
                rvRight.layoutManager = LinearLayoutManager(mContext)
                val rightRecyclerAdapter = MenuRightRecyclerAdapter(mContext, mCategoryRightBeans)
                rvRight.adapter = rightRecyclerAdapter
                val rvRightLayoutManager = rvRight.layoutManager as LinearLayoutManager?
                leftRecyclerAdapter.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val selectPosition = leftRecyclerAdapter.mSelectPosition
                        if (selectPosition == position) return
                        leftRecyclerAdapter.setSelectItem(position)
                        rightRecyclerAdapter.setSelectItem(position)
                        rvRightLayoutManager
                            ?.scrollToPositionWithOffset(position, 0)
                    }
                })
                rvRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val firstVisibleItemPosition =
                            rvRightLayoutManager!!.findFirstVisibleItemPosition()
                        if (firstVisibleItemPosition != -1) {
                            rightRecyclerAdapter.setSelectItem(firstVisibleItemPosition)
                            rvLeft.smoothScrollToPosition(firstVisibleItemPosition)
                            leftRecyclerAdapter.setSelectItem(firstVisibleItemPosition)
                        }
                    }
                })
            }
        }
    }

    override fun setTitleContent(textView: TextView?, position: Int) {
        if (textView == null) return
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
    }

    override fun getMenuLayoutId(position: Int): Int {
        return when (position) {
            0 -> R.layout.layout_menu_recommend_sort
            1, 2 -> R.layout.layout_menu_brand
            else -> R.layout.layout_categorize_screen
        }
    }

    override fun shadowClick(): Boolean {
        return false
    }
}
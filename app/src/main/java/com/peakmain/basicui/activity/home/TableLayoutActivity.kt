package com.peakmain.basicui.activity.home
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.fragment.ItemFragment
import com.peakmain.basicui.view.TableLayout
import com.peakmain.ui.tablayout.BaseTabLayout.OnTabItemClickListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class TableLayoutActivity : BaseActivity() {
    private var mViewPager: ViewPager? = null
    private var mTabLayout: TableLayout? = null
    private var mData: List<String>? = null
    private val arr = arrayOf("新闻", "直播", "推荐", "抗击肺炎", "视频", "图片", "段子", "精华")
    override fun getLayoutId(): Int {
        return R.layout.activity_table_layout
    }

    override fun initView() {
        mViewPager = findViewById(R.id.view_pager)
        mTabLayout = findViewById(R.id.tab_layout)
        mNavigationBuilder?.setTitleText("仿今日头条的TableLayout")?.create()
    }

    override fun initData() {
        mData = ArrayList()
        mData = listOf(*arr)
        mViewPager!!.adapter = object : FragmentPagerAdapter(this.supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return ItemFragment.newInstance(arr[position])
            }

            override fun getCount(): Int {
                return arr.size
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                super.destroyItem(container, position, `object`)
            }
        }
        mTabLayout!!.initIndicator(mData, mViewPager)
        mTabLayout!!.setOnTabItemClickListener(object : OnTabItemClickListener {
            override fun onTabItem(postition: Int) {
                mViewPager!!.currentItem = postition
            }
        })
    }
}
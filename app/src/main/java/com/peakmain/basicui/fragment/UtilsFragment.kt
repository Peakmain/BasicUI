package com.peakmain.basicui.fragment

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.utils.GlideActivity
import com.peakmain.basicui.activity.utils.TextUtilsActivity
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseFragmnet
import com.peakmain.basicui.utils.ActivityUtil
import com.peakmain.ui.dialog.AlertDialog
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.utils.BitmapUtils

/**
 * author ：Peakmain
 * createTime：2020/9/13
 * mail:2726449200@qq.com
 * describe：
 */
open class UtilsFragment : BaseFragmnet() {
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mUtilsBean: MutableList<String>
    private var mAdapter: BaseRecyclerStringAdapter? = null
    override val layoutId: Int
        protected get() = R.layout.fragment_utils

    override fun initView(view: View?) {
        DefaultNavigationBar.Builder(context, view?.findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("工具类")
                .setToolbarBackgroundColor(R.color.ui_color_01a8e3)
                .create()
        mRecyclerView = view?.findViewById(R.id.recycler_view)
    }

    override fun initData() {
        mUtilsBean = ArrayList()
        mUtilsBean.add("okhttp网络引擎切换工具类封装")
        mUtilsBean.add("Glide图片选择切换封装")
        mUtilsBean.add("文本高亮工具类的封装")
        mUtilsBean.add("View创建Bitmap")
        mUtilsBean.add("Java异常测试")
        mAdapter = BaseRecyclerStringAdapter(context, mUtilsBean)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        mRecyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> return
                    1 -> ActivityUtil.gotoActivity(context, GlideActivity::class.java)
                    2 -> ActivityUtil.gotoActivity(context, TextUtilsActivity::class.java)
                    3 -> {
                        val dialog = AlertDialog.Builder(context)
                                .setContentView(R.layout.dialog_bitmap_utils)
                                .setFullWidth()
                                .show()
                        val imageView = dialog.getView<ImageView>(R.id.iv_bitmap)
                        val bitmap = BitmapUtils.createBitmapFromView(getRootView())
                        imageView?.setImageBitmap(bitmap)
                    }
                    else -> {
                    }
                }
            }
        })
    }
}
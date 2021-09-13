package com.peakmain.basicui.activity.utils

import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.data.PesudoImageData
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.imageLoader.ImageLoader
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/9/14
 * mail:2726449200@qq.com
 * describe：
 */
class GlideActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mBean: MutableList<String>
    private var mAdapter: BaseRecyclerStringAdapter? = null
    private var mImageView: ImageView? = null
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        mImageView = findViewById(R.id.imageView)
   mNavigationBuilder!!.setToolbarBackgroundColor(R.color.ui_color_01a8e3)
                .create()
    }

    override fun initData() {
        mBean = ArrayList()
        mBean.add("glide简单使用")
        mBean.add("glide默认占位图")
        mBean.add("glide圆角图片")
        mBean.add("glide指定图片的大小")
        mAdapter = BaseRecyclerStringAdapter(this, mBean)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        mRecyclerView!!.adapter = mAdapter
        val data = PesudoImageData.instance.data
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> ImageLoader.instance!!.displayImage(this@GlideActivity, data[0].url!!, mImageView)
                    1 -> ImageLoader.instance!!.displayImage(this@GlideActivity, data[1].url!!, mImageView, R.mipmap.ic_default_portrait)
                    2 -> ImageLoader.instance!!.displayImageRound(this@GlideActivity, data[2].url!!, mImageView!!, 50, 0)
                    3 -> ImageLoader.instance!!.displayImage(this@GlideActivity, data[4].url!!, mImageView!!, 800, 800, 0)
                    else -> {
                    }
                }
            }
        })
    }
}
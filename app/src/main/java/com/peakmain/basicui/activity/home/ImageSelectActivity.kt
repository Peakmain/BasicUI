package com.peakmain.basicui.activity.home

import android.support.v7.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.image.PictureSelector
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.entry.SelectImageFileEntity

/**
 * author ：Peakmain
 * createTime：2021/4/18
 * mail:2726449200@qq.com
 * describe：
 */
class ImageSelectActivity : BaseActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private var originList: ArrayList<SelectImageFileEntity>? = ArrayList()
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {
        PictureSelector.create(this)
                .maxSelectNumber(9)
                .origin(originList)
                .forResult(object : PictureFileResultCallback {
                    override fun onResult(result: ArrayList<SelectImageFileEntity>?) {
                        originList = result

                    }

                })
    }
}
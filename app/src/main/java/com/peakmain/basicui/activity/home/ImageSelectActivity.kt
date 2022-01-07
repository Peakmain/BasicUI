package com.peakmain.basicui.activity.home

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.image.PicturePreview
import com.peakmain.ui.image.PictureSelector
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.imageLoader.ImageLoader
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.recyclerview.listener.OnItemClickListener

/**
 * author ：Peakmain
 * createTime：2021/4/18
 * mail:2726449200@qq.com
 * describe：
 */
class ImageSelectActivity : BaseActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private var originList: java.util.ArrayList<PictureFileInfo>?= ArrayList()
    private lateinit var mAdapter: ImageSelectAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_image_select
    }

    override fun initView() {
        mNavigationBuilder!!.setTitleText("图片选择库的使用")
                .create()
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    override fun initData() {

        mAdapter = ImageSelectAdapter(this@ImageSelectActivity, originList!!)
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int) {
                PicturePreview.create(activity = this@ImageSelectActivity)
                        .origin(originList = originList!!)
                        .previewPosition(position)
                        .forResult(null)
            }

        })
    }

    fun selectImageClick(view: View) {
        PictureSelector.create(this)
                .maxSelectNumber(9)
                .origin(originList)
                .single()
                .forResult(object : PictureFileResultCallback {
                    override fun onResult(result: ArrayList<PictureFileInfo>?) {
                        originList = result
                        mAdapter.setData(originList!!)

                    }

                })
    }

    class ImageSelectAdapter(context: Context, data: ArrayList<PictureFileInfo>) : CommonRecyclerAdapter<PictureFileInfo>(context, data, R.layout.recycler_view_image) {
        override fun convert(holder: ViewHolder, item: PictureFileInfo) {
            holder.setImageByUrl(R.id.iv_image, object : ViewHolder.HolderImageLoader(item.filePath!!) {
                override fun displayImage(context: Context?, imageView: ImageView?, imagePath: String?) {
                    ImageLoader.instance!!.displayImage(context!!, imagePath!!, imageView)
                }

            })
        }

    }
}
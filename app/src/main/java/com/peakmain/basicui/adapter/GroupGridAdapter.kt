package com.peakmain.basicui.adapter

import android.content.Context
import android.widget.ImageView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import com.peakmain.ui.imageLoader.ImageLoader.Companion.instance
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.recyclerview.group.BaseGridGroupRecyclerAdapter

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
class GroupGridAdapter(var context: Context, data: List<GroupBean?>?) : BaseGridGroupRecyclerAdapter<GroupBean>(context, data, R.layout.item_recycler_group) {
    override fun convert(holder: ViewHolder, item: GroupBean) {
        val imageView = holder.getView<ImageView>(R.id.iv_image)
        instance!!.displayImage(mContext!!, item.url!!, imageView, 0)
    }

}
package com.peakmain.basicui.adapter

import android.content.Context
import android.widget.ImageView
import com.peakmain.basicui.R
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import com.peakmain.ui.imageLoader.ImageLoader.Companion.instance
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.recyclerview.group.BaseGroupRecyclerAdapter

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
class GroupLinearAdapter(context: Context?, data: MutableList<GroupBean>) : BaseGroupRecyclerAdapter<GroupBean>(context, data, R.layout.item_recycler_group, R.layout.item_group_head) {
    override fun convertHead(holder: ViewHolder, item: GroupBean?) {
        holder.setText(R.id.tv_index, item?.header)
    }

    override fun convert(holder: ViewHolder, item: GroupBean) {
        val imageView = holder.getView<ImageView>(R.id.iv_image)
        instance!!.displayImage(mContext!!, item.url!!, imageView, 0)
    }

}
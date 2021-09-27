package com.peakmain.basicui.adapter

import android.content.Context
import com.peakmain.basicui.R
import com.peakmain.ui.imageLoader.ImageLoader
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.MultiTypeSupport
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2021/9/27
 * mail:2726449200@qq.com
 * describe：
 */
class MultiTypeRecyclerAdapter(context: Context?, data: MutableList<String>) : CommonRecyclerAdapter<String>(context, data, multiTypeSupport=object :MultiTypeSupport<String>{
    override fun getLayoutId(item: String, position: Int): Int {
        if(position%2==0&&position%3!=0){
            return R.layout.item_recyclerview_home
        }else if(position%3==0){
            return R.layout.recycler_view_image
        }
        return R.layout.item_flow_layout
    }
}){
    override fun convert(holder: ViewHolder, item: String) {
        val position = holder.adapterPosition
        when {
            position%2==0&&position%3!=0 -> {
                holder.setText(R.id.tv_title,item)
            }
            position%3==0 -> {
                ImageLoader.instance?.displayImage(mContext!!,item,holder.getView(R.id.iv_image))
            }
            else -> {
                holder.setText(R.id.tv_label,item)
            }
        }
    }

}
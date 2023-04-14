package com.peakmain.basicui.adapter

import android.content.Context
import com.peakmain.basicui.R
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2023/04/14
 * mail:2726449200@qq.com
 * describe：
 */
class MenuBrandAdapter(context: Context?, data: MutableList<String>) :
    CommonRecyclerAdapter<String>(context, data, R.layout.item_recycler_menu_brand) {
    override fun convert(holder: ViewHolder, item: String) {
        holder.setText(R.id.tv_brand, item)
    }
}
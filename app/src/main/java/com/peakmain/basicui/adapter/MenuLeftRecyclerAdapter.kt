package com.peakmain.basicui.adapter

import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import com.peakmain.basicui.R
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.utils.SizeUtils

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class MenuLeftRecyclerAdapter(context: Context?, data: List<String>) :
    CommonRecyclerAdapter<String>(context, data, R.layout.item_recyclerview_home) {
    override fun convert(holder: ViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
        holder.getView<TextView>(R.id.tv_title)
            ?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
    }

}
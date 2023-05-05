package com.peakmain.basicui.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.TextView
import com.peakmain.basicui.R
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class MenuLeftRecyclerAdapter(context: Context?, data: List<String>) :
    CommonRecyclerAdapter<String>(context, data, R.layout.item_recyclerview_home) {
    @JvmField
    var mSelectPosition = 0

    @JvmField
    var mOldSelectPosition = mSelectPosition
    override fun convert(holder: ViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
        val textView = holder.getView<TextView>(R.id.tv_title)
        textView
            ?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        if (holder.adapterPosition == mSelectPosition) {
            textView?.setTextColor(Color.parseColor("#6CBD9B"))
        } else {
            textView?.setTextColor(Color.parseColor("#272A2B"))
        }
    }

    fun setSelectItem(position: Int) {
        mSelectPosition = position
        notifyItemChanged(mOldSelectPosition)
        notifyItemChanged(mSelectPosition)
        mOldSelectPosition = position
    }
}
package com.peakmain.basicui.adapter

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peakmain.basicui.R
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.widget.RoundCheckBox

/**
 * author ：Peakmain
 * createTime：2023/04/14
 * mail:2726449200@qq.com
 * describe：
 */
class MenuRecommendSortAdapter(context: Context?, data: MutableList<String>) :
    CommonRecyclerAdapter<String>(
        context, data,
        R.layout.item_recycler_menu_recommend_sort
    ) {
    private var mOldSelectPosition = 0
    private var mSelectPosition=0
    override fun convert(holder: ViewHolder, item: String) {
        holder.setText(R.id.tv_recommend_sort, item)
        val tvRecommendSort = holder.getView<TextView>(R.id.tv_recommend_sort)
        val roundCheckBox = holder.getView<RoundCheckBox>(R.id.rcb_recommend_sort)
        if (holder.absoluteAdapterPosition == mSelectPosition) {
            mOldSelectPosition=mSelectPosition
            tvRecommendSort?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.color_01a8e3))
            }
            roundCheckBox?.visibility = View.VISIBLE
        } else {
            tvRecommendSort?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.color_272A2B))
            }
            roundCheckBox?.visibility = View.GONE
        }
        holder.setOnItemClickListener {
            val position = holder.absoluteAdapterPosition
            mSelectPosition=position
            notifyItemChanged(position)
            notifyItemChanged(mOldSelectPosition)
        }
    }
}
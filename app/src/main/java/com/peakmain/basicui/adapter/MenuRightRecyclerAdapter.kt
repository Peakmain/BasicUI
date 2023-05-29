package com.peakmain.basicui.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.bean.CategoryRightBean
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class MenuRightRecyclerAdapter(context: Context?, data: List<CategoryRightBean>) :
    CommonRecyclerAdapter<CategoryRightBean>(
        context,
        data,
        R.layout.item_recycler_menu_categroy_right
    ) {
    private var mSelectPosition: Int = 0
    private var mOldSelectPosition = mSelectPosition
    override fun convert(holder: ViewHolder, item: CategoryRightBean) {
        holder.setText(R.id.tv_title, item.title)
        val textView = holder.getView<TextView>(R.id.tv_title)
        if (holder.bindingAdapterPosition == mSelectPosition) {
            textView?.setTextColor(Color.parseColor("#6CBD9B"))
        } else {
            textView?.setTextColor(Color.parseColor("#272A2B"))
        }
        val recyclerView = holder.getView<RecyclerView>(R.id.rv_sub_content)
        recyclerView?.adapter = MenuSubRightRecyclerAdapter(mContext, item.categoryRightBeans)
    }
    fun setSelectItem(position: Int) {
        mSelectPosition = position
        if (mSelectPosition != mOldSelectPosition){
            notifyItemChanged(mOldSelectPosition)
            notifyItemChanged(mSelectPosition)
            mOldSelectPosition=position
        }

    }
}
package com.peakmain.ui.recyclerview.group

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：分组的recycleview适配器
 */
abstract class BaseGroupRecyclerAdapter<T : GroupRecyclerBean<T>?>(context: Context?, data: MutableList<T>, layoutResId: Int, var GROUP_HEADER_VIEW: Int) : CommonRecyclerAdapter<T>(context, data, GroupMultiType<T>(layoutResId, GROUP_HEADER_VIEW)) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == GROUP_HEADER_VIEW) {
            convertHead(holder, getItem(position))
        } else {
            super.onBindViewHolder(holder, holder.adapterPosition)
        }
    }

    /**
     * 解决GridLayoutManager添加不占用一行的问题
     */
    fun adjustSpanSize(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = getItemViewType(position)
                return if (itemViewType == GROUP_HEADER_VIEW) layoutManager.spanCount else 1
            }
        }
    }

    protected abstract fun convertHead(holder: ViewHolder, item: T?)

}
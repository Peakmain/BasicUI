package com.peakmain.ui.recyclerview.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.recyclerview.listener.OnLongClickListener

/**
 * author: peakmain
 * createdata：2019/7/17
 * mail: 2726449200@qq.com
 * desiption:添加头部和底部
 */
class WrapRecyclerAdapter(
        /**
         * 获取列表的Adapter
         */
        // 列表的Adapter
        private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * SparseArrays map integers to Objects.  Unlike a normal array of Objects,
     * there can be gaps in the indices.  It is intended to be more memory efficient
     * than using a HashMap to map Integers to Objects, both because it avoids
     * auto-boxing keys and its data structure doesn't rely on an extra entry object
     * for each mapping.
     */
    private val mHeaderViews: SparseArray<View>
    private val mFooterViews: SparseArray<View>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // viewType 可能就是 SparseArray 的key
        if (isHeaderViewType(viewType)) {
            val headerView = mHeaderViews[viewType]
            return createHeaderFooterViewHolder(headerView)
        }
        if (isFooterViewType(viewType)) {
            val footerView = mFooterViews[viewType]
            return createHeaderFooterViewHolder(footerView)
        }
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        // 计算一下位置
        val adapterPosition = position - mHeaderViews.size()
        adapter.onBindViewHolder(holder, adapterPosition)

        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(adapterPosition) }
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener { mLongClickListener!!.onLongClick(adapterPosition) }
        }
    }

    /**
     * 创建头部或者底部的ViewHolder
     */
    private fun createHeaderFooterViewHolder(view: View): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(view) {}
    }

    /**
     * 是不是头部类型
     */
    private fun isHeaderViewType(viewType: Int): Boolean {
        val position = mHeaderViews.indexOfKey(viewType)
        return position >= 0
    }

    /**
     * 是不是底部类型
     */
    private fun isFooterViewType(viewType: Int): Boolean {
        val position = mFooterViews.indexOfKey(viewType)
        return position >= 0
    }

    override fun getItemCount(): Int {
        // 条数三者相加 = 底部条数 + 头部条数 + Adapter的条数
        return adapter.itemCount + mHeaderViews.size() + mFooterViews.size()
    }

    override fun getItemViewType(position: Int): Int {
        var position = position
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return mHeaderViews.keyAt(position)
        }
        if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - mHeaderViews.size() - adapter.itemCount
            return mFooterViews.keyAt(position)
        }
        // 返回列表Adapter的getItemViewType
        position -= mHeaderViews.size()
        return adapter.getItemViewType(position)
    }

    /**
     * 是不是头部位置
     */
    private fun isHeaderPosition(position: Int): Boolean {
        return position < mHeaderViews.size()
    }

    /**
     * 是不是底部位置
     */
    private fun isFooterPosition(position: Int): Boolean {
        return position >= mHeaderViews.size() + adapter.itemCount
    }

    //添加头部
    fun addHeaderView(view: View) {
        val position = mHeaderViews.indexOfValue(view)
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view)
        }
        notifyDataSetChanged()
    }

    // 添加底部
    fun addFooterView(view: View) {
        val position = mFooterViews.indexOfValue(view)
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view)
        }
        notifyDataSetChanged()
    }

    // 移除头部
    fun removeHeaderView(view: View) {
        val index = mHeaderViews.indexOfValue(view)
        if (index < 0) return
        mHeaderViews.removeAt(index)
        notifyDataSetChanged()
    }

    // 移除底部
    fun removeFooterView(view: View) {
        val index = mFooterViews.indexOfValue(view)
        if (index < 0) return
        mFooterViews.removeAt(index)
        notifyDataSetChanged()
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     */
    fun adjustSpanSize(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is GridLayoutManager) {
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position)
                    return if (isHeaderOrFooter) layoutManager.spanCount else 1
                }
            }
        }
    }

    /**
     * 给条目设置点击和长按事件
     */
    var mItemClickListener: OnItemClickListener? = null
    var mLongClickListener: OnLongClickListener? = null
    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
    }

    fun setOnLongClickListener(longClickListener: OnLongClickListener?) {
        mLongClickListener = longClickListener
    }

    companion object {
        private const val TAG = "WrapRecyclerAdapter"

        // 基本的头部类型开始位置  用于viewType
        private var BASE_ITEM_TYPE_HEADER = 10000000

        // 基本的底部类型开始位置  用于viewType
        private var BASE_ITEM_TYPE_FOOTER = 20000000
    }

    init {
        mHeaderViews = SparseArray()
        mFooterViews = SparseArray()
    }
}
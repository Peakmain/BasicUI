package com.peakmain.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.recyclerview.listener.OnLongClickListener

/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2019/2/25
 * mail:2726449200@qq.com
 * describe：
 */
abstract class CommonRecyclerAdapter<T>(//上下文
    var mContext: Context?, data: List<T>, layoutId: Int
) : RecyclerView.Adapter<ViewHolder>() {
    protected var mInflater: LayoutInflater
    protected var mData //数据
            : MutableList<T>
    private var mLayoutId: Int

    //多布局支持
    private var mMultiTypeSupport: MultiTypeSupport<T>? = null
    protected var itemView: View? = null

    /**
     * 多布局支持
     */
    constructor(
        context: Context?,
        data: MutableList<T>,
        multiTypeSupport: MultiTypeSupport<T>
    ) : this(context, data, -1) {
        mMultiTypeSupport = multiTypeSupport
    }


    /**
     * 根据当前位置获取不同的viewType
     */
    override fun getItemViewType(position: Int): Int {
        return if (mMultiTypeSupport != null) {
            mMultiTypeSupport!!.getLayoutId(mData[position], position)
        } else position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType
        }
        itemView = mInflater.inflate(mLayoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(holder.adapterPosition) }
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener { mLongClickListener!!.onLongClick(holder.adapterPosition) }
        }
        convert(holder, mData[position])
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    abstract fun convert(holder: ViewHolder, item: T)
    override fun getItemCount(): Int {
        return mData.size
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

    fun getItem(position: Int): T? {
        return if (position >= 0 && position < mData.size) {
            mData[position]
        } else {
            null
        }
    }
    /**
     * 返回数据
     */
    val data: List<T>
        get() = mData

    fun setData(data: MutableList<T>) {
        mData = data
        notifyDataSetChanged()
    }

    fun setData(index: Int, data: T) {
        mData[index] = data
        notifyItemChanged(index)
    }
    /**
     * 添加数据
     */
    fun addData(data: T) {
        mData.add(data)
        notifyItemInserted(mData.size - 1)
    }

    fun addData(@androidx.annotation.IntRange(from = 0) position: Int, data: T) {
        mData.add(position, data)
        notifyItemInserted(position)
    }

    fun addData(datas: Collection<T>) {
        mData.addAll(datas)
        notifyItemRangeInserted(mData.size - datas.size, datas.size)
    }

    val dataSize: Int
        get() = mData.size

    /**
     * 替换数据
     */
    fun replaceData(newData: Collection<T>) {
        if (mData !== newData) {
            mData.clear()
            mData.addAll(newData)
        }
        notifyDataSetChanged()
    }

    /**
     * 移除数据
     */
    fun removeData(@IntRange(from = 0) position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }



    init {
        mInflater = LayoutInflater.from(mContext)
        mData = data as MutableList<T>
        mLayoutId = layoutId
    }
}
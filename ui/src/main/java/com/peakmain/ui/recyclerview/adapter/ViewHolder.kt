package com.peakmain.ui.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2019/2/25
 * mail:2726449200@qq.com
 * describe：
 */
class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    // 用来存放子View减少findViewById的次数
    private val mViews: SparseArray<View?>

    /**
     * 通过id获取view
     */
    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    /**
     * 设置TextView文本
     */
    fun setText(viewId: Int, text: CharSequence?): ViewHolder {
        val textView = getView<TextView>(viewId)!!
        textView.text = text
        return this
    }

    fun setTextColor(viewId: Int, color: Int): ViewHolder {
        val textView = getView<TextView>(viewId)!!
        textView.setTextColor(color)
        return this
    }

    /**
     * 设置view的Visibilty
     */
    fun setViewVisibility(viewId: Int, visibility: Int): ViewHolder {
        getView<View>(viewId)!!.visibility = visibility
        return this
    }

    /**
     * 设置ImageView的本地资源
     */
    fun setImageResource(viewId: Int, resourceId: Int): ViewHolder {
        val imageView = getView<ImageView>(viewId)!!
        imageView.setImageResource(resourceId)
        return this
    }

    fun setBackground(viewId: Int, resourceId: Int): ViewHolder {
        getView<View>(viewId)!!.setBackgroundResource(resourceId)
        return this
    }

    /**
     * 设置条目点击事件
     */
    fun setOnItemClickListener(listener: View.OnClickListener?) {
        itemView.setOnClickListener(listener)
    }

    /**
     * 设置条目某个view的点击事件
     */
    fun setOnItemClickListener(viewId: Int, listener: View.OnClickListener?) {
        getView<View>(viewId)!!.setOnClickListener(listener)
    }

    /**
     * 设置条目长按事件
     */
    fun setOnItemLongClickListener(listener: View.OnLongClickListener?) {
        itemView.setOnLongClickListener(listener)
    }

    /**
     * 设置条目某个view的长按事件
     */
    fun setOnItemLongClickListener(viewId: Int, listener: View.OnClickListener?) {
        getView<View>(viewId)!!.setOnClickListener(listener)
    }

    /**
     * 设置图片通过路径
     */
    fun setImageByUrl(viewId: Int, imageLoader: HolderImageLoader?): ViewHolder {
        val imageView = getView<ImageView>(viewId)!!
        if (imageLoader == null) {
            throw NullPointerException("imageLoader is null!")
        }
        imageLoader.displayImage(imageView.context, imageView, imageLoader.imagePath)
        return this
    }

    /**
     * 交给第三方自己处理
     */
    abstract class HolderImageLoader(val imagePath: String) {

        abstract fun displayImage(context: Context?, imageView: ImageView?, imagePath: String?)

    }

    init {
        mViews = SparseArray()
    }
}
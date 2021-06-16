package com.peakmain.ui.imageLoader

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.imageLoader.ImageLoader.Companion.instance

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：recylcerview在空闲的时候加载图片
 */
class ImageRecyclerScrollListener(private val mContext: Context) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        //空闲的时候加载图片
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            instance!!.resumeRequest(mContext)
        } else {
            instance!!.pauseRequest(mContext)
        }
    }

}
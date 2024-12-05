package com.peakmain.ui.recyclerview.manager

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.recyclerview.itemdecoration.CenterHorizontalDecoration

/**
 * author ：Peakmain
 * createTime：2024/11/27
 * mail:2726449200@qq.com
 * describe：
 */
class RecyclerViewManager {
    /**
     * 页面缩放
     * @param recyclerView 若为null，不做处理
     * @param scaleX 默认是0.9f
     * @param scaleY 默认是0.9f
     * @param space 设置RecyclerView子视图的边距默认是0
     */
    fun zoomOutTransformer(
        recyclerView: RecyclerView?,
        scaleX: Float = 0.9f,
        scaleY: Float = 0.9f,
        space: Int = 0,
    ) {
        recyclerView?.let {
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(it)
            it.addItemDecoration(CenterHorizontalDecoration(space))
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager ?: return
                    val currentView = pagerSnapHelper.findSnapView(layoutManager) ?: return
                    val currentPosition = layoutManager.getPosition(currentView)
                    for (i in 0 until recyclerView.childCount) {
                        val childView = recyclerView.getChildAt(i)
                        val childPosition = layoutManager.getPosition(childView)
                        if (childPosition == currentPosition - 1 || childPosition == currentPosition + 1) {
                            // 这里实现缩放80%的效果，通过设置ScaleX和ScaleY属性，可根据需求调整动画等更平滑过渡
                            childView.scaleX = scaleX
                            childView.scaleY = scaleY
                        } else {
                            childView.scaleX = 1.0f
                            childView.scaleY = 1.0f
                        }
                    }
                }
            })
        }
    }
}
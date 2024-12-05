package com.peakmain.ui.recyclerview.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * author ：Peakmain
 * createTime：2024/11/27
 * mail:2726449200@qq.com
 * describe：水平第一个和最后一个居中
 */

class CenterHorizontalDecoration(space: Int = 0) : RecyclerView.ItemDecoration() {
    private var space = 0

    /**
     * 第一个视图和最后一个视图偏移的距离
     */
    private var distance = 0

    /**
     * 设置RecyclerView子视图的边距
     */
    init {
        this.space = space
    }

    /**
     * 获取子视图的边距
     * @param view 子视图
     * @param parent RecyclerView对象
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val pos = parent.getChildAdapterPosition(view)
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        /**
         * 仅计算一次偏移边距即可，无需重复计算<P></P>
         * 由于此时View并未完成测量，无法基于测量获取其宽度;思路是在view绘制完成后再进行测量，并设置第一个的左边距
         */
        if (distance <= 0) {
            view.post {
                distance = pkDistance(parent, view)
                //设置第一个视图的左边距
                val childView = parent.getChildAt(0)
                val params = childView.layoutParams as RecyclerView.LayoutParams
                params.setMargins(distance, 0, space, 0)
                childView.layoutParams = params
                //打开后默认显示第一个（居中显示）
                parent.scrollToPosition(0)
            }
        }
        /**
         * 通过设置Item左右边距实现第一个左侧和最后一个右侧设置边距,确保显示的视图位于屏幕中间
         */
        val itemCount = parent.adapter!!.itemCount
        when (pos) {
            0 -> {
                layoutParams.setMargins(distance, 0, space, 0)
            }

            itemCount - 1 -> {
                layoutParams.setMargins(space, 0, distance, 0)
            }

            else -> {
                layoutParams.setMargins(space, 0, space, 0)
            }
        }
        /**
         * 更新子视图的边距
         */
        view.layoutParams = layoutParams
        super.getItemOffsets(outRect, view, parent, state)
    }

    /**
     * 为了使第一个和最后一个item居中，需要设置相应偏移，偏移量为RecyclerView布局宽度减去子视图的一半<P></P>
     * 注意此处由于子视图并未实例化完成，无法通过测量得知其宽度,故需要直接获取布局宽度参数得知<P></P>
     */
    private fun pkDistance(recyclerView: RecyclerView, childView: View): Int {
        val width = if (recyclerView.width != 0) recyclerView.width else recyclerView.measuredWidth
        //此处需要获取子视图布局的宽度，注意此处由于子视图并未实例化完成，无法通过测量得知其宽度
        childView.measuredWidth
        val childWidth = childView.width
        //第一个视图左侧偏移量，最后一个视图右侧偏移量
        return width / 2 - childWidth / 2
    }
}

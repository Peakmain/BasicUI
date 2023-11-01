package com.peakmain.ui.recyclerview.smooth

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * author ：Peakmain
 * createTime：2023/11/1
 * mail:2726449200@qq.com
 * describe：
 */
class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
    override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {
        val layoutManager = layoutManager
        if (layoutManager == null || !layoutManager.canScrollVertically()) {
            return 0
        }
        val params = view.layoutParams as RecyclerView.LayoutParams
        val top = layoutManager.getDecoratedTop(view) - params.topMargin
        val bottom = layoutManager.getDecoratedBottom(view) + params.bottomMargin
        val start = layoutManager.paddingTop
        val end = layoutManager.height - layoutManager.paddingBottom
        return calculateDtToFit(top, bottom, start, end, snapPreference)
    }

    override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
        val layoutManager = layoutManager
        if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
            return 0
        }
        val params = view.layoutParams as RecyclerView.LayoutParams
        val left = layoutManager.getDecoratedLeft(view) - params.leftMargin
        val right = layoutManager.getDecoratedRight(view) + params.rightMargin
        val start = layoutManager.paddingLeft
        val end = layoutManager.width - layoutManager.paddingRight
        return calculateDtToFit(left, right, start, end, snapPreference)
    }

    override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
        return when (snapPreference) {
            SNAP_TO_START -> boxStart - viewStart
            SNAP_TO_END -> boxEnd - viewEnd
            SNAP_TO_ANY -> {
                val dtStart = boxStart - viewStart
                val dtEnd = boxEnd - viewEnd
                (dtStart + dtEnd) / 2
            }

            else -> throw IllegalArgumentException(
                "snap preference should be one of the"
                        + " constants defined in SmoothScroller, starting with SNAP_"
            )
        }
    }

    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_ANY
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_ANY
    }
}
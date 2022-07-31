package com.peakmain.ui.recyclerview.itemdecoration

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DividerGridItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable?
    private val attrs = intArrayOf(
        R.attr.listDivider
    )

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
        drawHorizontal(c, parent)
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        if (mDivider == null) return
        // 绘制水平间隔线
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as GridLayoutManager.LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin + mDivider.intrinsicWidth
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        if (mDivider == null) return
        //绘制垂直间隔线(垂直的矩形)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as GridLayoutManager.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider.intrinsicWidth
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
        // 去掉右边的分割线
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mDivider == null) return
        // 四个方向的偏移值
        var right = mDivider.intrinsicWidth
        var bottom = mDivider.intrinsicHeight
        val position = (view.layoutParams as GridLayoutManager.LayoutParams).viewLayoutPosition
        if (isLastColumn(position, parent)) { // 是否是最后一列
            right = 0
        }
        if (isLastRow(position, parent)) { // 是否是最后一行
            bottom = 0
        }
        outRect[0, 0, right] = bottom
    }

    /**
     * 是否是最后一列
     */
    fun isLastColumn(itemPosition: Int, parent: RecyclerView): Boolean {
        val spanCount = getSpanCount(parent)
        return if ((itemPosition + 1) % spanCount == 0) {
            true
        } else {
            false
        }
    }

    /**
     * 是否是最后一行
     */
    fun isLastRow(itemPosition: Int, parent: RecyclerView): Boolean {
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter!!.itemCount
        val rowNumber =
            if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
        return itemPosition > (rowNumber - 1) * spanCount - 1
    }

    /**
     * 获取一行有多少列
     */
    fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            // 获取一行的spanCount
            return layoutManager.spanCount
        }
        return 1
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs)
        mDivider = typedArray.getDrawable(0)
        typedArray.recycle()
    }
}
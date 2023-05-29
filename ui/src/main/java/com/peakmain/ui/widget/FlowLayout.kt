package com.peakmain.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.peakmain.ui.adapter.flow.BaseFlowAdapter
import com.peakmain.ui.adapter.flow.FlowObserver
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：自定义view之流式布局
 */
class FlowLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val mChildViews: MutableList<List<View>> = ArrayList()
    private var mAdapter: BaseFlowAdapter? = null

    // 是否可以滚动
    private var mScrollable = false

    // 测量得到的高度
    private var mMeasuredHeight = 0

    // 整个流式布局控件的实际高度
    private var mRealHeight = 0

    // 已经滚动过的高度
    private var mScrolledHeight = 0

    // 本次滑动开始的Y坐标位置
    private var mStartY = 0

    // 本次滑动的偏移量
    private var mOffsetY = 0

    // 在ACTION_MOVE中，视第一次触发为手指按下，从第二次触发开始计入正式滑动
    private var mPointerDown = false

    // 2.1 onMeasure() 指定宽高
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 清空集合
        mChildViews.clear()
        val childCount = childCount

        // 获取到宽度
        val width = MeasureSpec.getSize(widthMeasureSpec)
        mMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec)
        // 高度需要计算
        var height = paddingTop + paddingBottom

        // 一行的宽度
        var lineWidth = paddingLeft
        var childViews = ArrayList<View>()
        mChildViews.add(childViews)

        // 子View高度不一致的情况下
        var maxHeight = 0
        for (i in 0 until childCount) {

            // 2.1.1 for循环测量子View
            val childView = getChildAt(i)
            if (childView.visibility == View.GONE) {
                continue
            }

            //调用子View的onMeasure
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)

            // margin值 ViewGroup.LayoutParams 没有 就用系统的MarginLayoutParams
            // LinearLayout有自己的 LayoutParams  会复写一个非常重要的方法
            val params = childView.layoutParams as MarginLayoutParams
            if (lineWidth + (childView.measuredWidth + params.rightMargin + params.leftMargin) > width) {
                // 换行,累加高度  加上一行条目中最大的高度
                height += maxHeight
                lineWidth = childView.measuredWidth + params.rightMargin + params.leftMargin
                childViews = ArrayList()
                mChildViews.add(childViews)
            } else {
                lineWidth += childView.measuredWidth + params.rightMargin + params.leftMargin
                maxHeight =
                    (childView.measuredHeight + params.bottomMargin + params.topMargin).coerceAtLeast(
                        maxHeight
                    )
            }
            childViews.add(childView)
        }
        height += maxHeight
        mRealHeight = height
        mScrollable = mRealHeight > mMeasuredHeight
        // 2.1.2 根据子View计算和指定自己的宽高
        setMeasuredDimension(measuredWidth, height)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return super.generateLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 摆放子View
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left: Int
        var top = paddingTop
        var right: Int
        var bottom: Int
        for (childViews in mChildViews) {
            left = paddingLeft
            var maxHeight = 0
            for (childView in childViews) {
                if (childView.visibility == View.GONE) {
                    continue
                }
                val params = childView.layoutParams as MarginLayoutParams
                left += params.leftMargin
                val childTop = top + params.topMargin
                right = left + childView.measuredWidth
                bottom = childTop + childView.measuredHeight


                // 摆放
                childView.layout(left, childTop, right, bottom)
                // left 叠加
                left += childView.measuredWidth + params.rightMargin

                // 不断的叠加top值
                val childHeight = childView.measuredHeight + params.topMargin + params.bottomMargin
                maxHeight = maxHeight.coerceAtLeast(childHeight)
            }
            top += maxHeight
        }
    }

    private inner class AdapterDataSetObserver : FlowObserver() {
        override fun notifyDataChange() {
            notiftyDataChange()
        }
    }

    private var mObserver: AdapterDataSetObserver? = null
    fun notiftyDataChange() {
        setAdapter(mAdapter)
        requestLayout()
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    fun setAdapter(adapter: BaseFlowAdapter?) {
        if (adapter == null) {
            return
        }
        if (mAdapter != null && mObserver != null) {
            adapter.unregisterDataSetObserver()
        }
        // 清空所有子View
        removeAllViews()
        mAdapter = adapter
        mObserver = AdapterDataSetObserver()
        mAdapter!!.registerDataSetObserver(mObserver)
        // 获取数量
        val childCount = mAdapter!!.count
        for (i in 0 until childCount) {
            // 通过位置获取View
            val childView = mAdapter!!.getView(i, this)
            addView(childView)
        }
    }

    /**
     * 滚动事件的处理，当布局可以滚动（内容高度大于测量高度）时，对手势操作进行处理
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mScrollable) {
            val currY = event.y.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_MOVE -> if (!mPointerDown) {
                    mStartY = currY
                    mPointerDown = true
                } else {
                    mOffsetY = mStartY - currY
                    scrollTo(0, mScrolledHeight + mOffsetY)
                }
                MotionEvent.ACTION_UP -> {
                    mScrolledHeight += mOffsetY
                    if (mScrolledHeight + mOffsetY < 0) {
                        scrollTo(0, 0)
                        mScrolledHeight = 0
                    } else if (mScrolledHeight + mOffsetY + mMeasuredHeight > mRealHeight) {
                        scrollTo(0, mRealHeight - mMeasuredHeight)
                        mScrolledHeight = mRealHeight - mMeasuredHeight
                    }
                    mPointerDown = false
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> intercepted = false
            MotionEvent.ACTION_MOVE -> intercepted = true
        }
        return intercepted
    }
}
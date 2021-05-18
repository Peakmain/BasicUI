package com.peakmain.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller
import kotlin.math.abs

/**
 * author ：Peakmain
 * createTime：2021/5/18
 * mail:2726449200@qq.com
 * describe：侧滑菜单
 */
class SlideFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mContentView: View? = null
    private var mMenuView: View? = null
    private val mScroller: Scroller = Scroller(context)
    private var mContentWidth = 0
    private var mMenuWidht = 0
    private var mViewHeight = 0

    companion object {
        val TAG: String = SlideFrameLayout::class.java.simpleName
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mContentView = getChildAt(0)
        mMenuView = getChildAt(1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mContentWidth = mContentView!!.measuredWidth
        mMenuWidht = mMenuView!!.measuredWidth
        mViewHeight = measuredHeight
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mMenuView?.layout(mContentWidth, 0, mContentWidth + mMenuWidht, mViewHeight)
    }

    private var startX = 0f
    private var startY = 0f
    private var downX = 0f
    private var downY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                downX = startX
                startY = event.y
                downY = startY
                Log.e(TAG, "SlideFrameLayout-onTouchEvent-ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e(TAG, "SlideFrameLayout-onTouchEvent-ACTION_MOVE")
                val endX = event.x
                val endY = event.y
                val distanceX = endX - startX
                var scrollX = (scrollX - distanceX).toInt()
                if (scrollX < 0) {
                    scrollX = 0
                } else if (scrollX > mMenuWidht) {
                    scrollX = mMenuWidht
                }
                scrollTo(scrollX, scrollY)
                startX = event.x
                startY = event.y
                val dx = abs(endX - downX)
                val dy = abs(endY - downY)
                if (dx > dy && dx > 10) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                Log.e(TAG, "SlideFrameLayout-onTouchEvent-ACTION_UP")
                val scrollX = scrollX
                if (scrollX < mMenuWidht / 2) {
                    closeMenu()
                } else {
                    openMenu()
                }
            }
        }
        return true
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        var intercept = false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                downX = startX
                onStateChangeListenter?.onDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                startX = event.x
                val dx = abs(endX - downX)
                if (dx > 10) {
                    intercept = true
                }
            }
        }
        return intercept
    }

    /**
     * 打开menu
     */
    fun openMenu() {
        val distanceX = mMenuWidht - scrollX
        mScroller.startScroll(scrollX, scrollY, distanceX, scrollY)
        invalidate()
        onStateChangeListenter?.onOpen(this)
    }

    /**
     * 关闭menu
     */
    fun closeMenu() {
        val distanceX = 0 - scrollX
        mScroller.startScroll(scrollX, scrollY, distanceX, scrollY)
        invalidate() //强制刷新
        onStateChangeListenter?.onClose(this)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    interface OnStateChangeListenter {
        fun onClose(layout: SlideFrameLayout?)
        fun onDown(layout: SlideFrameLayout?)
        fun onOpen(layout: SlideFrameLayout?)
    }

    private var onStateChangeListenter: OnStateChangeListenter? = null

    /**
     * 设置SlideFrameLayout状态的监听
     * @param onStateChangeListenter
     */
    fun setOnStateChangeListenter(onStateChangeListenter: OnStateChangeListenter?) {
        this.onStateChangeListenter = onStateChangeListenter
    }
}
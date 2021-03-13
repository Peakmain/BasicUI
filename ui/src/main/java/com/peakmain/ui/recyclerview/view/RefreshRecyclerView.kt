package com.peakmain.ui.recyclerview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.peakmain.ui.recyclerview.creator.RefreshViewCreator

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/21 0021 下午 2:27
 * mail : 2726449200@qq.com
 * describe ：下拉刷新的RecyclerView
 */
open class RefreshRecyclerView : WrapRecyclerView {
    // 下拉刷新的辅助类
    private var mRefreshCreator: RefreshViewCreator? = null

    // 下拉刷新头部的高度
    private var mRefreshViewHeight = 0

    // 下拉刷新的头部View
    private var mRefreshView: View? = null

    // 手指按下的Y位置
    private var mFingerDownY = 0

    // 手指拖拽的阻力指数
    protected var mDragIndex = 0.35f

    // 当前是否正在拖动
    private var mCurrentDrag = false

    // 当前的状态
    private var mCurrentRefreshStatus = 0

    // 默认状态
    private val REFRESH_STATUS_NORMAL = 0x0011

    // 下拉刷新状态
    private val REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022

    // 松开刷新状态
    private val REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033

    // 正在刷新状态
    private val REFRESH_STATUS_REFRESHING = 0x0044

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    // 先处理下拉刷新，同时考虑刷新列表的不同风格样式，确保这个项目还是下一个项目都能用
    // 所以我们不能直接添加View，需要利用辅助类
    fun addRefreshViewCreator(refreshCreator: RefreshViewCreator?) {
        mRefreshCreator = refreshCreator
        addRefreshView()
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        super.setAdapter(adapter)
        addRefreshView()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->                 // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = ev.rawY.toInt()
            MotionEvent.ACTION_UP -> if (mCurrentDrag) {
                restoreRefreshView()
                return true
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    var count = 0

    /**
     * 重置当前刷新状态状态
     */
    private fun restoreRefreshView() {
        val currentTopMargin = (mRefreshView!!.layoutParams as MarginLayoutParams).topMargin
        var finalTopMargin = -mRefreshViewHeight + 1
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            count = 0
            finalTopMargin = 0
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING
            if (mRefreshCreator != null) {
                mRefreshCreator!!.onRefreshing()
            }
            if (mListener != null) {
                mListener!!.onRefresh()
                restLoadView()
            }
        }
        if (mCurrentDrag) {
            count = count + 1
            val distance = currentTopMargin - finalTopMargin
            // 回弹到指定位置
            val animator = ObjectAnimator.ofFloat(currentTopMargin.toFloat(), finalTopMargin.toFloat()).setDuration(distance.toLong())
            animator.addUpdateListener { animation ->
                val currentTopMargin = animation.animatedValue as Float
                setRefreshViewMarginTop(currentTopMargin.toInt())
            }
            if (!animator.isRunning) animator.start()
            mCurrentDrag = false
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                // 如果是在最顶部才处理，否则不需要处理
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING || mRefreshView == null || mRefreshCreator == null) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e)
                }

                // 解决下拉刷新自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(0)
                }

                // 获取手指触摸拖拽的距离
                val distanceY = ((e.rawY - mFingerDownY) * mDragIndex).toInt()
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY > 0) {
                    val marginTop = distanceY - mRefreshViewHeight
                    setRefreshViewMarginTop(marginTop)
                    updateRefreshStatus(distanceY)
                    mCurrentDrag = true
                    return false
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 重置加载更多的view
     */
    open fun restLoadView() {}

    /**
     * 更新刷新的状态
     */
    private fun updateRefreshStatus(distanceY: Int) {
        mCurrentRefreshStatus = if (distanceY <= 0) {
            REFRESH_STATUS_NORMAL
        } else if (distanceY < mRefreshViewHeight) {
            REFRESH_STATUS_PULL_DOWN_REFRESH
        } else {
            REFRESH_STATUS_LOOSEN_REFRESHING
        }
        if (mRefreshCreator != null) {
            mRefreshCreator!!.onPull(distanceY, mRefreshViewHeight, mCurrentRefreshStatus)
        }
    }

    /**
     * 添加头部的刷新View
     */
    private fun addRefreshView() {
        val adapter = adapter
        if (adapter != null && mRefreshCreator != null) {
            // 添加头部的刷新View
            val refreshView = mRefreshCreator!!.getRefreshView(context, this)
            if (refreshView != null) {
                addHeaderView(refreshView)
                mRefreshView = refreshView
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mRefreshView != null) {
            if (mRefreshViewHeight <= 0) {
                // 获取头部刷新View的高度
                mRefreshViewHeight = mRefreshView!!.measuredHeight
                if (mRefreshViewHeight > 0) {
                    // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1)
                }
            } else {
                if (!mCurrentDrag && mCurrentRefreshStatus == REFRESH_STATUS_NORMAL) {
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1)
                }
            }
        }
    }

    /**
     * 设置刷新View的marginTop
     */
    private fun setRefreshViewMarginTop(marginTop: Int) {
        var marginTop = marginTop
        val params = mRefreshView!!.layoutParams as MarginLayoutParams
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1
        }
        params.topMargin = marginTop
        mRefreshView!!.layoutParams = params
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    private fun canScrollUp(): Boolean {
        return if (Build.VERSION.SDK_INT < 14) {
            ViewCompat.canScrollVertically(this, -1) || this.scrollY > 0
        } else {
            ViewCompat.canScrollVertically(this, -1)
        }
    }

    /**
     * 停止刷新
     */
    fun onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL
        if (mRefreshCreator != null) {
            mRefreshCreator!!.onStopRefresh()
        }
        restoreRefreshView()
    }

    // 处理刷新回调监听
    private var mListener: OnRefreshListener? = null
    fun setOnRefreshListener(listener: OnRefreshListener?) {
        mListener = listener
    }

    interface OnRefreshListener {
        fun onRefresh()
    }
}
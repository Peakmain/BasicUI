package com.peakmain.ui.recyclerview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.peakmain.ui.recyclerview.creator.LoadViewCreator

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/21 0021 下午 3:13
 * mail : 2726449200@qq.com
 * describe ：下拉刷新上拉加载更多的RecyclerView
 */
class LoadRefreshRecyclerView : RefreshRecyclerView {
    // 上拉加载更多的辅助类
    private var mLoadCreator: LoadViewCreator? = null

    // 上拉加载更多头部的高度
    private var mLoadViewHeight = 0

    // 上拉加载更多的头部View
    private var mLoadView: View? = null

    // 手指按下的Y位置
    private var mFingerDownY = 0

    // 当前是否正在拖动
    private var mCurrentDrag = false

    // 当前的状态
    private var mCurrentLoadStatus = 0

    // 默认状态
    var LOAD_STATUS_NORMAL = 0x0011

    // 正在加载更多状态
    var LOAD_STATUS_LOADING = 0x0044

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    // 辅助类——加载列表的不同风格样式
    fun addLoadViewCreator(loadCreator: LoadViewCreator?) {
        mLoadCreator = loadCreator
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
                restoreLoadView()
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 重置当前加载更多状态
     */
    private fun restoreLoadView() {
        val currentBottomMargin = (mLoadView!!.layoutParams as MarginLayoutParams).bottomMargin
        val finalBottomMargin = 0
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING) {
            mCurrentLoadStatus = LOAD_STATUS_LOADING
            if (mLoadCreator != null) {
                if (mListener != null && mListener!!.isLoadMore) {
                    mLoadCreator!!.onLoading()
                } else {
                    mLoadCreator!!.onFinishLoadData()
                }
            }
            if (mListener != null) {
                if (mListener!!.isLoadMore) {
                    mListener!!.onLoad()
                }
            }
        }
        Log.e("TAG", "是否加载更多:" + mListener!!.isLoadMore)
        if (mListener != null && mLoadCreator != null && !mListener!!.isLoadMore) {
            mLoadCreator!!.onFinishLoadData()
        }
        if (mCurrentDrag) {
            val distance = currentBottomMargin - finalBottomMargin

            // 回弹到指定位置
            val animator = ObjectAnimator.ofFloat(currentBottomMargin.toFloat(), finalBottomMargin.toFloat()).setDuration(distance.toLong())
            animator.addUpdateListener { animation ->
                val currentTopMargin = animation.animatedValue as Float
                setLoadViewMarginBottom(currentTopMargin.toInt())
            }
            animator.start()
            mCurrentDrag = false
        }
    }

    /**
     * 重置加载的view
     */
    override fun restLoadView() {
        updateLoadStatus(1)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                // 如果是在最底部才处理，否则不需要处理
                if (canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING || mLoadCreator == null || mLoadView == null || !mListener!!.isLoadMore) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e)
                }
                mLoadViewHeight = mLoadView!!.measuredHeight
                // 解决上拉加载更多自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(adapter.itemCount - 1)
                }

                // 获取手指触摸拖拽的距离
                val distanceY = ((e.rawY - mFingerDownY) * mDragIndex).toInt()
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY < 0) {
                    setLoadViewMarginBottom(-distanceY)
                    updateLoadStatus(-distanceY)
                    mCurrentDrag = true
                    return true
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 更新加载的状态
     */
    private fun updateLoadStatus(distanceY: Int) {
        mCurrentLoadStatus = if (distanceY <= 0) {
            LOAD_STATUS_NORMAL
        } else if (distanceY < mLoadViewHeight) {
            LOAD_STATUS_PULL_DOWN_REFRESH
        } else {
            LOAD_STATUS_LOOSEN_LOADING
        }
        if (mLoadCreator != null) {
            mLoadCreator!!.onPull(distanceY, mLoadViewHeight, mCurrentLoadStatus)
        }
    }

    /**
     * 添加底部加载更多View
     */
    private fun addRefreshView() {
        val adapter = adapter
        if (adapter != null && mLoadCreator != null) {
            // 添加底部加载更多View
            val loadView = mLoadCreator!!.getLoadView(context, this)
            if (loadView != null) {
                addFooterView(loadView)
                mLoadView = loadView
            }
        }
    }

    /**
     * 设置加载View的marginBottom
     */
    fun setLoadViewMarginBottom(marginBottom: Int) {
        var marginBottom = marginBottom
        val params = mLoadView!!.layoutParams as MarginLayoutParams
        if (marginBottom < 0) {
            marginBottom = 0
        }
        params.bottomMargin = marginBottom
        mLoadView!!.layoutParams = params
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最底部
     */
    fun canScrollDown(): Boolean {
        return ViewCompat.canScrollVertically(this, 1)
    }

    /**
     * 停止加载更多
     */
    fun onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL
        if (mLoadCreator != null) {
            mLoadCreator!!.onStopLoad()
        }
        restoreLoadView()
    }

    // 处理加载更多回调监听
    private var mListener: OnLoadMoreListener? = null
    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        this.mListener = listener
    }


    interface OnLoadMoreListener {
        fun onLoad()
        val isLoadMore: Boolean
    }

    companion object {
        // 上拉加载更多状态
        @JvmField
        var LOAD_STATUS_PULL_DOWN_REFRESH = 0x0022

        // 松开加载更多状态
        @JvmField
        var LOAD_STATUS_LOOSEN_LOADING = 0x0033
    }
}
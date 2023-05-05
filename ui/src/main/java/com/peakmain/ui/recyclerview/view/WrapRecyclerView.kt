package com.peakmain.ui.recyclerview.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.peakmain.ui.R
import com.peakmain.ui.recyclerview.adapter.WrapRecyclerAdapter
import com.peakmain.ui.recyclerview.listener.OnItemClickListener

/**
 * author: peakmain
 * createdata：2019/7/17
 * mail: 2726449200@qq.com
 * desiption:
 */
open class WrapRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = androidx.recyclerview.R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyle) {
    // 包裹了一层的头部底部Adapter
    private var mWrapRecyclerAdapter: WrapRecyclerAdapter? = null

    // 这个是列表数据的Adapter
    private var mAdapter: Adapter<ViewHolder>? = null

    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    //view
    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null

    //布局的id
    private var mEmptyViewResId = 0
    private var mErrorViewResId = 0
    private var mLoadingViewResId = 0
    private var mNoNetworkViewResId = 0
    private var mInflater: LayoutInflater? = null
    private val mOtherIds = ArrayList<Int>()
    private val mStatusView = ArrayList<View?>()
    private var isShouldSpan = false
    private var mContextMenuInfo: RecyclerViewContextMenuInfo? = null
    private val mDataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            if (mAdapter == null) {
                return
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter!!.notifyDataSetChanged()
            }
            dataChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (mAdapter == null) return
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) mWrapRecyclerAdapter!!.notifyItemRemoved(
                positionStart
            )
            dataChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (mAdapter == null) {
                return
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter!!.notifyItemMoved(fromPosition, toPosition)
            }
            dataChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (mAdapter == null) {
                return
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter!!.notifyItemChanged(positionStart)
            }
            dataChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if (mAdapter == null) {
                return
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter!!.notifyItemChanged(positionStart, payload)
            }
            dataChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (mAdapter == null) {
                return
            }
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter!!.notifyItemInserted(positionStart)
            }
            dataChanged()
        }
    }

    /**
     * adapter数据改变的方法
     */
    private fun dataChanged() {
        if (mAdapter!!.itemCount == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView!!.visibility = View.VISIBLE
            }
        } else {
            if (mEmptyView != null) {
                mEmptyView!!.visibility = View.INVISIBLE
            }
        }
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyle, 0)
        mEmptyViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.layout_empty_view)
        mErrorViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.layout_error_view)
        mLoadingViewResId = a.getResourceId(
            R.styleable.MultipleStatusView_loadingView,
            R.layout.layout_loading_view
        )
        mNoNetworkViewResId = a.getResourceId(
            R.styleable.MultipleStatusView_noNetworkView,
            R.layout.layout_network_view
        )
        a.recycle()
        mInflater = LayoutInflater.from(context)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is GridLayoutManager || layout is StaggeredGridLayoutManager) {
            isShouldSpan = true
        }
        super.setLayoutManager(layout)
    }

    override fun getContextMenuInfo(): ContextMenu.ContextMenuInfo {
        return mContextMenuInfo ?: super.getContextMenuInfo()
    }

    override fun showContextMenuForChild(originalView: View?): Boolean {
        val longPressPosition = getChildBindingAdapterPosition(originalView)
        if (longPressPosition >= 0) {
            val longPressId = adapter?.getItemId(longPressPosition)
            mContextMenuInfo = RecyclerViewContextMenuInfo(longPressPosition, longPressId!!)
            return super.showContextMenuForChild(originalView)
        }
        return false
    }

    private fun getChildBindingAdapterPosition(child: View?): Int {
        if (child == null) {
            return -1
        }
        val holder = getChildViewHolder(child)
        return holder.adapterPosition
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (mAdapter != null) {
            mAdapter!!.unregisterAdapterDataObserver(mDataObserver)
            mAdapter = null
        }
        mAdapter = adapter
        mWrapRecyclerAdapter = if (mAdapter is WrapRecyclerAdapter) {
            adapter as WrapRecyclerAdapter?
        } else {
            WrapRecyclerAdapter(mAdapter!!)
        }
        super.setAdapter(mWrapRecyclerAdapter)
        // 注册一个观察者
        mAdapter!!.registerAdapterDataObserver(mDataObserver)

        if (isShouldSpan) {
            // 解决GridLayout添加头部和底部也要占据一行
            mWrapRecyclerAdapter!!.adjustSpanSize(this)
        }

        //加载数据页面
        if (mLoadingView != null && mLoadingView!!.visibility == View.VISIBLE) {
            mLoadingView!!.visibility = View.INVISIBLE
        }
        if (mItemClickListener != null) {
            mWrapRecyclerAdapter!!.setOnItemClickListener(mItemClickListener)
        }
        if (mLongClickListener != null) {
            mWrapRecyclerAdapter!!.setOnLongClickListener(mLongClickListener)
        }
    }

    // 添加头部
    fun addHeaderView(view: View?) {
        if (view == null) return
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        mWrapRecyclerAdapter?.addHeaderView(view)
    }

    // 添加底部
    fun addFooterView(view: View?) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter!!.addFooterView(view!!)
        }
    }

    // 移除头部
    fun removeHeaderView(view: View?) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter!!.removeHeaderView(view!!)
        }
    }

    // 移除底部
    fun removeFooterView(view: View?) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter!!.removeFooterView(view!!)
        }
    }

    private fun inflateView(layoutId: Int): View {
        return mInflater!!.inflate(layoutId, null)
    }

    /**
     * 添加一个空列表数据页面
     */
    fun showEmptyView() {
        if (mEmptyView != null) {
            showEmpty(mEmptyView!!, DEFAULT_LAYOUT_PARAMS)
        } else {
            showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS)
        }
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showEmpty(layoutId: Int, layoutParams: LayoutParams) {
        showEmpty(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private fun showEmpty(view: View, layoutParams: LayoutParams) {
        if (mEmptyView == null) {
            mEmptyView = view
            val emptyRetryView = mEmptyView!!.findViewById<View>(R.id.empty_retry_view)
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mEmptyView!!.id)
            val parent = parent as ViewGroup
            parent.addView(view, 0, layoutParams)
        }
        mStatusView.add(mEmptyView)
        showViewById(mEmptyView!!.id)
    }

    /**
     * 显示错误视图
     */
    fun showError() {
        if (mErrorView != null) {
            showError(mErrorView, DEFAULT_LAYOUT_PARAMS)
        } else {
            showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS)
        }
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showError(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showError(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showError(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        if (null == mErrorView) {
            mErrorView = view
            val errorRetryView = mErrorView!!.findViewById<View>(R.id.error_retry_view)
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mErrorView!!.id)
            val parent = parent as ViewGroup
            parent.addView(view, 0, layoutParams)
        }
        mStatusView.add(mErrorView)
        showViewById(mErrorView!!.id)
    }

    /**
     * 显示加载中视图
     */
    fun showLoading() {
        if (mLoadingView != null) {
            showLoading(mLoadingView, DEFAULT_LAYOUT_PARAMS)
        } else {
            showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS)
        }
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showLoading(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showLoading(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showLoading(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        if (null == mLoadingView) {
            mLoadingView = view
            mOtherIds.add(mLoadingView!!.id)
            val parent = parent as ViewGroup
            parent.addView(view, 0, layoutParams)
        }
        mStatusView.add(mLoadingView)
        showViewById(mLoadingView!!.id)
    }

    /**
     * 隐藏loadingView
     */
    fun hideLoading() {
        if (mLoadingView != null && mLoadingView!!.visibility == View.VISIBLE) showContentView()
    }

    /**
     * 显示无网络视图
     */
    fun showNoNetwork() {
        if (mNoNetworkView != null) {
            showNoNetwork(mNoNetworkView, DEFAULT_LAYOUT_PARAMS)
        } else {
            showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS)
        }
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showNoNetwork(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        if (null == mNoNetworkView) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView!!.findViewById<View>(R.id.no_network_retry_view)
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mNoNetworkView!!.id)
            val parent = parent as ViewGroup
            parent.addView(view, 0, layoutParams)
        }
        mStatusView.add(mNoNetworkView)
        showViewById(mNoNetworkView!!.id)
    }

    /**
     * 显示内容布局
     */
    fun showContentView() {
        val parent = parent as ViewGroup
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) View.INVISIBLE else View.VISIBLE
        }
        dataChanged()
    }

    private fun showViewById(viewId: Int) {
        visibility = View.INVISIBLE
        for (view in mStatusView) {
            if (view!!.id == viewId) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.INVISIBLE
            }
        }
    }

    //重试的点击事件
    private var mOnRetryClickListener: OnClickListener? = null

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: OnClickListener?) {
        mOnRetryClickListener = onRetryClickListener
    }

    /**
     * 添加一个正在加载数据的页面
     */
    fun addLoadingView(loadingView: View?) {
        mLoadingView = loadingView
        mLoadingView!!.visibility = View.VISIBLE
    }

    /**
     * 设置自定的空view
     *
     * @param emptyView 布局view
     */
    fun setEmptyView(emptyView: View?) {
        mEmptyView = emptyView
    }

    /**
     * 设置自定的空view
     *
     * @param emptyViewResId R.layout.xx
     */
    fun setEmptyView(emptyViewResId: Int) {
        mEmptyViewResId = emptyViewResId
    }

    /**
     * 设置自定义view的错误view
     *
     * @param errorView 布局view
     */
    fun setErrorView(errorView: View?) {
        mErrorView = errorView
    }

    /**
     * 设置自定义view的错误view
     *
     * @param errorViewResId 布局view的id
     */
    fun setErrorView(errorViewResId: Int) {
        mErrorViewResId = errorViewResId
    }

    /**
     * 设置自定义view的正在加载的view
     *
     * @param loadingView 布局view
     */
    fun setLoadingView(loadingView: View?) {
        mLoadingView = loadingView
    }

    /**
     * 设置自定义view的正在加载的view
     *
     * @param loadingViewResId 布局view的id
     */
    fun setLoadingView(loadingViewResId: Int) {
        mLoadingViewResId = loadingViewResId
    }

    /**
     * 设置自定义view的没有网络的view
     *
     * @param noNetworkView 布局view
     */
    fun setNoNetworkView(noNetworkView: View?) {
        mNoNetworkView = noNetworkView
    }

    /**
     * 设置自定义view的没有网络的view
     *
     * @param noNetworkViewResId 布局view的id
     */
    fun setNoNetworkView(noNetworkViewResId: Int) {
        mNoNetworkViewResId = noNetworkViewResId
    }

    var mItemClickListener: OnItemClickListener? = null
    var mLongClickListener: com.peakmain.ui.recyclerview.listener.OnLongClickListener? = null
    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        mItemClickListener = itemClickListener
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter!!.setOnItemClickListener(mItemClickListener)
        }
    }

    fun setOnLongListener(longClickListener: com.peakmain.ui.recyclerview.listener.OnLongClickListener?) {
        mLongClickListener = longClickListener
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter!!.setOnLongClickListener(mLongClickListener)
        }
    }

    companion object {
        /**
         * 默认设置是全屏
         */
        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        private class RecyclerViewContextMenuInfo(val position: Int, val id: Long) :
            ContextMenu.ContextMenuInfo
    }

    init {
        init(context, attrs, defStyle)
    }
}
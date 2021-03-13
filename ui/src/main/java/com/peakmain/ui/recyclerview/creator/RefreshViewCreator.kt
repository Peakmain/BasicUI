package com.peakmain.ui.recyclerview.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/21 0021 下午 2:26
 * mail : 2726449200@qq.com
 * describe ：下拉刷新的辅助类为了匹配所有效果
 */
abstract class RefreshViewCreator {
    /**
     * 获取下拉刷新的View
     *
     * @param context 上下文
     * @param parent  RecyclerView
     * @return 布局的View
     */
    abstract fun getRefreshView(context: Context, parent: ViewGroup): View?

    /**
     * 正在下拉
     *
     * @param currentDragHeight    当前拖动的高度
     * @param refreshViewHeight    总的刷新高度
     * @param currentRefreshStatus 当前状态
     */
    abstract fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int)

    /**
     * 正在刷新中
     */
    abstract fun onRefreshing()

    /**
     * 停止刷新
     */
    abstract fun onStopRefresh()
}
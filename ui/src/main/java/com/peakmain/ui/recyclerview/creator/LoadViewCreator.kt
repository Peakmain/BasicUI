package com.peakmain.ui.recyclerview.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/21 0021 下午 2:25
 * mail : 2726449200@qq.com
 * describe ：上拉加载更多的辅助类为了匹配所有效果
 */
abstract class LoadViewCreator {
    /**
     * 获取上拉加载更多的View
     *
     * @param context 上下文
     * @param parent  RecyclerView
     */
    abstract fun getLoadView(context: Context, parent: ViewGroup): View?

    /**
     * 正在上拉
     *
     * @param currentDragHeight 当前拖动的高度
     * @param loadViewHeight    总的加载高度
     * @param currentLoadStatus 当前状态
     */
    abstract fun onPull(currentDragHeight: Int, loadViewHeight: Int, currentLoadStatus: Int)

    /**
     * 正在加载中
     */
    abstract fun onLoading()

    /**
     * 停止加载
     */
    abstract fun onStopLoad()

    /**
     * 数据已经全部加载完成
     */
    abstract fun onFinishLoadData()
}
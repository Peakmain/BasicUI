package com.peakmain.ui.adapter

import android.view.View
import android.view.ViewGroup

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
abstract class BaseAdapter {
    /**
     * 1.多少item数量
     */
    abstract val count: Int

    /**
     * 2.获取view通过position
     */
    abstract fun getView(position: Int, parent: ViewGroup?): View?
}
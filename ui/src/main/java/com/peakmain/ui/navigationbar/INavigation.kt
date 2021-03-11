package com.peakmain.ui.navigationbar

import android.view.View
import android.view.ViewGroup

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/7/27  下午 3:14
 * mail : 2726449200@qq.com
 * describe ：
 */
interface INavigation {
    /**
     * 创建View
     */
    fun createNavigationBar()

    /**
     * 添加参数
     */
    fun attachNavigationParams()
    fun attachParent(navigationBarView: View?, parent: ViewGroup?)
}
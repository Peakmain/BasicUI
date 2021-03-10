package com.peakmain.ui.recyclerview.adapter

/**
 * author ：Peakmain
 * version : 1.0
 * createTime：2019/2/25
 * mail:2726449200@qq.com
 * describe：多布局支持
 */
interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    fun getLayoutId(item: T, position: Int): Int
}
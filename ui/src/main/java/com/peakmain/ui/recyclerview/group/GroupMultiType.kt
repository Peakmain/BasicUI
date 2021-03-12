package com.peakmain.ui.recyclerview.group

import com.peakmain.ui.recyclerview.adapter.MultiTypeSupport

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
internal class GroupMultiType<T : GroupRecyclerBean<T>?>(private val mLayoutResId: Int, private val mGroupHeadResId: Int) : MultiTypeSupport<T> {
    override fun getLayoutId(item: T, position: Int): Int {
        return if (item!!.isHeader) {
            mGroupHeadResId
        } else mLayoutResId
    }

}
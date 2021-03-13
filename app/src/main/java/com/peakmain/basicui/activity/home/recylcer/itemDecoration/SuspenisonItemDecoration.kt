package com.peakmain.basicui.activity.home.recylcer.itemDecoration

import android.content.Context
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import com.peakmain.ui.recyclerview.itemdecoration.BaseSuspenisonItemDecoration

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
class SuspenisonItemDecoration : BaseSuspenisonItemDecoration<GroupBean?> {
    constructor(builder: BaseSuspenisonItemDecoration.Builder<GroupBean?>) : super(builder) {}
    constructor(context: Context?, data: List<GroupBean?>?) : super(context, data!!) {}

    override fun getTopText(data: List<GroupBean?>?, position: Int): String? {
        return data!![position]?.time
    }

    class Builder(context: Context?, data: List<GroupBean?>?) : BaseSuspenisonItemDecoration.Builder<GroupBean?>(context!!, data!!) {
        override fun create(): BaseSuspenisonItemDecoration<*> {
            return SuspenisonItemDecoration(this)
        }
    }
}
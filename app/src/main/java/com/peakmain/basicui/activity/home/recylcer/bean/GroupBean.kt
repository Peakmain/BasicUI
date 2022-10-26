package com.peakmain.basicui.activity.home.recylcer.bean

import com.peakmain.ui.recyclerview.group.GroupRecyclerBean

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
class GroupBean : GroupRecyclerBean<GroupBean> {
    var url: String? = null
    var time: String? = null

    constructor(isHeader: Boolean, header: String?) : super(isHeader, header)
    constructor(url: String?, time: String?) {
        this.url = url
        this.time = time
    }

}
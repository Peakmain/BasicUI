package com.peakmain.ui.recyclerview.group

import java.io.Serializable

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
open class GroupRecyclerBean<T> : Serializable {
    @JvmField
    var isHeader = false

    @JvmField
    var header: String? = null

    constructor()
    constructor(isHeader: Boolean, header: String?) {
        this.isHeader = isHeader
        this.header = header
    }
}
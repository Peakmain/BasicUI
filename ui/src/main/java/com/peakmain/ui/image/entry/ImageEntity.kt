package com.peakmain.ui.image.entry

import android.text.TextUtils
import java.io.Serializable

/**
 * author:Peakmain
 * createTime:2021/3/25
 * mail:2726449200@qq.com
 * describe： 图片对象
 */
class ImageEntity(var path: String, var name: String, var time: Long) : Serializable {
    constructor() : this("", "", 0)

    var isSelect = false

    override fun equals(o: Any?): Boolean {
        if (o is ImageEntity) {
            return TextUtils.equals(path, o.path)
        }
        return false
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + time.hashCode()
        return result
    }

}
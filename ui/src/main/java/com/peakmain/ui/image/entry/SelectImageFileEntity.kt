package com.peakmain.ui.image.entry

import java.io.Serializable

/**
 * author ：Peakmain
 * createTime：2021/3/29
 * mail:2726449200@qq.com
 * describe：已选择图片和文件的实体类
 */
class SelectImageFileEntity(var type: String?, var path: String?) : Serializable {
    var size: Long = 0
    override fun equals(other: Any?): Boolean {
        if (other is SelectImageFileEntity) {
            return type.equals(other.type) && path.equals(other.path)
        }
        return false
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (path?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "SelectImageFileEntity(type=$type, path=$path)"
    }


}
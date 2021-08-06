package com.peakmain.ui.image.entry

import android.graphics.Bitmap
import android.widget.ImageView
import com.peakmain.ui.image.gif.GifHelper
import java.io.Serializable


/**
 * author:Peakmain
 * createTime:2021/3/26
 * mail:2726449200@qq.com
 * describe：File文件属性
 */
class PictureFileInfo : Serializable {
    constructor(filePath: String, name: String, time: Long, fileSize: Long) {
        this.filePath = filePath
        this.fileName = name
        this.time = time
        this.fileSize = fileSize
    }

    constructor(type: String?, path: String?) {
        this.filePath = path
        this.type = type
    }

    constructor() : this("", "", 0, 0)

    var isSelect = false

    /**
     * 文件名称
     */
    var fileName: String? = null

    /**
     * 文件路径
     */
    var filePath: String? = null

    /**
     * 如果是文件夹则表示文件夹下的数量，否则表示文件大小
     */
    var fileSize: Long = 0

    /**
     * 是否是文件夹
     */
    var isDirectory = false

    /**
     * 文件扩展属性
     */
    var suffix: String? = null

    /**
     * 文件类型
     */
    var type: String? = null

    /**
     * 时间
     */
    var time: Long = 0

    override fun equals(other: Any?): Boolean {
        if (other is PictureFileInfo) {
            return type.equals(other.type) && filePath.equals(other.filePath)
        }
        return false
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (filePath?.hashCode() ?: 0)
        return result
    }


}
data class GifPlayerMessage(val gifHelper: GifHelper, val bitmap: Bitmap, val imageView: ImageView, val delay: Int)
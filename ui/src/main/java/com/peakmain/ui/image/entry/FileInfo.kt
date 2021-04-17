package com.peakmain.ui.image.entry


/**
 * author:Peakmain
 * createTime:2021/3/26
 * mail:2726449200@qq.com
 * describe：File文件属性
 */
class FileInfo {
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

}
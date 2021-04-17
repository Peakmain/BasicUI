package com.peakmain.ui.image.`interface`

import com.peakmain.ui.image.entry.SelectImageFileEntity

/**
 * author ：Peakmain
 * createTime：2021/4/2
 * mail:2726449200@qq.com
 * describe：文件图片选择后的结果回调
 */
interface PictureFileResultCallback {
    fun onResult(result: ArrayList<SelectImageFileEntity>?)
}
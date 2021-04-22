package com.peakmain.ui.image.`interface`

import com.peakmain.ui.image.entry.PictureFileInfo

/**
 * author ：Peakmain
 * createTime：2021/4/6
 * mail:2726449200@qq.com
 * describe：
 */
interface PicturePreviewDownloadListener {
    fun onPicturePreviewDownload(imageEntity: PictureFileInfo)
}
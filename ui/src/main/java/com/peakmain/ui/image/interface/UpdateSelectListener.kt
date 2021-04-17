package com.peakmain.ui.image.`interface`

import java.io.File

/**
 * author ：Peakmain
 * createTime：2021/3/29
 * mail:2726449200@qq.com
 * describe：更新已经选择的接口
 */
interface UpdateSelectListener {
    fun selector()
    fun openCamera(file: File?)
}
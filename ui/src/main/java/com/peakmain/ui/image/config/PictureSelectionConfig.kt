package com.peakmain.ui.image.config

import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.entry.PictureFileInfo
import java.util.ArrayList

/**
 * author ：Peakmain
 * createTime：2021/3/30
 * mail:2726449200@qq.com
 * describe：选择配置
 */
class PictureSelectionConfig private constructor() {



    //文件选择类型
    var mimeType = PictureConfig.TYPE_IMAGE

    //图片选择模式  单选或者多选
    var selectionMode = PictureConfig.SINGLE

    //最多选择数量
    var maxSelectNumber = 9

    //是否显示拍照
    var showCamera = false

    //是否压缩图片
    var isCompress = true

    //是否开启预览图片
    var enablePreview = true

    //是否开启裁剪
    var enableCrop = false

    //九宫格列表列数
    var imageSpanCount = 4

    //是否显示动画
    var zoomAnim = true
    var mOriginData: ArrayList<PictureFileInfo>? = ArrayList()
    var mResultCallBack: PictureFileResultCallback?=null

    private object InstanceHolder {
        val INSTANCE = PictureSelectionConfig()
    }

    /**
     * 重置一下
     */
    private fun reset() {
        mimeType = PictureConfig.TYPE_IMAGE
        selectionMode = PictureConfig.MULTIPLE
        maxSelectNumber = 9
        showCamera = false
        isCompress = true
        enablePreview = true
        enableCrop = false
        imageSpanCount = 4
        zoomAnim = true
    }

    companion object {
        /**
         * 返回一个最新的实例对象
         * @return
         */
        val newInstance: PictureSelectionConfig
            get() = InstanceHolder.INSTANCE

        /**
         * 返回重置数据后的对象
         * @return
         */
        val restInstance: PictureSelectionConfig
            get() {
                val selectionConfig =
                    newInstance
                selectionConfig.reset()
                return selectionConfig
            }
    }
}
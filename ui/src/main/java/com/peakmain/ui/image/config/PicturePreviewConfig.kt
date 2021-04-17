package com.peakmain.ui.image.config

import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.`interface`.PicturePreviewDownloadListener
import com.peakmain.ui.image.entry.ImageEntity
import com.peakmain.ui.image.entry.SelectImageFileEntity
import java.util.ArrayList

/**
 * author ：Peakmain
 * createTime：2021/3/30
 * mail:2726449200@qq.com
 * describe：预览选择配置
 */
class PicturePreviewConfig private constructor() {

    //下载的点击事件
    var mDownloadListener: PicturePreviewDownloadListener? = null

    //结果回调
    var mResultCallBack: PictureFileResultCallback? = null
    //选择的本地文件喝图片集合
    var selectImageFileLists: ArrayList<SelectImageFileEntity>? = ArrayList()
    var previewPosition: Int = 0
    var urlLists: ArrayList<String>? = ArrayList()

    //本地图片的集合
    var imageFileLists: ArrayList<ImageEntity?> = ArrayList()


    //是否显示底部的view
    var showBottomView: Boolean = false

    //是否显示标题右边的选择按钮
    var showTitleRightIcon: Boolean = false

    //设置标题的前缀
    var titlePrefix: CharSequence = ""

    //是否显示标题栏的返回键
    var showTitleLeftBack: Boolean = false

    //是否显示下载
    var showDownload: Boolean = false

    //设置是否显示关闭
    var showClose: Boolean = false

    private object InstanceHolder {
        val INSTANCE = PicturePreviewConfig()
    }

    /**
     * 重置一下
     */
    private fun reset() {
        showTitleLeftBack = false
        titlePrefix = ""
        showTitleRightIcon = false
        showBottomView = false
        showDownload = false
        showClose = false
        imageFileLists = ArrayList()
        urlLists = ArrayList()
        previewPosition = 0
    }

    companion object {
        /**
         * 返回一个最新的实例对象
         * @return
         */
        val newInstance: PicturePreviewConfig
            get() = InstanceHolder.INSTANCE

        /**
         * 返回重置数据后的对象
         * @return
         */
        val restInstance: PicturePreviewConfig
            get() {
                val selectionConfig =
                    newInstance
                selectionConfig.reset()
                return selectionConfig
            }
    }
}
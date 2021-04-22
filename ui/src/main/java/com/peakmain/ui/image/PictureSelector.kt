package com.peakmain.ui.image

import android.app.Activity
import android.content.Intent
import com.peakmain.ui.R
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.config.PictureConfig
import com.peakmain.ui.image.config.PictureSelectionConfig
import com.peakmain.ui.image.entry.PictureFileInfo
import java.lang.ref.WeakReference
import java.util.*

/**
 * author:Peakmain
 * createTime:2021/3/25
 * mail:2726449200@qq.com
 * describe：图片选择的链式调用
 */
class PictureSelector private constructor(var activity: Activity) {
    //防止内存泄漏
    private var mActivityWeakReferences: WeakReference<Activity>
    private var mSelectionConfig: PictureSelectionConfig = PictureSelectionConfig.restInstance

    init {
        mActivityWeakReferences = WeakReference(activity)
    }

    companion object {
        fun create(activity: Activity): PictureSelector {
            return PictureSelector(activity)
        }
    }

    /**
     * 单选模式
     */
    fun single(): PictureSelector {
        mSelectionConfig.selectionMode = PictureConfig.SINGLE
        return this
    }

    /**
     * 多选模式
     */
    fun multi(): PictureSelector {
        mSelectionConfig.selectionMode = PictureConfig.MULTIPLE
        return this
    }

    /**
     * 设置可以选多少张图片
     */
    fun maxSelectNumber(count: Int): PictureSelector {
        mSelectionConfig.maxSelectNumber = count
        return this
    }

    /**
     * 是否显示相机
     */
    fun showCamera(showCamera: Boolean): PictureSelector {
        mSelectionConfig.showCamera = showCamera
        return this
    }

    /**
     * 原来选择好的图片
     */
    fun origin(originList: ArrayList<PictureFileInfo>?): PictureSelector {
        mSelectionConfig.mOriginData = originList
        return this
    }

    fun forResult(callback: PictureFileResultCallback) {
        mSelectionConfig.mResultCallBack = callback
        val activity = mActivityWeakReferences.get() ?: return
        val intent = Intent(activity, PictureSelectorActivity::class.java)
        if (mSelectionConfig.mOriginData != null) {
            intent.putExtra(PictureSelectorActivity.SELECT_RESULT_KEY, mSelectionConfig.mOriginData)
        }
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.ui_activity_enter_transition_anim, 0)
    }

}
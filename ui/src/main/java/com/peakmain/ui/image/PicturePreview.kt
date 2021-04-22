package com.peakmain.ui.image

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.peakmain.ui.R
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.`interface`.PicturePreviewDownloadListener
import com.peakmain.ui.image.config.PicturePreviewConfig
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.image.fragment.PictureSelectFragment
import java.lang.ref.WeakReference
import java.util.*

/**
 * author ：Peakmain
 * createTime：2021/4/6
 * mail:2726449200@qq.com
 * describe：图片预览的链式调用
 */
class PicturePreview {
    private var activity: Activity? = null

    //防止内存泄漏
    private var mActivityWeakReferences: WeakReference<Activity>? = null
    private var mPreviewConfig: PicturePreviewConfig = PicturePreviewConfig.restInstance

    private var fragment: Fragment? = null

    private constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    private constructor (activity: Activity) {
        this.activity = activity
        mActivityWeakReferences = WeakReference(activity)
    }

    companion object {
        fun create(activity: Activity): PicturePreview {
            return PicturePreview(activity)
        }

        fun create(activity: FragmentActivity): PicturePreview {
            return PicturePreview(activity)
        }

        fun create(fragment: Fragment): PicturePreview {
            return PicturePreview(fragment)
        }

        fun create(context: Context): PicturePreview {
            return when (context) {
                is FragmentActivity -> {
                    create(context)
                }
                is Activity -> {
                    create(context)
                }
                is Fragment -> {
                    create(fragment = context)
                }
                else -> throw IllegalAccessException("Context is an exception")
            }
        }


    }

    /**
     * 是否显示标题栏的返回键
     */
    fun showTitleLeftBack(showTitleLeftBack: Boolean): PicturePreview {
        mPreviewConfig.showTitleLeftBack = showTitleLeftBack
        return this
    }

    /**
     * 设置标题的前缀
     */
    fun setTitlePrefix(titlePrefix: CharSequence): PicturePreview {
        mPreviewConfig.titlePrefix = titlePrefix
        return this
    }

    /**
     * 是否显示标题右边的图片按钮
     */
    fun showTitleRightIcon(showTitleRightIcon: Boolean): PicturePreview {
        mPreviewConfig.showTitleRightIcon = showTitleRightIcon
        return this
    }

    /**
     * 设置是否显示底部的view
     */
    fun showBottomView(showBottomView: Boolean): PicturePreview {
        mPreviewConfig.showBottomView = showBottomView
        return this
    }

    /**
     * 设置是否显示下载
     */
    fun showDownload(showDownload: Boolean): PicturePreview {
        mPreviewConfig.showDownload = showDownload
        return this
    }

    fun setDownloadListener(listener: PicturePreviewDownloadListener): PicturePreview {
        mPreviewConfig.mDownloadListener = listener
        return this
    }

    fun showClose(showClose: Boolean): PicturePreview {
        mPreviewConfig.showClose = showClose
        return this
    }

    fun previewPosition(position: Int): PicturePreview {
        mPreviewConfig.previewPosition = position
        return this
    }

    /**
     * 设置原始图片的集合
     */
    fun origin(originList: ArrayList<PictureFileInfo>?): PicturePreview {
        mPreviewConfig.imageFileLists = originList
        return this
    }

    fun selectImageFileList(selectImageFileLists: ArrayList<PictureFileInfo>): PicturePreview {
        mPreviewConfig.selectImageFileLists = selectImageFileLists
        return this
    }

    /**
     * 设置网络图片的url集合
     */
    fun originUrl(originList: ArrayList<String>?): PicturePreview {
        mPreviewConfig.urlLists = originList
        return this
    }

    fun forResult(callback: PictureFileResultCallback?) {
        mPreviewConfig.mResultCallBack = callback
        if (fragment != null) {
            val intent = Intent(fragment!!.activity, PicturePreviewActivity::class.java)
            (fragment!! as PictureSelectFragment).startActivity(intent)
            fragment!!.activity?.overridePendingTransition(
                R.anim.ui_activity_enter_transition_anim,
                0
            )
            return
        } else if (mActivityWeakReferences != null) {
            val activity = mActivityWeakReferences!!.get() ?: return
            val intent = Intent(activity, PicturePreviewActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.ui_activity_enter_transition_anim, 0)
        }

    }
}
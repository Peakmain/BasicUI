package com.peakmain.ui.read

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.FrameLayout
import com.peakmain.ui.utils.FileUtils
import com.tencent.smtt.sdk.TbsReaderView

/**
 * author ：Peakmain
 * createTime：2021/4/16
 * mail:2726449200@qq.com
 * describe：文件阅读view
 */
class FileReadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TbsReaderView.ReaderCallback {
    private var mTbsReaderView: TbsReaderView? = null
    private var mContext: Context = context

    init {
        mTbsReaderView = getTbsReaderView(context)
        addView(mTbsReaderView, LayoutParams(-1, -1))
    }


    private fun getTbsReaderView(context: Context): TbsReaderView? {
        return TbsReaderView(context, this)
    }

    /**
     * 浏览文件
     */
    fun openFile(filePath: String): Boolean {
        if (!TextUtils.isEmpty(filePath)) {
            val bundle = Bundle()
            bundle.putString("filePath", filePath)
            bundle.putString(
                "tempPath",
                Environment.getExternalStorageDirectory()
                    .toString() + "/" + "TbsReaderTemp"
            )
            if (mTbsReaderView == null) {
                mTbsReaderView = getTbsReaderView(mContext)
            }
            val preOpen = mTbsReaderView!!.preOpen(FileUtils.getFileType(filePath), false)
            if (preOpen) {
                mTbsReaderView!!.openFile(bundle)
            }
            return preOpen
        }
        return false
    }



    /**
     * 避免第二次加载的时候无法预览
     */
    fun onDestory() {
        if (mTbsReaderView != null) {
            mTbsReaderView!!.onStop()
            mTbsReaderView = null
        }
    }
    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

    }
}
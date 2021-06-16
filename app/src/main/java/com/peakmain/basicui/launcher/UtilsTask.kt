package com.peakmain.basicui.launcher

import android.R
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.peakmain.basicui.utils.ToastUtils
import com.peakmain.ui.utils.launcher.task.Task

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
class UtilsTask : Task() {
    override fun run() {
        ToastUtils.setBgColor(ContextCompat.getColor(mContext!!, R.color.transparent))
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
        ToastUtils.setMsgColor(ContextCompat.getColor(mContext!!, com.peakmain.basicui.R.color.colorAccent))
    }
}
package com.peakmain.ui

import android.app.Application
import androidx.core.content.FileProvider
import com.peakmain.ui.constants.BasicUIUtils

/**
 * author ：Peakmain
 * createTime：2020/12/24
 * mail:2726449200@qq.com
 * describe：
 */
class BasicUIProvider : FileProvider() {
    override fun onCreate(): Boolean {
        BasicUIUtils.init(context?.applicationContext as Application?)
        return true
    }
}
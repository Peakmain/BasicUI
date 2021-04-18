package com.peakmain.ui.read

import android.content.Context
import com.peakmain.ui.utils.LogUtils
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback

/**
 * author ：Peakmain
 * createTime：2021/4/16
 * mail:2726449200@qq.com
 * describe：
 */
internal class X5WebViewManager private constructor() {

    /**
     * 初始化x5内核
     *
     * @param context
     */
    fun initX5Web(context: Context) {
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(context.applicationContext, object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                LogUtils.d("X5内核初始化:$arg0")
            }

            override fun onCoreInitFinished() {
                LogUtils.d("X5内核开始初始化完成")
            }
        })
    }

    companion object {
        var instance: X5WebViewManager? = null
            get() {
                if (field == null) {
                    synchronized(X5WebViewManager::class.java) {
                        if (field === field) {
                            field = X5WebViewManager()
                        }
                    }
                }
                return field
            }
            private set
    }
}

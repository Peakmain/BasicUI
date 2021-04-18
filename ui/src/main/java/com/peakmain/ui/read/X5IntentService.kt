package com.peakmain.ui.read

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.peakmain.ui.constants.BasicUIUtils

/**
 * author ：Peakmain
 * createTime：2021/4/16
 * mail:2726449200@qq.com
 * describe：
 */
class X5IntentService : IntentService(X5IntentService::class.java.canonicalName) {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, X5IntentService::class.java)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        X5WebViewManager.instance?.initX5Web(BasicUIUtils.application!!)
    }

}
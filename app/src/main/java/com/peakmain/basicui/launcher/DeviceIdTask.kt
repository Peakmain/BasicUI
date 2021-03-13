package com.peakmain.basicui.launcher

import android.content.Context
import android.telephony.TelephonyManager
import com.peakmain.basicui.App
import com.peakmain.ui.utils.launcher.task.Task

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
class DeviceIdTask : Task() {
    private var mDeviceId: String? = null
    override fun run() {
        // 真正自己的代码
        val tManager = mContext!!.getSystemService(
                Context.TELEPHONY_SERVICE) as TelephonyManager
        mDeviceId = tManager.deviceId
        val app = mContext as App
        app.deviceId = mDeviceId
    }
}
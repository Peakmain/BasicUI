package com.peakmain.basicui.launcher

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
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
        val tManager = mContext?.getSystemService(
                Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mDeviceId = tManager.deviceId
        val app = mContext as App?
        app?.deviceId = mDeviceId
    }
}
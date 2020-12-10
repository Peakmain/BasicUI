package com.peakmain.basicui.launcher;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.peakmain.basicui.App;
import com.peakmain.ui.utils.launcher.task.Task;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class DeviceIdTask extends Task {
    private String mDeviceId;

    @Override
    public void run() {
        // 真正自己的代码
        TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(
                Context.TELEPHONY_SERVICE);
        mDeviceId = tManager.getDeviceId();
        App app = (App) mContext;
        app.setDeviceId(mDeviceId);
    }
}

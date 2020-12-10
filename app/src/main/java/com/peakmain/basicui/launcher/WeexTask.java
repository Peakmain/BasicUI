package com.peakmain.basicui.launcher;

import android.app.Application;

import com.peakmain.ui.utils.launcher.task.Task;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class WeexTask extends Task {
    @Override
    public void run() {
        InitConfig config = new InitConfig.Builder().build();
        WXSDKEngine.initialize((Application) mContext, config);
    }
}

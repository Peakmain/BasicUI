package com.peakmain.basicui.launcher;

import android.util.Log;

import com.peakmain.basicui.App;
import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.launcher.task.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class JPushTask extends Task {
    @Override
    public List<Class<? extends Task>> dependsOn() {
        List<Class<? extends Task>> tasks = new ArrayList<>();
        tasks.add(DeviceIdTask.class);
        return tasks;
    }

    @Override
    public void run() {
        //模拟极光推送
        LogUtils.e("极光推送开始");
        App app = (App) mContext;
        LogUtils.e("极光推送获取id:",app.getDeviceId());
    }
}

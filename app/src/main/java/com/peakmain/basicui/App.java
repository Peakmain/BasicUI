package com.peakmain.basicui;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.peakmain.basicui.launcher.AMapTask;
import com.peakmain.basicui.launcher.DeviceIdTask;
import com.peakmain.basicui.launcher.JPushTask;
import com.peakmain.basicui.launcher.UtilsTask;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public class App extends Application {
    private static Application sApplication;
    private static final Handler APP_HANDLER = new Handler(Looper.getMainLooper());
    private String mDeviceId;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        TaskDispatcher.init(this);
        TaskDispatcher dispatcher = TaskDispatcher.createInstance();
        dispatcher.addTask(new AMapTask())
                .addTask(new UtilsTask())
                .addTask(new JPushTask())
                .addTask(new DeviceIdTask())
                .start();
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            APP_HANDLER.post(runnable);
        }
    }

    /**
     * 获取Application
     *
     * @return Application
     */
    public static Application getApp() {
        return sApplication;
    }

    public void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
    }

    public String getDeviceId() {
        return mDeviceId;
    }
}

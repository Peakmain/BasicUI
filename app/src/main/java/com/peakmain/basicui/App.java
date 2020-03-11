package com.peakmain.basicui;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
public class App extends Application {
    private static Application sApplication;
    private static final Handler APP_HANDLER       = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication=this;

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
}

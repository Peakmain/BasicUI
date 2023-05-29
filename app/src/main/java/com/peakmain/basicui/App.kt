package com.peakmain.basicui

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.peakmain.basicui.launcher.*
import com.peakmain.ui.utils.ActivityUtils
import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher.Companion.createInstance
import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher.Companion.init

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class App : Application() {
    var deviceId: String? = null
    override fun onCreate() {
        super.onCreate()
        app = this
        init(this)
        val dispatcher = createInstance()
        dispatcher.addTask(AMapTask())
                .addTask(UtilsTask())
                .addTask(JPushTask())
                .addTask(DeviceIdTask())
                .addTask(WeexTask())
                .start()
        ActivityUtils.mInstance.init(this)
        //异常初始化
       // CrashUtils.init()
    }

    companion object {
        /**
         * 获取Application
         *
         * @return Application
         */
        var app: Application? = null
            private set
        private val APP_HANDLER = Handler(Looper.getMainLooper())
        fun runOnUiThread(runnable: Runnable) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run()
            } else {
                APP_HANDLER.post(runnable)
            }
        }

    }
}
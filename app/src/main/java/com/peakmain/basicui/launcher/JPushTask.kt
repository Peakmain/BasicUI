package com.peakmain.basicui.launcher

import com.peakmain.basicui.App
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.launcher.task.Task
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
class JPushTask : Task() {
    override fun dependsOn(): List<Class<out Task?>?> {
        val tasks: MutableList<Class<out Task?>?> = ArrayList()
        tasks.add(DeviceIdTask::class.java)
        return tasks
    }

    override fun run() {
        //模拟极光推送
        LogUtils.e("极光推送开始")
        val app = mContext as App
        LogUtils.e("极光推送获取id:", app.deviceId)
    }
}
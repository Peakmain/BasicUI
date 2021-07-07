package com.peakmain.basicui.launcher

import android.os.Environment
import com.peakmain.ui.utils.crash.CrashUtils
import com.peakmain.ui.utils.file.LogFileUtils
import com.peakmain.ui.utils.launcher.task.MainTask

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
class MainUtilsTask : MainTask() {
    override fun run() {
        //异常初始化
        CrashUtils.init()
        //日志
        LogFileUtils.init(
                5,
                1024 * 1024,
                Environment.getExternalStorageDirectory().toString() + "/log"
        )
    }
}
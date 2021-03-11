package com.peakmain.ui.utils.launcher.stat

import com.peakmain.ui.utils.LogUtils
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
object TaskStat {
    @Volatile
    private var sCurrentSituation = ""
    private val sBeans: MutableList<TaskStatBean> = ArrayList()

    //完成的任务数量
    private var sTaskDoneCount = AtomicInteger()

    // 是否开启统计
    private const val sOpenLaunchStat = false
    @JvmStatic
    var currentSituation: String
        get() = sCurrentSituation
        set(currentSituation) {
            if (!sOpenLaunchStat) {
                return
            }
            LogUtils.normal("currentSituation   $currentSituation")
            sCurrentSituation = currentSituation
            setLaunchStat()
        }

    @JvmStatic
    fun markTaskDone() {
        sTaskDoneCount.getAndIncrement()
    }

    fun setLaunchStat() {
        val bean = TaskStatBean()
        bean.situation = sCurrentSituation
        bean.count = sTaskDoneCount.get()
        sBeans.add(bean)
        sTaskDoneCount = AtomicInteger(0)
    }
}
package com.peakmain.ui.utils.launcher.task

import android.os.Process
import android.support.annotation.IntRange
import java.util.concurrent.Executor

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：任务Task接口
 */
interface ITask {
    /**
     * 优先级
     */
    @IntRange(from = Process.THREAD_PRIORITY_FOREGROUND.toLong(), to = Process.THREAD_PRIORITY_LOWEST.toLong())
    fun priority(): Int

    /**
     * 运行
     */
    fun run()

    /**
     * Task执行所在的线程池
     */
    fun runOn(): Executor?

    /**
     * 依赖关系
     */
    fun dependsOn(): List<Class<out Task?>?>?

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     */
    fun needWait(): Boolean

    /**
     * 是否运行在主线程
     */
    fun runOnMainThread(): Boolean

    /**
     * 是否只在主线程执行
     */
    fun onlyInMainProcess(): Boolean

    /**
     * Task主任务执行完成之后需要执行的任务
     */
    val tailRunnable: Runnable?

    /**
     * 设置回掉
     */
    fun setTaskCallBack(callBack: TaskCallBack?)

    /**
     * 是否回掉
     */
    fun needCall(): Boolean
}
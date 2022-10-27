package com.peakmain.ui.utils.launcher.task

import android.os.Process
import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher
import com.peakmain.ui.utils.launcher.tools.DispatcherExecutor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：任务
 */
abstract class Task : ITask {
    // 是否正在等待
    @Volatile
    var isWaiting = false

    /**
     * 是否正在运行
     */
    // 是否正在执行
    @Volatile
    var isRunning = false

    // Task是否执行完成
    @Volatile
    var isFinished = false

    // Task是否已经被分发
    @Volatile
    var isSend = false
    @JvmField
    var mContext = TaskDispatcher.context

    // 当前Task依赖的Task数量（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
    private val mDepends = CountDownLatch(if (this.dependsOn() == null) 0 else this.dependsOn()!!.size)

    /**
     * 当前的Task等待，让依赖的Task先执行
     */
    fun waitToSatisfy() {
        try {
            mDepends.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 依赖Task执行完一个
     */
    fun satisfy() {
        mDepends.countDown()
    }

    /**
     * 是否需要尽快执行
     */
    fun needRunAsSoon(): Boolean {
        return false
    }

    /**
     * Task的优先级，运行在主线程则不要去改优先级
     */
    override fun priority(): Int {
        return Process.THREAD_PRIORITY_BACKGROUND
    }

    /**
     * Task执行在哪个线程池，默认在IO的线程池；
     */
    override fun runOn(): ExecutorService? {
        return DispatcherExecutor.iOExecutor
    }

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     */
    override fun needWait(): Boolean {
        return false
    }

    /**
     * 当前Task依赖的Task集合（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
     */
    override fun dependsOn(): List<Class<out Task?>?>? {
        return null
    }

    /**
     * 是否运行在主线程
     */
    override fun runOnMainThread(): Boolean {
        return false
    }

    /**
     * Task主任务执行完成之后需要执行的任务
     */
    override val tailRunnable: Runnable?
        get() = null

    /**
     * 设置回掉
     */
    override fun setTaskCallBack(callBack: TaskCallBack?) {}

    /**
     * 是否需要回掉
     */
    override fun needCall(): Boolean {
        return false
    }

    /**
     * 是否只在主进程，默认是
     */
    override fun onlyInMainProcess(): Boolean {
        return true
    }

}
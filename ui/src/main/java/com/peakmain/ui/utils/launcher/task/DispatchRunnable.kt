package com.peakmain.ui.utils.launcher.task

import android.os.Looper
import android.os.Process
import android.support.v4.os.TraceCompat
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher
import com.peakmain.ui.utils.launcher.stat.TaskStat.currentSituation
import com.peakmain.ui.utils.launcher.stat.TaskStat.markTaskDone

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：任务真正执行的地方
 */
class DispatchRunnable : Runnable {
    private val mTask: Task
    private var mTaskDispatcher: TaskDispatcher? = null

    constructor(task: Task) {
        mTask = task
    }

    constructor(task: Task, dispatcher: TaskDispatcher?) {
        mTask = task
        mTaskDispatcher = dispatcher
    }

    override fun run() {
        TraceCompat.beginSection(mTask.javaClass.simpleName)
        Process.setThreadPriority(mTask.priority())
        var startTime = System.currentTimeMillis()
        mTask.isWaiting = true
        mTask.waitToSatisfy()
        val waitTime = System.currentTimeMillis() - startTime
        startTime = System.currentTimeMillis()
        mTask.isRunning = true
        mTask.run()
        val tailRunnable = mTask.tailRunnable
        tailRunnable?.run()
        if (!mTask.needCall() || !mTask.runOnMainThread()) {
            //printTaskLog(startTime, waitTime);
            //更新完成数量
            markTaskDone()
            mTask.isFinished = true
            //处理完通知
            if (mTaskDispatcher != null) {
                mTaskDispatcher!!.satisfyChildren(mTask)
                mTaskDispatcher!!.markTaskDone(mTask)
            }
            LogUtils.i(mTask.javaClass.simpleName + " finish")
        }
        TraceCompat.endSection()
    }

    /**
     * 打印出来Task执行的日志
     *
     * @param startTime startTime
     * @param waitTime  waitTime
     */
    private fun printTaskLog(startTime: Long, waitTime: Long) {
        val runTime = System.currentTimeMillis() - startTime
        if (LogUtils.isDebug) {
            LogUtils.normal(mTask.javaClass.simpleName + "  wait " + waitTime + "    run "
                    + runTime + "   isMain " + (Looper.getMainLooper() == Looper.myLooper())
                    + "  needWait " + (mTask.needWait() || Looper.getMainLooper() == Looper.myLooper())
                    + "  ThreadId " + Thread.currentThread().id
                    + "  ThreadName " + Thread.currentThread().name
                    + "  Situation  " + currentSituation
            )
        }
    }
}
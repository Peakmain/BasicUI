package com.peakmain.ui.utils.launcher.task;

import android.os.Looper;
import android.os.Process;
import android.support.v4.os.TraceCompat;

import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher;
import com.peakmain.ui.utils.launcher.stat.TaskStat;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：任务真正执行的地方
 */
public class DispatchRunnable implements Runnable {
    private final Task mTask;
    private TaskDispatcher mTaskDispatcher;

    public DispatchRunnable(Task task) {
        this.mTask = task;
    }

    public DispatchRunnable(Task task, TaskDispatcher dispatcher) {
        mTask = task;
        mTaskDispatcher = dispatcher;
    }

    @Override
    public void run() {
        TraceCompat.beginSection(mTask.getClass().getSimpleName());
        Process.setThreadPriority(mTask.priority());
        long startTime = System.currentTimeMillis();

        mTask.setWaiting(true);
        mTask.waitToSatisfy();
        long waitTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        mTask.setRunning(true);
        mTask.run();

        Runnable tailRunnable = mTask.getTailRunnable();
        if (tailRunnable != null) {
            tailRunnable.run();
        }
        if (!mTask.needCall() || !mTask.runOnMainThread()) {
            //printTaskLog(startTime, waitTime);
            //更新完成数量
            TaskStat.markTaskDone();
            mTask.setFinished(true);
            //处理完通知
            if (mTaskDispatcher != null) {
                mTaskDispatcher.satisfyChildren(mTask);
                mTaskDispatcher.markTaskDone(mTask);
            }
            LogUtils.i(mTask.getClass().getSimpleName() + " finish");
        }
        TraceCompat.endSection();
    }

    /**
     * 打印出来Task执行的日志
     *
     * @param startTime startTime
     * @param waitTime  waitTime
     */
    private void printTaskLog(long startTime, long waitTime) {
        long runTime = System.currentTimeMillis() - startTime;
        if (LogUtils.isDebug()) {
            LogUtils.normal(mTask.getClass().getSimpleName() + "  wait " + waitTime + "    run "
                    + runTime + "   isMain " + (Looper.getMainLooper() == Looper.myLooper())
                    + "  needWait " + (mTask.needWait() || (Looper.getMainLooper() == Looper.myLooper()))
                    + "  ThreadId " + Thread.currentThread().getId()
                    + "  ThreadName " + Thread.currentThread().getName()
                    + "  Situation  " + TaskStat.getCurrentSituation()
            );
        }
    }
}

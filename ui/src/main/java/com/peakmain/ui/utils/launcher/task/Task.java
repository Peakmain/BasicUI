package com.peakmain.ui.utils.launcher.task;

import android.content.Context;
import android.os.Process;

import com.peakmain.ui.utils.launcher.dispatcher.TaskDispatcher;
import com.peakmain.ui.utils.launcher.tools.DispatcherExecutor;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：任务
 */
public abstract class Task implements ITask {
    // 是否正在等待
    private volatile boolean mIsWaiting;
    // 是否正在执行
    private volatile boolean mIsRunning;
    // Task是否执行完成
    private volatile boolean mIsFinished;
    // Task是否已经被分发
    private volatile boolean mIsSend;
    public Context mContext = TaskDispatcher.getContext();
    // 当前Task依赖的Task数量（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
    private CountDownLatch mDepends = new CountDownLatch(dependsOn() == null ? 0 : dependsOn().size());

    /**
     * 当前的Task等待，让依赖的Task先执行
     */
    public void waitToSatisfy() {
        try {
            mDepends.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 依赖Task执行完一个
     */
    public void satisfy() {
        mDepends.countDown();
    }

    /**
     * 是否需要尽快执行
     */
    public boolean needRunAsSoon() {
        return false;
    }

    /**
     * Task的优先级，运行在主线程则不要去改优先级
     */
    @Override
    public int priority() {
        return Process.THREAD_PRIORITY_BACKGROUND;
    }

    /**
     * Task执行在哪个线程池，默认在IO的线程池；
     */
    @Override
    public ExecutorService runOn() {
        return DispatcherExecutor.getIOExecutor();
    }

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     */
    @Override
    public boolean needWait() {
        return false;
    }

    /**
     * 当前Task依赖的Task集合（需要等待被依赖的Task执行完毕才能执行自己），默认没有依赖
     */
    @Override
    public List<Class<? extends Task>> dependsOn() {
        return null;
    }

    /**
     * 是否运行在主线程
     */
    @Override
    public boolean runOnMainThread() {
        return false;
    }

    /**
     * Task主任务执行完成之后需要执行的任务
     */
    @Override
    public Runnable getTailRunnable() {
        return null;
    }

    /**
     * 设置回掉
     */
    @Override
    public void setTaskCallBack(TaskCallBack callBack) {

    }

    /**
     * 是否需要回掉
     */
    @Override
    public boolean needCall() {
        return false;
    }

    /**
     * 是否只在主进程，默认是
     */
    @Override
    public boolean onlyInMainProcess() {
        return true;
    }

    /**
     * 是否正在运行
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    public void setRunning(boolean running) {
        mIsRunning = running;
    }

    public boolean isWaiting() {
        return mIsWaiting;
    }

    public void setWaiting(boolean waiting) {
        mIsWaiting = waiting;
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public void setFinished(boolean finished) {
        mIsFinished = finished;
    }

    public boolean isSend() {
        return mIsSend;
    }

    public void setSend(boolean send) {
        mIsSend = send;
    }
}

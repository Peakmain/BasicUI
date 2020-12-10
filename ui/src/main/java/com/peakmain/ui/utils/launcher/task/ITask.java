package com.peakmain.ui.utils.launcher.task;

import android.os.Process;
import android.support.annotation.IntRange;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：任务Task接口
 */
public interface ITask {
    /**
     * 优先级
     */
    @IntRange(from = Process.THREAD_PRIORITY_FOREGROUND, to = Process.THREAD_PRIORITY_LOWEST)
    int priority();

    /**
     * 运行
     */
    void run();

    /**
     * Task执行所在的线程池
     */
    Executor runOn();

    /**
     * 依赖关系
     */
    List<Class<? extends Task>> dependsOn();

    /**
     * 异步线程执行的Task是否需要在被调用await的时候等待，默认不需要
     */
    boolean needWait();

    /**
     * 是否运行在主线程
     */
    boolean runOnMainThread();

    /**
     * 是否只在主线程执行
     */
    boolean onlyInMainProcess();

    /**
     * Task主任务执行完成之后需要执行的任务
     */
    Runnable getTailRunnable();

    /**
     * 设置回掉
     */
    void setTaskCallBack(TaskCallBack callBack);

    /**
     * 是否回掉
     */
    boolean needCall();
}

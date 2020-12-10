package com.peakmain.ui.utils.launcher.dispatcher;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.UiThread;

import com.peakmain.ui.utils.HandlerUtils;
import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.launcher.sort.TaskSortUtil;
import com.peakmain.ui.utils.launcher.stat.TaskStat;
import com.peakmain.ui.utils.launcher.task.DispatchRunnable;
import com.peakmain.ui.utils.launcher.task.Task;
import com.peakmain.ui.utils.launcher.task.TaskCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：启动器调用类
 */
public class TaskDispatcher {
    private static final long WAITTIME = 10000;
    private long mStartTime;
    private static Context sContext;
    private static volatile boolean sHasInit;
    private static boolean sIsMainProcess;
    //所有任务的集合
    private List<Task> mAllTasks = new ArrayList<>();
    private List<Class<? extends Task>> mClsAllTasks = new ArrayList<>();
    //已经结束了的Task
    private volatile List<Class<? extends Task>> mFinishedTasks = new ArrayList<>(100);
    /**
     * 所有依赖的集合
     */
    private HashMap<Class<? extends Task>, ArrayList<Task>> mDependedHashMap = new HashMap<>();
    //调用了await的时候还没结束的且需要等待的Task
    private List<Task> mNeedWaitTasks = new ArrayList<>();
    //保存需要Wait的Task的数量
    private AtomicInteger mNeedWaitCount = new AtomicInteger();
    //启动器分析的次数，统计下分析的耗时；
    private AtomicInteger mAnalyseCount = new AtomicInteger();
    private CountDownLatch mCountDownLatch;
    /**
     * 所有主线程的任务
     */
    private volatile List<Task> mMainThreadTasks = new ArrayList<>();
    /**
     * 异步执行任务的集合
     */
    private List<Future> mFutures = new ArrayList<>();

    private TaskDispatcher() {
    }

    public static void init(Context context) {
        if (context != null) {
            sContext = context.getApplicationContext();
            sHasInit = true;
            sIsMainProcess = HandlerUtils.isMainProcess(context);
        }
    }

    public static TaskDispatcher createInstance() {
        if (!sHasInit) {
            throw new RuntimeException("must call TaskDispatcher.init first");
        }
        return new TaskDispatcher();
    }

    public TaskDispatcher addTask(Task task) {
        if (task != null) {
            collectDepends(task);
            mAllTasks.add(task);
            mClsAllTasks.add(task.getClass());
            // 非主线程且需要wait的，主线程不需要CountDownLatch也是同步的
            if (ifNeedWait(task)) {
                mNeedWaitTasks.add(task);
                mNeedWaitCount.getAndIncrement();

            }
        }
        return this;
    }

    /**
     * 判断是否需要等待
     */
    private boolean ifNeedWait(Task task) {
        return task.needWait() && !task.runOnMainThread();
    }

    @UiThread
    public void start() {
        mStartTime = System.currentTimeMillis();
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("必须在主线程中执行");
        }
        if (mAllTasks.size() > 0) {
            mAnalyseCount.getAndIncrement();
            //打印所有依赖的信息
            printDependedMsg();
            //所有依赖需要进行图排序
            mAllTasks = TaskSortUtil.getSortResult(mAllTasks, mClsAllTasks);
            mCountDownLatch = new CountDownLatch(mNeedWaitCount.get());
            sendAndExecuteAsyncTasks();
            executeTaskMain();
        }
    }

    /**
     * 执行主线程
     */
    private void executeTaskMain() {
        mStartTime = System.currentTimeMillis();
        for (Task task : mMainThreadTasks) {
            long time = System.currentTimeMillis();
            new DispatchRunnable(task).run();

        }
    }

    //执行
    private void sendAndExecuteAsyncTasks() {
        for (Task task : mAllTasks) {
            if (task.onlyInMainProcess() && !sIsMainProcess) {
                markTaskDone(task);
            } else {
                sendTaskReal(task);
            }
            task.setSend(true);
        }
    }

    private void sendTaskReal(final Task task) {
        if (task.runOnMainThread()) {
            mMainThreadTasks.add(task);
            //是否需要回掉
            if (task.needCall()) {
                task.setTaskCallBack(new TaskCallBack() {
                    @Override
                    public void call() {
                        TaskStat.markTaskDone();
                        task.setFinished(true);
                        satisfyChildren(task);
                        markTaskDone(task);
                        LogUtils.i(task.getClass().getSimpleName() + " finish");
                    }
                });
            }
        } else {
            // 直接发，是否执行取决于具体线程池
            Future<?> future = task.runOn().submit(new DispatchRunnable(task, this));
            mFutures.add(future);
        }
    }

    public void executeTask(Task task) {
        if (ifNeedWait(task)) {
            mNeedWaitCount.getAndIncrement();
        }
        task.runOn().execute(new DispatchRunnable(task, this));
    }

    /**
     * 搜集task相关所有依赖
     *
     * @param task 当前task
     */
    private void collectDepends(Task task) {
        if (task.dependsOn() != null && task.dependsOn().size() > 0) {
            for (Class<? extends Task> cls : task.dependsOn()) {
                if (mDependedHashMap.get(cls) == null) {
                    mDependedHashMap.put(cls, new ArrayList<Task>());
                }
                mDependedHashMap.get(cls).add(task);
                if (mFinishedTasks.contains(cls)) {
                    task.satisfy();
                }
            }
        }
    }

    /**
     * 通知Children一个前置任务已完成
     */
    public void satisfyChildren(Task task) {
        ArrayList<Task> tasks = mDependedHashMap.get(task.getClass());
        if (tasks != null && tasks.size() > 0) {
            for (Task finishTask : tasks) {
                finishTask.satisfy();
            }
        }
    }

    @UiThread
    public void await() {
        try {
            if (LogUtils.isDebug()) {
                LogUtils.i("still has " + mNeedWaitCount.get());
                for (Task task : mNeedWaitTasks) {
                    LogUtils.i("needWait: " + task.getClass().getSimpleName());
                }
            }

            if (mNeedWaitCount.get() > 0) {
                mCountDownLatch.await(WAITTIME, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
        }
    }

    /**
     * 执行完成的任务
     */
    public void markTaskDone(Task task) {
        if (ifNeedWait(task)) {
            mFinishedTasks.add(task.getClass());
            mNeedWaitTasks.remove(task);
            mCountDownLatch.countDown();
            mNeedWaitCount.getAndDecrement();
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static boolean isMainProcess() {
        return sIsMainProcess;
    }

    /**
     * 查看被依赖的信息
     */
    private void printDependedMsg() {

        if (false) {
            LogUtils.i("needWait size : " + (mNeedWaitCount.get()));
            for (Class<? extends Task> cls : mDependedHashMap.keySet()) {
                LogUtils.i("cls " + cls.getSimpleName() + "   " + mDependedHashMap.get(cls).size());
                for (Task task : mDependedHashMap.get(cls)) {
                    LogUtils.i("cls       " + task.getClass().getSimpleName());
                }
            }
        }
    }

}

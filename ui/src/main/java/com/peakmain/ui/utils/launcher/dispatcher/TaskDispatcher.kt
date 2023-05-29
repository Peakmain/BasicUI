package com.peakmain.ui.utils.launcher.dispatcher

import android.content.Context
import android.os.Looper
import androidx.annotation.UiThread
import com.peakmain.ui.utils.HandlerUtils.isMainProcess
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.launcher.sort.TaskSortUtil.getSortResult
import com.peakmain.ui.utils.launcher.stat.TaskStat.markTaskDone
import com.peakmain.ui.utils.launcher.task.DispatchRunnable
import com.peakmain.ui.utils.launcher.task.Task
import com.peakmain.ui.utils.launcher.task.TaskCallBack
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：启动器调用类
 */
class TaskDispatcher private constructor() {
    private var mStartTime: Long = 0

    //所有任务的集合
    private var mAllTasks: MutableList<Task> = mutableListOf()
    private val mClsAllTasks: MutableList<Class<out Task>> = ArrayList()

    //已经结束了的Task
    private val mFinishedTasks: MutableList<Class<out Task>> = ArrayList(100)

    /**
     * 所有依赖的集合
     */
    private val mDependedHashMap = HashMap<Class<out Task?>?, ArrayList<Task>>()

    //调用了await的时候还没结束的且需要等待的Task
    private val mNeedWaitTasks: MutableList<Task> = ArrayList()

    //保存需要Wait的Task的数量
    private val mNeedWaitCount = AtomicInteger()

    //启动器分析的次数，统计下分析的耗时；
    private val mAnalyseCount = AtomicInteger()
    private var mCountDownLatch: CountDownLatch? = null

    /**
     * 所有主线程的任务
     */
    private val mMainThreadTasks: MutableList<Task> = ArrayList()

    /**
     * 异步执行任务的集合
     */
    private val mFutures: MutableList<Future<*>> = ArrayList()
    fun addTask(task: Task?): TaskDispatcher {
        if (task != null) {
            collectDepends(task)
            mAllTasks.add(task)
            mClsAllTasks.add(task.javaClass)
            // 非主线程且需要wait的，主线程不需要CountDownLatch也是同步的
            if (ifNeedWait(task)) {
                mNeedWaitTasks.add(task)
                mNeedWaitCount.getAndIncrement()
            }
        }
        return this
    }

    /**
     * 判断是否需要等待
     */
    private fun ifNeedWait(task: Task): Boolean {
        return task.needWait() && !task.runOnMainThread()
    }

    @UiThread
    fun start() {
        mStartTime = System.currentTimeMillis()
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw RuntimeException("必须在主线程中执行")
        }
        if (mAllTasks.size > 0) {
            mAnalyseCount.getAndIncrement()
            //打印所有依赖的信息
            printDependedMsg()
            //所有依赖需要进行图排序
            mAllTasks = getSortResult(mAllTasks, mClsAllTasks) as ArrayList<Task>
            mCountDownLatch = CountDownLatch(mNeedWaitCount.get())
            sendAndExecuteAsyncTasks()
            executeTaskMain()
        }
    }

    /**
     * 执行主线程
     */
    private fun executeTaskMain() {
        mStartTime = System.currentTimeMillis()
        for (task in mMainThreadTasks) {
            val time = System.currentTimeMillis()
            DispatchRunnable(task).run()
        }
    }

    //执行
    private fun sendAndExecuteAsyncTasks() {
        val isMainProcess = if (context?.get() != null) isMainProcess(context?.get()) else false
        for (task in mAllTasks) {
            if (task.onlyInMainProcess() && !isMainProcess) {
                markTaskDone(task)
            } else {
                sendTaskReal(task)
            }
            task.isSend = true
        }
    }

    private fun sendTaskReal(task: Task) {
        if (task.runOnMainThread()) {
            mMainThreadTasks.add(task)
            //是否需要回掉
            if (task.needCall()) {
                task.setTaskCallBack(object : TaskCallBack {
                    override fun call() {
                        markTaskDone()
                        task.isFinished = true
                        satisfyChildren(task)
                        markTaskDone(task)
                        LogUtils.i(task.javaClass.simpleName + " finish")
                    }
                })
            }
        } else {
            // 直接发，是否执行取决于具体线程池
            val future = task.runOn()!!.submit(DispatchRunnable(task, this))
            mFutures.add(future)
        }
    }

    fun executeTask(task: Task) {
        if (ifNeedWait(task)) {
            mNeedWaitCount.getAndIncrement()
        }
        task.runOn()!!.execute(DispatchRunnable(task, this))
    }

    /**
     * 搜集task相关所有依赖
     *
     * @param task 当前task
     */
    private fun collectDepends(task: Task) {
        if (task.dependsOn() != null && task.dependsOn()!!.isNotEmpty()) {
            for (cls in task.dependsOn()!!) {
                if (mDependedHashMap[cls] == null) {
                    mDependedHashMap[cls] = ArrayList()
                }
                mDependedHashMap[cls]!!.add(task)
                if (mFinishedTasks.contains(cls)) {
                    task.satisfy()
                }
            }
        }
    }

    /**
     * 通知Children一个前置任务已完成
     */
    fun satisfyChildren(task: Task) {
        val tasks = mDependedHashMap[task.javaClass]
        if (tasks != null && tasks.size > 0) {
            for (finishTask in tasks) {
                finishTask.satisfy()
            }
        }
    }

    @UiThread
    fun await() {
        try {
            if (LogUtils.isDebug) {
                LogUtils.i("still has " + mNeedWaitCount.get())
                for (task in mNeedWaitTasks) {
                    LogUtils.i("needWait: " + task.javaClass.simpleName)
                }
            }
            if (mNeedWaitCount.get() > 0) {
                mCountDownLatch!!.await(WAITTIME, TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {
        }
    }

    /**
     * 执行完成的任务
     */
    fun markTaskDone(task: Task) {
        if (ifNeedWait(task)) {
            mFinishedTasks.add(task.javaClass)
            mNeedWaitTasks.remove(task)
            mCountDownLatch!!.countDown()
            mNeedWaitCount.getAndDecrement()
        }
    }

    /**
     * 查看被依赖的信息
     */
    private fun printDependedMsg() {
        LogUtils.i("needWait size : " + mNeedWaitCount.get())
        for (cls in mDependedHashMap.keys) {
            LogUtils.i("cls " + cls!!.simpleName + "   " + mDependedHashMap[cls]!!.size)
            for (task in mDependedHashMap[cls]!!) {
                LogUtils.i("cls       " + task.javaClass.simpleName)
            }
        }
    }

    companion object {
        private const val WAITTIME: Long = 10000
        var context: WeakReference<Context>? = null
            private set

        @Volatile
        private var sHasInit = false

        @JvmStatic
        fun init(context: Context?) {
            if (context != null) {
                this.context = WeakReference(context.applicationContext)
                sHasInit = true
            }
        }

        @JvmStatic
        fun createInstance(): TaskDispatcher {
            if (!sHasInit) {
                throw RuntimeException("must call TaskDispatcher.init first")
            }
            return TaskDispatcher()
        }

    }
}
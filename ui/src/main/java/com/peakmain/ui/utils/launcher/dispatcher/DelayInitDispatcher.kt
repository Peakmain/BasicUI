package com.peakmain.ui.utils.launcher.dispatcher

import android.os.Looper
import android.os.MessageQueue
import com.peakmain.ui.utils.launcher.task.DispatchRunnable
import com.peakmain.ui.utils.launcher.task.Task
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：延时Task的一个处理器
 */
class DelayInitDispatcher {
    private val mTasks: Queue<Task> = LinkedList()
    private val mIdleHandler = MessageQueue.IdleHandler {
        if (mTasks.size > 0) {
            val task = mTasks.poll()
            DispatchRunnable(task).run()
        }
        //false:不需要再监听
        !mTasks.isEmpty()
    }

    fun addTask(task: Task): DelayInitDispatcher {
        mTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(mIdleHandler)
    }
}
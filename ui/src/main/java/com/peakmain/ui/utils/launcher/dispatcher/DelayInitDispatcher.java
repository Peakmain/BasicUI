package com.peakmain.ui.utils.launcher.dispatcher;

import android.os.Looper;
import android.os.MessageQueue;

import com.peakmain.ui.utils.launcher.task.DispatchRunnable;
import com.peakmain.ui.utils.launcher.task.Task;

import java.util.LinkedList;
import java.util.Queue;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：延时Task的一个处理器
 */
public class DelayInitDispatcher {
    private Queue<Task> mTasks = new LinkedList<>();
    private MessageQueue.IdleHandler mIdleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            if (mTasks.size() > 0) {
                Task task = mTasks.poll();
                new DispatchRunnable(task).run();
            }
            //false:不需要再监听
            return !mTasks.isEmpty();
        }
    };

    public DelayInitDispatcher addTask(Task task) {
        mTasks.add(task);
        return this;
    }

    public void start() {
        Looper.myQueue().addIdleHandler(mIdleHandler);
    }
}

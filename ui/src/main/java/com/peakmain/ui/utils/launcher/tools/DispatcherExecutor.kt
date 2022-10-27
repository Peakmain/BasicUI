package com.peakmain.ui.utils.launcher.tools

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：多线程
 */
object DispatcherExecutor {
    /**
     * 获取CPU线程池
     *
     * @return
     */
    var cPUExecutor: ThreadPoolExecutor? = null

    /**
     * 获取IO线程池
     *
     * @return
     */
    var iOExecutor: ExecutorService? = null
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    val CORE_POOL_SIZE = 2.coerceAtLeast((CPU_COUNT - 1).coerceAtMost(5))
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE
    private const val KEEP_ALIVE_SECONDS = 5
    private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val sThreadFactory = DefaultThreadFactory()
    private val sHandler = RejectedExecutionHandler { r, executor -> Executors.newCachedThreadPool().execute(r) }

    private class DefaultThreadFactory : ThreadFactory {
        private val group: ThreadGroup
        private val threadNumber = AtomicInteger(1)
        private val namePrefix: String
        override fun newThread(r: Runnable): Thread {
            val t = Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0)
            if (t.isDaemon) t.isDaemon = false
            if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
            return t
        }

        companion object {
            private val poolNumber = AtomicInteger(1)
        }

        init {
            val s = System.getSecurityManager()
            group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
            namePrefix = "TaskDispatcherPool-" +
                    poolNumber.getAndIncrement() +
                    "-Thread-"
        }
    }

    init {
        cPUExecutor = ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory, sHandler)
        cPUExecutor?.allowCoreThreadTimeOut(true)
        iOExecutor = Executors.newCachedThreadPool(sThreadFactory)
    }
}
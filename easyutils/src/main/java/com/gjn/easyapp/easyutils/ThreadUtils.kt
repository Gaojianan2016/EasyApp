package com.gjn.easyapp.easyutils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object ThreadUtils {
    const val TAG = "ThreadUtils"

    private val sMainHandler = Handler(Looper.getMainLooper())

    private val CPU_COUNT: Int = Runtime.getRuntime().availableProcessors()

    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private val CORE_POOL_SIZE = 2.coerceAtLeast((CPU_COUNT - 1).coerceAtMost(4))
    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
    private val POOL_WORK_QUEUE: BlockingQueue<Runnable> = LinkedBlockingQueue(128)
    private val THREAD_FACTORY: ThreadFactory = object : ThreadFactory {
        private val mCount = AtomicInteger(1)

        override fun newThread(r: Runnable): Thread {
            return Thread(r, "ThreadUtils #${mCount.getAndIncrement()}")
        }
    }
    private var THREAD_POOL_EXECUTOR: ThreadPoolExecutor

    init {
        val threadPoolExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 30,
            TimeUnit.SECONDS, POOL_WORK_QUEUE, THREAD_FACTORY
        )
        threadPoolExecutor.allowCoreThreadTimeOut(true)
        THREAD_POOL_EXECUTOR = threadPoolExecutor
    }

    fun runOnUiThread(runnable: Runnable, delayed: Long = 0) {
        sMainHandler.postDelayed(runnable, delayed)
    }

    fun runOnSubThread(runnable: Runnable) {
        if (THREAD_POOL_EXECUTOR.queue.size == 128 || THREAD_POOL_EXECUTOR.isShutdown) {
            "线程池爆满警告，请查看是否开启了过多的耗时线程".logE(TAG)
            return
        }
        THREAD_POOL_EXECUTOR.execute(runnable)
    }
}
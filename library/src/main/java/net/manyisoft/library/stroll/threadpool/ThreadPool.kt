package net.manyisoft.library.stroll.threadpool

import net.manyisoft.library.stroll.core.Request
import net.manyisoft.library.stroll.util.StrollLog
import net.manyisoft.library.stroll.util.requestQueue
import java.util.concurrent.*

/**
 * 网络访问线程池
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */
class ThreadPool {

    private val workQueue: LinkedBlockingDeque<Runnable> by lazy {
        LinkedBlockingDeque<Runnable>()
    }

    private val threadPool = ThreadPoolExecutor(6,
            10,
            10,
            TimeUnit.SECONDS,
            LinkedBlockingDeque<Runnable>(4),
            RejectedExecutionHandler { r, _ -> workQueue.put(r) })

    init {

        threadPool.execute(runnable())
    }

    fun execute(runnable: Request){
        workQueue.addLast(runnable)
    }

    fun removeAll(){
        workQueue.clear()
        requestQueue.clear()
    }


    private fun runnable() = Runnable {
        while (true){
            val a = workQueue.takeFirst()
            StrollLog.msg("workQueue-size:   ${workQueue.size}")
            StrollLog.msg("threadpool-size:   ${threadPool.poolSize}")
            threadPool.execute(a)
        }
    }

}
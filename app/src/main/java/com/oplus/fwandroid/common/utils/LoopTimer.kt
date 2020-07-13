package com.oplus.fwandroid.common.utils

import android.os.Handler

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：循环定时器
 * version: 1.0
 */
class LoopTimer {
    private var intervalMillis //间隔时间
            = 0
    private var running //运行状态
            = false
    private var handler //消息处理器
            : Handler? = null
    private var executeRunnable //执行Runnable
            : ExecuteRunnable? = null

    /**
     * 创建一个循环定时器
     * @param handler
     * @param runnable
     * @param intervalMillis
     */
    constructor(
        handler: Handler?,
        runnable: Runnable?,
        intervalMillis: Int
    ) {
        this.handler = handler
        this.intervalMillis = intervalMillis
        executeRunnable = ExecuteRunnable(runnable)
    }

    /**
     * 创建一个循环定时器
     * @param runnable
     * @param intervalMillis
     */
    constructor(runnable: Runnable?, intervalMillis: Int) {
        LoopTimer(Handler(), runnable, intervalMillis)
    }

    /**
     * 立即启动
     */
    fun start() {
        running = true
        handler!!.removeCallbacks(executeRunnable)
        handler!!.post(executeRunnable)
    }

    /**
     * 延迟指定间隔毫秒启动
     */
    fun delayStart() {
        running = true
        handler!!.removeCallbacks(executeRunnable)
        handler!!.postDelayed(executeRunnable, intervalMillis.toLong())
    }

    /**
     * 立即停止
     */
    fun stop() {
        running = false
        handler!!.removeCallbacks(executeRunnable)
    }

    /**
     * 获取间隔毫秒
     * @return
     */
    fun getIntervalMillis(): Int {
        return intervalMillis
    }

    /**
     * 设置间隔毫秒
     * @param intervalMillis
     */
    fun setIntervalMillis(intervalMillis: Int) {
        this.intervalMillis = intervalMillis
    }

    /**
     * 是否正在运行
     * @return
     */
    fun isRunning(): Boolean {
        return running
    }

    /**
     * 设置消息处理器
     * @param handler
     */
    fun setHandler(handler: Handler?) {
        this.handler = handler
    }

    /**
     * 设置执行内容
     * @param runnable
     */
    fun setRunnable(runnable: Runnable?) {
        executeRunnable!!.setRunnable(runnable)
    }

    /**
     * 执行Runnable
     */
    inner class ExecuteRunnable(private var runnable: Runnable?) : Runnable {
        override fun run() {
            if (running && runnable != null) {
                runnable!!.run()
            }
        }

        fun setRunnable(runnable: Runnable?) {
            this.runnable = runnable
        }

    }
}
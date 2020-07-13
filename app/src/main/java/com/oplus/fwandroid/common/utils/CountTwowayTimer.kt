package com.oplus.fwandroid.common.utils

import android.os.Handler
import android.os.Message
import android.os.SystemClock

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：双向的计时器，先倒计时然后正计时，也可以只正计时
 * version: 1.0
 */
abstract class CountTwowayTimer {
    /**
     * Millis since epoch when alarm should stop.
     */
    private var mMillisInFuture: Long = 0

    /**
     * The interval in millis that the user receives callbacks
     */
    private var mCountTwowayInterval: Long = 0

    private var mStopTimeInFuture: Long = 0

    /**
     * boolean representing if the timer was cancelled
     */
    private var mCancelled = false

    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countTwoway is done}
     * is called.
     * 距离目标时间的总间隔时长
     * 为正，距离目标时间的间隔，开始倒计时，倒计时到0后转为正计时
     * 为负，超出目标时间的间隔，已经过了目标时间，开始正计时
     * @param countTwowayInterval The interval along the way to receive
     * [.onTick] callbacks.
     * 参数单位毫秒
     */
    fun CountTwowayTimer(
        millisInFuture: Long,
        countTwowayInterval: Long
    ) {
        mMillisInFuture = millisInFuture
        mCountTwowayInterval = countTwowayInterval
    }

    /**
     * Cancel the countTwoway.
     */
    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    /**
     * Start the countTwoway.
     */
    @Synchronized
    fun start(): CountTwowayTimer {
        mCancelled = false
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    abstract fun onTick(millisUntilFinished: Long)

    private val MSG = 1


    // handles counting down
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(this@CountTwowayTimer) {
                if (mCancelled) {
                    return
                }
                val millisLeft =
                    mStopTimeInFuture - SystemClock.elapsedRealtime()
                val lastTickStart = SystemClock.elapsedRealtime()
                onTick(millisLeft)

                // take into account user's onTick taking time to execute
                var delay =
                    lastTickStart + mCountTwowayInterval - SystemClock.elapsedRealtime()

                // special case: user's onTick took more than interval to
                // complete, skip to next interval
                while (delay < 0) delay += mCountTwowayInterval
                sendMessageDelayed(obtainMessage(MSG), delay)
            }
        }
    }
}
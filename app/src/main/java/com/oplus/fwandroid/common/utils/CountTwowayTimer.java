package com.oplus.fwandroid.common.utils;

/**
 * Created by Sinaan on 2018/10/12.
 */

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * 双向的计时器，先倒计时然后正计时，也可以只正计时
 */
public abstract class CountTwowayTimer {

    /**
     * Millis since epoch when alarm should stop.
     */
    private final long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountTwowayInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countTwoway is done}
     *                          is called.
     *                          距离目标时间的总间隔时长
     *                          为正，距离目标时间的间隔，开始倒计时，倒计时到0后转为正计时
     *                          为负，超出目标时间的间隔，已经过了目标时间，开始正计时
     * @param countTwowayInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     * 参数单位毫秒
     */
    public CountTwowayTimer(long millisInFuture, long countTwowayInterval) {
        mMillisInFuture = millisInFuture;
        mCountTwowayInterval = countTwowayInterval;
    }

    /**
     * Cancel the countTwoway.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countTwoway.
     */
    public synchronized final CountTwowayTimer start() {
        mCancelled = false;
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountTwowayTimer.this) {
                if (mCancelled) {
                    return;
                }

                long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                long lastTickStart = SystemClock.elapsedRealtime();
                onTick(millisLeft);

                // take into account user's onTick taking time to execute
                long delay = lastTickStart + mCountTwowayInterval - SystemClock.elapsedRealtime();

                // special case: user's onTick took more than interval to
                // complete, skip to next interval
                while (delay < 0) delay += mCountTwowayInterval;

                sendMessageDelayed(obtainMessage(MSG), delay);
            }
        }
    };
}
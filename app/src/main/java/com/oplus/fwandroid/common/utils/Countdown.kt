package com.oplus.fwandroid.common.utils

import android.os.Handler
import android.widget.TextView

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：倒计时器
 * version: 1.0
 */
class Countdown : Runnable{
    private var remainingSeconds = 0
    private var currentRemainingSeconds = 0
    private var clickable = false
    private var running = false
    private var defaultText: String? = null
    private var countdownText: String? = null
    private var showTextView: TextView? = null
    private var handler: Handler? = null
    private var countdownListener: CountdownListener? = null
    private var textViewGetListener: TextViewGetListener? = null

    /**
     * 创建一个倒计时器
     * @param showTextView 显示倒计时的文本视图
     * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     * @param remainingSeconds 倒计时秒数，例如：60，就是从60开始倒计时一直到0结束
     */
    constructor(
        showTextView: TextView?,
        countdownText: String?,
        remainingSeconds: Int
    ) {
        this.showTextView = showTextView
        this.countdownText = countdownText
        this.remainingSeconds = remainingSeconds
        handler = Handler()
    }

    /**
     * 创建一个倒计时器
     * @param textViewGetListener 显示倒计时的文本视图获取监听器
     * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     * @param remainingSeconds 倒计时秒数，例如：60，就是从60开始倒计时一直到0结束
     */
    constructor(
        textViewGetListener: TextViewGetListener?,
        countdownText: String?,
        remainingSeconds: Int
    ) {
        this.textViewGetListener = textViewGetListener
        this.countdownText = countdownText
        this.remainingSeconds = remainingSeconds
        handler = Handler()
    }

    /**
     * 创建一个倒计时器，默认60秒
     * @param showTextView 显示倒计时的文本视图
     * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     */
    constructor(showTextView: TextView?, countdownText: String?) {
        Countdown(showTextView, countdownText, 60)
    }

    /**
     * 创建一个倒计时器，默认60秒
     * @param textViewGetListener 显示倒计时的文本视图获取监听器
     * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     */
    constructor(
        textViewGetListener: TextViewGetListener?,
        countdownText: String?
    ) {
        Countdown(textViewGetListener, countdownText, 60)
    }

    private fun getShowTextView(): TextView? {
        if (showTextView != null) {
            return showTextView
        }
        return if (textViewGetListener != null) {
            textViewGetListener!!.OnGetShowTextView()
        } else null
    }

    override fun run() {
        if (currentRemainingSeconds > 0) {
            getShowTextView()!!.isEnabled = clickable
            getShowTextView()!!.text = String.format(countdownText!!, currentRemainingSeconds)
            if (countdownListener != null) {
                countdownListener!!.onUpdate(currentRemainingSeconds)
            }
            currentRemainingSeconds--
            handler!!.postDelayed(this, 1000)
        } else {
            stop()
        }
    }

    fun start() {
        defaultText = getShowTextView()!!.text as String
        currentRemainingSeconds = remainingSeconds
        handler!!.removeCallbacks(this)
        handler!!.post(this)
        if (countdownListener != null) {
            countdownListener!!.onStart()
        }
        running = true
    }

    fun stop() {
        getShowTextView()!!.isEnabled = true
        getShowTextView()!!.text = defaultText
        handler!!.removeCallbacks(this)
        if (countdownListener != null) {
            countdownListener!!.onFinish()
        }
        running = false
    }

    fun isRunning(): Boolean {
        return running
    }

    fun getRemainingSeconds(): Int {
        return remainingSeconds
    }

    fun getCountdownText(): String? {
        return countdownText
    }

    fun setCountdownText(countdownText: String?) {
        this.countdownText = countdownText
    }

    fun setCurrentRemainingSeconds(currentRemainingSeconds: Int) {
        this.currentRemainingSeconds = currentRemainingSeconds
    }

    fun setClickableWhileCountdown(clickable: Boolean) {
        this.clickable = clickable
    }

    fun setCountdownListener(countdownListener: CountdownListener?) {
        this.countdownListener = countdownListener
    }

    fun getCountdownListener(): CountdownListener? {
        return countdownListener
    }

    /**
     * 倒计时监听器
     */
    interface CountdownListener {
        /**
         * 当倒计时开始
         */
        fun onStart()

        /**
         * 当倒计时结束
         */
        fun onFinish()

        /**
         * 更新
         * @param currentRemainingSeconds 剩余时间
         */
        fun onUpdate(currentRemainingSeconds: Int)
    }

    interface TextViewGetListener {
        fun OnGetShowTextView(): TextView?
    }
}
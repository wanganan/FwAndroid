package com.oplus.fwandroid.common.utils

import android.view.View

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：是否快速双击
 * version: 1.0
 */
object ForbadClick {
    private var lastClickTime: Long = 0
    private var view: View? = null
    private var mCount = 1
    private const val FAST_DURATION = 3000

    fun isFastDoubleClick(v: View): Boolean {
        val time = System.currentTimeMillis()
        if (view === v && time - lastClickTime < FAST_DURATION) {
            return true
        }
        view = v
        lastClickTime = time
        return false
    }

    fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < FAST_DURATION) {
            return true
        }
        lastClickTime = time
        return false
    }

    /**
     * 是否是连续count次点击
     *
     * @param count 连续点击次数
     * @return
     */
    fun isStraightClick(count: Int): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < FAST_DURATION) {
            mCount++
            if (mCount % count == 0) return true
        } else {
            mCount = 1
        }
        lastClickTime = time
        return false
    }
}
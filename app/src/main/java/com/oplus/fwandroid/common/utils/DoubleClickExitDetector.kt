package com.oplus.fwandroid.common.utils

import android.content.Context
import android.widget.Toast
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：双击退出识别器
 * version: 1.0
 */
class DoubleClickExitDetector {
    var DEFAULT_HINT_MESSAGE_CHINA = "再按一次退出程序"
    var DEFAULT_HINT_MESSAGE_OTHER = "再按一次退出程序"
    private var effectiveIntervalTime // 有效的间隔时间，单位毫秒
            = 0
    private var lastClickTime // 上次点击时间
            : Long = 0
    private var hintMessage // 提示消息
            : String? = null
    private var context: Context? = null

    /**
     * 创建一个双击退出识别器
     * @param context Androdi上下文
     * @param hintMessage 提示消息
     * @param effectiveIntervalTime 有效间隔时间
     */
    constructor(
        context: Context?,
        hintMessage: String?,
        effectiveIntervalTime: Int
    ) {
        this.context = context
        this.hintMessage = hintMessage
        this.effectiveIntervalTime = effectiveIntervalTime
    }

    /**
     * 创建一个双击退出识别器，有效间隔时间默认为2000毫秒
     * @param context Androdi上下文
     * @param hintContent 提示消息
     */
    constructor(
        context: Context?,
        hintContent: String?
    ) {
        DoubleClickExitDetector(context, hintContent, 2000)
    }

    /**
     * 创建一个双击退出识别器，中国环境下默认提示消息为“再按一次退出程序”，其它环境下默认提示消息为“Press again to exit the program”；有效间隔时间默认为2000毫秒
     * @param context Androdi上下文
     */
    fun DoubleClickExitDetector(context: Context?) {
        DoubleClickExitDetector(
            context,
            if (Locale.CHINA == Locale.getDefault()) DEFAULT_HINT_MESSAGE_CHINA else DEFAULT_HINT_MESSAGE_OTHER,
            2000
        )
    }

    /**
     * 点击，你需要重写Activity的onBackPressed()方法，先调用此方法，如果返回true就执行父类的onBackPressed()方法来关闭Activity否则不执行
     * @return 当两次点击时间间隔小于有效间隔时间时就会返回true，否则返回false
     */
    fun click(): Boolean {
        val currentTime = System.currentTimeMillis()
        val result = currentTime - lastClickTime < effectiveIntervalTime
        lastClickTime = currentTime
        if (!result) {
            Toast.makeText(context, hintMessage, Toast.LENGTH_SHORT).show()
        }
        return result
    }

    /**
     * 设置有效间隔时间，单位毫秒
     * @param effectiveIntervalTime 效间隔时间，单位毫秒。当两次点击时间间隔小于有效间隔时间click()方法就会返回true
     */
    fun setEffectiveIntervalTime(effectiveIntervalTime: Int) {
        this.effectiveIntervalTime = effectiveIntervalTime
    }

    /**
     * 设置提示消息
     * @param hintMessage 当前点击同上次点击时间间隔大于有效间隔时间click()方法就会返回false，并且显示提示消息
     */
    fun setHintMessage(hintMessage: String?) {
        this.hintMessage = hintMessage
    }
}
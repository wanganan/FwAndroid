package com.oplus.fwandroid.common.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：获取屏幕相关
 * version: 1.0
 */
object DensityUtil {
    private val deviceWidthHeight = IntArray(2)

    fun getDeviceInfo(context: Context): IntArray? {
        if (deviceWidthHeight[0] == 0 && deviceWidthHeight[1] == 0) {
            val metrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay
                .getMetrics(metrics)
            deviceWidthHeight[0] = metrics.widthPixels
            deviceWidthHeight[1] = metrics.heightPixels
        }
        return deviceWidthHeight
    }

    /**
     * 得到屏幕的宽度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 0
    }

    /**
     * 得到屏幕的高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 0
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context?, dpValue: Float): Int {
        if (null == context) {
            return 0
        }
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param context
     * @param spValue
     * @return
     */
    fun sp2px(context: Context?, spValue: Float): Int {
        if (null == context) {
            return 0
        }
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * @param context 上下文
     * @param pxValue px的数值
     * @return px to dp
     */
    fun px2dp(context: Context?, pxValue: Float): Int {
        if (null == context) {
            return 0
        }
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获取屏幕尺寸
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun getScreenSize(context: Context): Point? {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point(display.width, display.height)
        } else {
            val point = Point()
            display.getSize(point)
            point
        }
    }
}
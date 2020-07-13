package com.oplus.fwandroid.common.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：解决底部屏幕按键适配
 * version: 1.0
 */
class AndroidWorkaround {
    fun assistActivity(content: View) {
        AndroidWorkaround(content)
    }

    private var mChildOfContent: View? = null
    private var usableHeightPrevious = 0
    private var frameLayoutParams: ViewGroup.LayoutParams? = null

    private constructor(content: View) {
        mChildOfContent = content
        mChildOfContent!!.viewTreeObserver
            .addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
        frameLayoutParams = mChildOfContent!!.layoutParams
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams!!.height = usableHeightNow
            mChildOfContent!!.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent!!.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }

    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass =
                Class.forName("android.os.SystemProperties")
            val m =
                systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride =
                m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }
        return hasNavigationBar
    }
}
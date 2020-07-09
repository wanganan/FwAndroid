package com.oplus.fwandroid.common.base

import android.app.Activity

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：根view,添加无加载纯界面的显示方法
 * version: 1.0
 */
interface BaseView {
    /**
     * 宿主，activity返回this,fragment返回activity
     */
    fun host(): Activity

    /**
     * Toast.Toast.LENGTH_SHORT
     */
    fun showShortToast(toast: String?)

    /**
     * Toast.Toast.LENGTH_LONG
     */
    fun showLongToast(toast: String?)

    /**
     * 从界面底部弹出布局
     */
    fun showBottomDialog()

    /**
     * 在界面中间显示一个编辑框
     */
    fun showCenterEditDialog()

    /**
     * 在屏幕中间显示一个提示框
     */
    fun showCenterPromptDialog()

    /**
     * 弹出框挂载在某个view上，点击view触发
     */
    fun showPopupDialog()

    /**
     * 给界面设置背景色
     */
    fun background(): Int
}
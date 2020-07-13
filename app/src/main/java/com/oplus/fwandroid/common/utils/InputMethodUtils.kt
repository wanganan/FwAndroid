package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
object InputMethodUtils {
    /**
     * 为给定的编辑器开启软键盘
     *
     * @param editText 给定的编辑器
     */
    fun openSoftKeyboard(context: Context, editText: EditText) {
        editText.requestFocus()
        val inputMethodManager =
            context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethodManager.showSoftInput(
            editText,
            InputMethodManager.SHOW_IMPLICIT
        )
        ViewUtils.setEditTextSelectionToEnd(editText)
    }


    /**
     * 关闭软键盘
     */
    fun closeSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        //如果软键盘已经开启
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


    /**
     * 切换软键盘的状态
     */
    fun toggleSoftKeyboardState(context: Context) {
        (context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_IMPLICIT,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    /**
     * 判断隐藏软键盘是否弹出,弹出就隐藏
     * @param mActivity
     * @return
     */
    fun keyBoxIsShow(mActivity: Activity): Boolean {
        return if (mActivity.window
                .attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
        ) {
            //隐藏软键盘
            mActivity.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            true
        } else {
            false
        }
    }
}
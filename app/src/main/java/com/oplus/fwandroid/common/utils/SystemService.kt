package com.oplus.fwandroid.common.utils

import android.content.ClipboardManager
import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
object SystemService {
    fun getLayoutInflater(context: Context): LayoutInflater? {
        return context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun getInputMethodManager(context: Context): InputMethodManager? {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun getClipboardManager(context: Context): ClipboardManager? {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    //<uses-permission Android:name="android.permission.VIBRATE"/>
    fun getVibrator(context: Context): Vibrator? {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}
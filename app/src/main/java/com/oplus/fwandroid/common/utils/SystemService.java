package com.oplus.fwandroid.common.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sinaan on 2016/10/26.
 */
public class SystemService {
    public static LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static ClipboardManager getClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    //<uses-permission Android:name="android.permission.VIBRATE"/>
    public static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}

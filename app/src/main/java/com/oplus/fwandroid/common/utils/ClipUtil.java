package com.oplus.fwandroid.common.utils;

import android.content.Context;

/**
 * Created by Sinaan on 2016/12/22.
 */
public class ClipUtil {
    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(Context context,String content) {
        SystemService.getClipboardManager(context).setText(content.trim());
    }
}

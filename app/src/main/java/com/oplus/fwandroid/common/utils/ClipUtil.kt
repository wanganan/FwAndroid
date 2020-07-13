package com.oplus.fwandroid.common.utils

import android.content.Context

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
object ClipUtil {
    /**
     * 实现文本复制功能
     *
     * @param content
     */
    fun copy(context: Context?, content: String) {
        SystemService.getClipboardManager(context!!)?.text = content.trim { it <= ' ' }
    }
}
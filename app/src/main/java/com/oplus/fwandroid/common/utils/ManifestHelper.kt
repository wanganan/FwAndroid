package com.oplus.fwandroid.common.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/**
 * @author Sinaan
 * @date 2020/7/10
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：获取清单文件中的数据
 * version: 1.0
 */
object ManifestHelper {
    const val FONT_SCALE = "font_scale"//字体缩放倍数

    /**
     * 获取使用者在 AndroidManifest 中填写的 Meta 信息
     *
     * Example usage:
     * <pre>
     * <meta-data android:name="font_scale" android:value="1.5f"></meta-data>
     * </pre>
     *
     * @param context [Context]
     */
    fun getMetaData(context: Context, metaKey: String): String {
        val packageManager = context.packageManager
        val applicationInfo: ApplicationInfo?
        try {
            applicationInfo = packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            if (applicationInfo?.metaData != null) {
                if (applicationInfo.metaData.containsKey(metaKey)) {
                    return applicationInfo.metaData[metaKey].toString()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}
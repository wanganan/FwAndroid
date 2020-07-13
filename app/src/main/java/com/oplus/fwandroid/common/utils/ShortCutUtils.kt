package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.Intent.ShortcutIconResource
import android.net.Uri
import com.oplus.fwandroid.R

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：创建删除快捷图标
 * version: 1.0
 * 需要权限: com.android.launcher.permission.INSTALL_SHORTCUT com.android.launcher.permission.UNINSTALL_SHORTCUT
 */
object ShortCutUtils {
    /**
     * Don't let anyone instantiate this class.
     */
    private fun ShortCutUtils() {
        throw Error("Do not need instantiate!")
    }

    /**
     * 检测是否存在快捷键
     *
     * @param activity Activity
     * @return 是否存在桌面图标
     */
    fun hasShortcut(activity: Activity): Boolean {
        var isInstallShortcut = false
        val cr = activity.contentResolver
        val AUTHORITY = "com.android.launcher.settings"
        val CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY
                    + "/favorites?notify=true"
        )
        val c = cr.query(
            CONTENT_URI,
            arrayOf("title", "iconResource"),
            "title=?",
            arrayOf(activity.getString(
                R.string.app_name
            ).trim { it <= ' ' }
            ),
            null
        )
        if (c != null && c.count > 0) {
            isInstallShortcut = true
        }
        return isInstallShortcut
    }

    /**
     * 为程序创建桌面快捷方式
     *
     * @param activity Activity
     * @param res     res
     */
    fun addShortcut(activity: Activity, res: Int) {
        val shortcut = Intent(
            "com.android.launcher.action.INSTALL_SHORTCUT"
        )
        // 快捷方式的名称
        shortcut.putExtra(
            Intent.EXTRA_SHORTCUT_NAME,
            activity.getString(R.string.app_name)
        )
        shortcut.putExtra("duplicate", false) // 不允许重复创建
        val shortcutIntent = Intent(Intent.ACTION_MAIN)
        shortcutIntent.setClassName(activity, activity.javaClass.name)
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        // 快捷方式的图标
        val iconRes = ShortcutIconResource.fromContext(
            activity, res
        )
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes)
        activity.sendBroadcast(shortcut)
    }

    /**
     * 删除程序的快捷方式
     *
     * @param activity Activity
     */
    fun delShortcut(activity: Activity) {
        val shortcut = Intent(
            "com.android.launcher.action.UNINSTALL_SHORTCUT"
        )
        // 快捷方式的名称
        shortcut.putExtra(
            Intent.EXTRA_SHORTCUT_NAME,
            activity.getString(R.string.app_name)
        )
        val appClass = (activity.packageName + "."
                + activity.localClassName)
        val comp = ComponentName(
            activity.packageName,
            appClass
        )
        shortcut.putExtra(
            Intent.EXTRA_SHORTCUT_INTENT, Intent(
                Intent.ACTION_MAIN
            ).setComponent(comp)
        )
        activity.sendBroadcast(shortcut)
    }
}
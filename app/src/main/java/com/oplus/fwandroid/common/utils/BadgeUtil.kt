package com.oplus.fwandroid.common.utils

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 * 应用启动图标未读消息数显示 工具类  (效果如：QQ、微信、未读短信 等应用图标)<br/>
 * 依赖于第三方手机厂商(如：小米、三星)的Launcher定制、原生系统不支持该特性<br/>
 * 该工具类 支持的设备有 小米、三星、索尼【其中小米、三星亲测有效、索尼未验证】
 */
object BadgeUtil {
    /**
     * 设置Badge 目前支持Launcher:
     * MIUI
     * Sony
     * Samsung
     * LG
     * HTC
     * Nova 需要这些权限
     * @param context context
     * @param count   count
     * @param icon    icon应用的图标
     */
    /*  <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
    <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT"/>
    <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.android.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.android.launcher2.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.android.launcher2.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.android.launcher3.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.android.launcher3.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="org.adw.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="org.adw.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.htc.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.htc.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.qihoo360.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.qihoo360.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.lge.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.lge.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="net.qihoo.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="net.qihoo.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="org.adwfreak.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="org.adwfreak.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="org.adw.launcher_donut.permission.READ_SETTINGS"/>
    <uses-permission android:name="org.adw.launcher_donut.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.huawei.launcher3.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.huawei.launcher3.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.fede.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.fede.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.sec.android.app.twlauncher.settings.READ_SETTINGS"/>
    <uses-permission android:name="com.sec.android.app.twlauncher.settings.WRITE_SETTINGS"/>
    <uses-permission android:name="com.anddoes.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.anddoes.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.tencent.qqlauncher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.tencent.qqlauncher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.huawei.launcher2.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.huawei.launcher2.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.android.mylauncher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.android.mylauncher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.ebproductions.android.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.ebproductions.android.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.oppo.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.oppo.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.miui.mihome2.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.miui.mihome2.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="com.huawei.android.launcher.permission.READ_SETTINGS"/>
    <uses-permission android:name="com.huawei.android.launcher.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="telecom.mdesk.permission.READ_SETTINGS"/>
    <uses-permission android:name="telecom.mdesk.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="dianxin.permission.ACCESS_LAUNCHER_DATA"/>
    <uses-permission android:name="android.hardware.sensor.accelerometer"/>*/
    fun setBadgeCount(context: Context, count: Int, icon: Int) {

        // TODO 生成器模式重构
        var count = count
        count = if (count <= 0) {
            0
        } else {
            Math.max(0, Math.min(count, 99))
        }
        if (Build.MANUFACTURER.equals("xiaomi", ignoreCase = true)) {
            setBadgeOfMIUI(context, count, icon)
        } else if (Build.MANUFACTURER.equals("sony", ignoreCase = true)) {
            setBadgeOfSony(context, count)
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung") ||
            Build.MANUFACTURER.toLowerCase().contains("lg")
        ) {
            setBadgeOfSumsung(context, count)
        } else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            setBadgeOfHTC(context, count)
        } else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
            setBadgeOfNova(context, count)
        } else {
            Toast.makeText(context, "Not Found Support Launcher", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 设置MIUI的Badge
     *
     * @param context context
     * @param count   count
     * @param icon    icon
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setBadgeOfMIUI(
        context: Context,
        count: Int,
        icon: Int
    ) {
        Log.d("xys", "Launcher : MIUI")
        val mNotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(context)
            .setContentTitle("title").setContentText("text").setSmallIcon(icon)
        val notification = builder.build()
        try {
            val field =
                notification.javaClass.getDeclaredField("extraNotification")
            val extraNotification = field[notification]
            val method = extraNotification.javaClass.getDeclaredMethod(
                "setMessageCount",
                Int::class.javaPrimitiveType
            )
            method.invoke(extraNotification, count)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mNotificationManager.notify(0, notification)
    }

    /**
     * 设置索尼的Badge
     *
     *
     * 需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE"></uses-permission>
     *
     * @param context context
     * @param count   count
     */
    private fun setBadgeOfSony(context: Context, count: Int) {
        val launcherClassName = getLauncherClassName(context) ?: return
        var isShow = true
        if (count == 0) {
            isShow = false
        }
        val localIntent = Intent()
        localIntent.action = "com.sonyericsson.home.action.UPDATE_BADGE"
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow) //是否显示
        localIntent.putExtra(
            "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
            launcherClassName
        ) //启动页
        localIntent.putExtra(
            "com.sonyericsson.home.intent.extra.badge.MESSAGE",
            count.toString()
        ) //数字
        localIntent.putExtra(
            "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",
            context.packageName
        ) //包名
        context.sendBroadcast(localIntent)
    }

    /**
     * 设置三星的Badge\设置LG的Badge
     *
     * @param context context
     * @param count   count
     */
    private fun setBadgeOfSumsung(context: Context, count: Int) {
        // 获取你当前的应用
        val launcherClassName = getLauncherClassName(context) ?: return
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        intent.putExtra("badge_count", count)
        intent.putExtra("badge_count_package_name", context.packageName)
        intent.putExtra("badge_count_class_name", launcherClassName)
        context.sendBroadcast(intent)
    }

    /**
     * 设置HTC的Badge
     *
     * @param context context
     * @param count   count
     */
    private fun setBadgeOfHTC(context: Context, count: Int) {
        val intentNotification = Intent("com.htc.launcher.action.SET_NOTIFICATION")
        val localComponentName = ComponentName(
            context.packageName,
            getLauncherClassName(context)
        )
        intentNotification.putExtra(
            "com.htc.launcher.extra.COMPONENT",
            localComponentName.flattenToShortString()
        )
        intentNotification.putExtra("com.htc.launcher.extra.COUNT", count)
        context.sendBroadcast(intentNotification)
        val intentShortcut = Intent("com.htc.launcher.action.UPDATE_SHORTCUT")
        intentShortcut.putExtra("packagename", context.packageName)
        intentShortcut.putExtra("count", count)
        context.sendBroadcast(intentShortcut)
    }

    /**
     * 设置Nova的Badge
     *
     * @param context context
     * @param count   count
     */
    private fun setBadgeOfNova(context: Context, count: Int) {
        val contentValues = ContentValues()
        contentValues.put(
            "tag", context.packageName + "/" +
                    getLauncherClassName(context)
        )
        contentValues.put("count", count)
        context.contentResolver.insert(
            Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
            contentValues
        )
    }

    fun setBadgeOfMadMode(
        context: Context,
        count: Int,
        packageName: String?,
        className: String?
    ) {
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        intent.putExtra("badge_count", count)
        intent.putExtra("badge_count_package_name", packageName)
        intent.putExtra("badge_count_class_name", className)
        context.sendBroadcast(intent)
    }

    /**
     * 重置Badge
     *
     * @param context context
     * @param icon    icon
     */
    fun resetBadgeCount(context: Context, icon: Int) {
        setBadgeCount(context, 0, icon)
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     * "android:name" attribute.
     */
    private fun getLauncherClassName(context: Context): String {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.packageName)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        var info = packageManager
            .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0)
        }
        return info!!.activityInfo.name
    }
}
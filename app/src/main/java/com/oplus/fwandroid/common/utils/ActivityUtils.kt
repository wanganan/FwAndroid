package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcelable
import android.os.Process
import android.provider.Settings
import com.oplus.fwandroid.R
import java.io.File
import java.io.Serializable
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Activity工具类
 * version: 1.0
 */
object ActivityUtils {
    private val activityList: MutableList<Activity?> =
        LinkedList()

    interface Anim {
        companion object {
            /*没有动画*/
            const val None = 0

            /*淡入淡出效果*/
            const val Fade_In_Out = 1

            /*放大淡出(第二个页面从前面淡出)*/
            const val Enlarge_Fadeout_Front = 2

            /*放大淡出(第二个页面从后面淡出)*/
            const val Enlarge_Fadeout_Behind = 3

            /*转动淡出(以中心为轴转动)*/
            const val Rotate_Fadeout_Center = 4

            /*转动淡出(以某个角为轴转动)*/
            const val Rotate_Fadeout_Angle = 5

            /*左上角展开淡出*/
            const val Expand_TLCorner_Fadeout = 6

            /*缩小到左上角淡出*/
            const val Shrink_TLCorner_Fadeout = 7

            /*压缩变小淡出*/
            const val Compress_Fadeout = 8

            /*右往左推出*/
            const val Pushout_R2L = 9

            /*下往上推出*/
            const val Pushout_B2T = 10

            /*左右交错*/
            const val Stagger_LR = 11

            /*上下交错*/
            const val Stagger_TB = 12
        }
    }

    fun addActivity(activity: Activity?) {
        activityList.add(activity)
    }

    fun finishActivity(activity: Activity?) {
        if (activity != null && !activity.isFinishing) {
            activity.finish()
        }
        activityList.remove(activity)
    }

    fun getSurvivalNumber(): Int {
        return activityList.size
    }

    /**
     * 销毁列表中的所有Activity
     * 获取PID:android.os.Process.myPid()
     */
    fun exitAllActivity(): Int {
        var num = 0
        try {
            for (i in activityList.indices) {
                if (activityList[i] != null && !activityList[i]!!.isFinishing) {
                    activityList[i]!!.finish()
                    num++
                }
            }
            activityList.clear()
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return num
    }

    /**
     * 销毁当前Activity之前的列表中的所有Activity
     */
    fun exitBeforeCurrentActivity(): Int {
        var num = 0
        try {
            for (i in activityList.indices) {
                if (activityList[i] != null && !activityList[i]!!.isFinishing) {
                    activityList[i]!!.finish()
                    num++
                }
            }
            activityList.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return num
    }

    /**
     * finish()后调用，启动新Activity的动画方式
     *
     * @param activity 当前Activity
     * @param type     启动新Activity的动画标识
     */
    fun switchAnimate(activity: Activity, type: Int) {
        when (type) {
            Anim.None -> {
            }
            Anim.Fade_In_Out -> activity.overridePendingTransition(
                R.anim.fade,
                R.anim.hold
            )
            Anim.Enlarge_Fadeout_Front -> activity.overridePendingTransition(
                R.anim.my_scale_action,
                R.anim.my_alpha_action
            )
            Anim.Enlarge_Fadeout_Behind -> activity.overridePendingTransition(
                R.anim.wave_scale,
                R.anim.my_alpha_action
            )
            Anim.Rotate_Fadeout_Center -> activity.overridePendingTransition(
                R.anim.scale_rotate,
                R.anim.my_alpha_action
            )
            Anim.Rotate_Fadeout_Angle -> activity.overridePendingTransition(
                R.anim.scale_translate_rotate,
                R.anim.my_alpha_action
            )
            Anim.Expand_TLCorner_Fadeout -> activity.overridePendingTransition(
                R.anim.scale_translate,
                R.anim.my_alpha_action
            )
            Anim.Shrink_TLCorner_Fadeout -> activity.overridePendingTransition(
                R.anim.zoom_enter,
                R.anim.zoom_exit
            )
            Anim.Compress_Fadeout -> activity.overridePendingTransition(
                R.anim.hyperspace_in,
                R.anim.hyperspace_out
            )
            Anim.Pushout_R2L -> activity.overridePendingTransition(
                R.anim.push_left_in,
                R.anim.push_left_out
            )
            Anim.Pushout_B2T -> activity.overridePendingTransition(
                R.anim.push_up_in,
                R.anim.push_up_out
            )
            Anim.Stagger_LR -> activity.overridePendingTransition(
                R.anim.slide_left,
                R.anim.slide_right
            )
            Anim.Stagger_TB -> activity.overridePendingTransition(
                R.anim.slide_up_in,
                R.anim.slide_down_out
            )
            else -> {
            }
        }
    }

    /**
     * 启动一个Activity并关闭当前Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity
     */
    fun startActivityAndFinish(activity: Activity, cls: Class<*>?) {
        val intent = Intent(activity, cls)
        activity.startActivity(intent)
        activity.finish()
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动一个Activity并关闭当前Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity
     * @param type     启动新Activity的动画标识
     */
    fun startActivityAndFinish(
        activity: Activity,
        cls: Class<*>?,
        type: Int
    ) {
        val intent = Intent(activity, cls)
        activity.startActivity(intent)
        activity.finish()
        switchAnimate(activity, type)
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     */
    fun startActivity(activity: Activity, cls: Class<*>?) {
        val intent = Intent(activity, cls)
        activity.startActivity(intent)
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param type     启动新Activity的动画标识
     */
    fun startActivity(activity: Activity, cls: Class<*>?, type: Int) {
        val intent = Intent(activity, cls)
        activity.startActivity(intent)
        switchAnimate(activity, type)
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     */
    fun startActivity(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        activity.startActivity(intent)
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     */
    fun startActivityAndFinishForData(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        activity.startActivity(intent)
        activity.finish()
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param type     启动新Activity的动画标识
     */
    fun startActivityAndFinishForData(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?,
        type: Int
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        activity.startActivity(intent)
        activity.finish()
        switchAnimate(activity, type)
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     */
    fun startActivityForData(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        activity.startActivity(intent)
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动Activity并传数据
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param type     启动新Activity的动画标识
     */
    fun startActivityForData(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?,
        type: Int
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        activity.startActivity(intent)
        switchAnimate(activity, type)
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param flag     int标记
     */
    fun startActivityForResult(
        activity: Activity,
        cls: Class<*>?,
        flag: Int
    ) {
        val intent = Intent(activity, cls)
        intent.flags = flag
        activity.startActivityForResult(intent, flag)
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param flag     int标记
     * @param type     启动新Activity的动画标识
     */
    fun startActivityForResult(
        activity: Activity,
        cls: Class<*>?,
        flag: Int,
        type: Int
    ) {
        val intent = Intent(activity, cls)
        intent.flags = flag
        activity.startActivityForResult(intent, flag)
        switchAnimate(activity, type)
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param flag     int标记
     */
    fun startActivityForResult(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?,
        flag: Int
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        intent.flags = flag
        activity.startActivityForResult(intent, flag)
        switchAnimate(activity, Anim.Pushout_R2L)
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:"data"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param map      intent传值的键值对
     * @param flag     int标记
     * @param type     启动新Activity的动画标识
     */
    fun startActivityForResult(
        activity: Activity,
        cls: Class<*>?,
        map: Map<String, Any>?,
        flag: Int,
        type: Int
    ) {
        val intent = Intent(activity, cls)
        putIntent(map, intent)
        intent.flags = flag
        activity.startActivityForResult(intent, flag)
        switchAnimate(activity, type)
    }

    /**
     * 给Intent传值
     *
     * @param map
     * @param intent
     */
    private fun putIntent(
        map: Map<String, Any>?,
        intent: Intent
    ) {
        if (map == null) return
        for ((key, value1) in map) {
            val value = value1
            if (value is String) intent.putExtra(key, value)
            if (value is Boolean) intent.putExtra(key, value)
            if (value is Float) intent.putExtra(key, value)
            if (value is Long) intent.putExtra(key, value)
            if (value is Int) intent.putExtra(key, value)
            if (value is Serializable) intent.putExtra(key, value)
            if (value is Parcelable) intent.putExtra(key, value)
            if (value is ArrayList<*>) {
                val v = value
                if (v != null && v.size > 0) {
                    val o = v[0]
                    if (o is String) intent.putStringArrayListExtra(
                        key,
                        value as ArrayList<String?>
                    )
                    if (o is Parcelable) intent.putParcelableArrayListExtra(
                        key,
                        value as ArrayList<Parcelable?>
                    )
                }
            }
        }
    }

    /**
     * 获取一个Intent
     *
     * @param map
     */
    fun putIntent(map: Map<String, Any?>?): Intent? {
        val intent = Intent()
        if (map == null) return intent
        for ((key, value1) in map) {
            val value = value1!!
            if (value is String) intent.putExtra(key, value)
            if (value is Boolean) intent.putExtra(key, value)
            if (value is Float) intent.putExtra(key, value)
            if (value is Long) intent.putExtra(key, value)
            if (value is Int) intent.putExtra(key, value)
            if (value is Serializable) intent.putExtra(key, value)
            if (value is Parcelable) intent.putExtra(key, value)
            if (value is ArrayList<*>) {
                val v = value
                if (v != null && v.size > 0) {
                    val o = v[0]
                    if (o is String) intent.putStringArrayListExtra(
                        key,
                        value as ArrayList<String?>
                    )
                    if (o is Parcelable) intent.putParcelableArrayListExtra(
                        key,
                        value as ArrayList<Parcelable?>
                    )
                }
            }
        }
        return intent
    }

    /**
     * 启动网络设置
     *
     * @param activity 当前Activity
     */
    fun startSetNetActivity(activity: Activity) {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        activity.startActivity(intent)
    }

    /**
     * 启动系统设置
     *
     * @param activity 当前Activity
     */
    fun startSetActivity(activity: Activity) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        activity.startActivity(intent)
    }

    fun isIntentAvailable(context: Context, intent: Intent?): Boolean {
        val packageManager = context.packageManager
        val list =
            packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
        return list.size > 0
    }

    fun startHtmlFile(activity: Activity, path: String?) {
        val uri =
            Uri.parse(path).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(path).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, "text/html")
        activity.startActivity(intent)
    }

    fun startImageFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "image/*")
        activity.startActivity(intent)
    }

    fun startPdfFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "application/pdf")
        activity.startActivity(intent)
    }

    fun startTextFile(
        activity: Activity,
        path: String?,
        pathBoolean: Boolean
    ) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (pathBoolean) {
            val uri1 = Uri.parse(path)
            intent.setDataAndType(uri1, "text/plain")
        } else {
            val uri2 = Uri.fromFile(File(path))
            intent.setDataAndType(uri2, "text/plain")
        }
        activity.startActivity(intent)
    }

    fun startAudioFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "audio/*")
        activity.startActivity(intent)
    }

    fun startVideoFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "video/*")
        activity.startActivity(intent)
    }

    fun startChmFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "application/x-chm")
        activity.startActivity(intent)
    }

    fun startWordFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "application/msword")
        activity.startActivity(intent)
    }

    fun startExcelFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        activity.startActivity(intent)
    }

    fun startPptFile(activity: Activity, path: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(path))
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        activity.startActivity(intent)
    }
}
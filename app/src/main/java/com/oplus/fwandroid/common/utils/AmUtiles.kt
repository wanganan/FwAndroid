package com.oplus.fwandroid.common.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：定时器
 * version: 1.0
 */
object AmUtiles {
    private var am: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null


    /**
     * 简化10分钟之后启动一个intent 600代表10分钟   20代表20秒
     */
    fun sendUpdateBroadcastRepeat(
        ctx: Context,
        intent: Intent?
    ): AlarmManager? {
        pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0)
        am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am!![AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 600 * 1000] = pendingIntent
        return am
    }


    /**
     * 应用退出了就取消闹钟
     */
    fun destoryAlarmManger() {
        if (null != am) {
            am!!.cancel(pendingIntent)
        }
    }


    /**
     * 设置午夜定时器, 午夜12点发送广播, MIDNIGHT_ALARM_FILTER.
     * 实际测试可能会有一分钟左右的偏差.
     *
     * @param context 上下文
     */
    fun setMidnightAlarm(
        context: Context,
        action: String?
    ) {
        val appContext = context.applicationContext
        val intent = Intent(action)
        val pi = PendingIntent.getBroadcast(
            appContext, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val am = appContext.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        // 午夜12点的标准计时, 来源于SO, 实际测试可能会有一分钟左右的偏差.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR] = 0
        calendar[Calendar.AM_PM] = Calendar.AM
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        // 显示剩余时间
        val now = Calendar.getInstance().timeInMillis
        Log.e("剩余时间(秒): ", ((calendar.timeInMillis - now) / 1000).toString() + "")

        // 设置之前先取消前一个PendingIntent
        am.cancel(pi)
        // 设置每一天的计时器
        am.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pi
        )
    }
}
package com.oplus.fwandroid.common.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：计算显示的时间是多久之前的
 * version: 1.0
 */
class TransitionTime {
    private var endDate: Date? = null
    private var formatBuilder: SimpleDateFormat? = null
    val WEEKDAYS = 7
    var WEEK =
        arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    constructor(endTime: Long) {
        endDate = Date(endTime)
    }


    var timeMills: Long = 0


    /**
     * 时间转换
     *
     * @param time    时间
     * @param timeFormat 时间的格式 eg: yyyy-MM-dd hh:mm:ss
     * @return  String
     */
    fun convert(time: String, timeFormat: String?): String? {
        timeMills = time.toLong()
        val date = Date(timeMills)
        var strs = ""
        try {
            val sdf = SimpleDateFormat(timeFormat)
            strs = sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strs
    }


    /**
     *
     * @param startTime  开始的事件
     * @return  返回距离发帖时间的时间差
     */
    fun twoDateDistance(startTime: String): String? {
        if (startTime == "") {
            return ""
        }
        timeMills = startTime.toLong()
        val startDate = Date(timeMills)
        if (startDate == null || endDate == null) {
            return null
        }
        var timeLong = endDate!!.time - startDate.time
        return if (timeLong <= 0) {
            "刚刚"
        } else if (timeLong < 60 * 1000) {
            (timeLong / 1000).toString() + "秒前"
        } else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60
            timeLong.toString() + "分钟前"
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000
            timeLong.toString() + "小时前"
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24
            timeLong.toString() + "天前"
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7
            timeLong.toString() + "周前"
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24
            timeLong.toString() + "天前"
        }
    }

    /**
     * UTM转换成日期描述，如三周前，上午，昨天等
     *
     * @param milliseconds milliseconds
     * @param isShowWeek 是否采用周的形式显示  true 显示为3周前，false 则显示为时间格式mm-dd
     * @return  如三周前，上午，昨天等
     */
    fun getTimeDesc(milliseconds: Long, isShowWeek: Boolean): String? {
        val sb = StringBuffer()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        val hour = calendar[Calendar.HOUR_OF_DAY].toLong()
        calendar.timeInMillis = System.currentTimeMillis()
        val hourNow = calendar[Calendar.HOUR_OF_DAY].toLong()
        Log.v(
            "---------->---",
            System.currentTimeMillis().toString() + "----------" + milliseconds
        )
        val datetime = System.currentTimeMillis() - milliseconds
        val day =
            Math.floor(datetime / 24 / 60 / 60 / 1000.0f.toDouble())
                .toLong() + if (hourNow < hour) 1 else 0 // 天前
        if (day <= 7) { // 一周内
            if (day == 0L) { // 今天
                if (hour <= 4) {
                    sb.append(" 凌晨 ")
                } else if (hour in 5..6) {
                    sb.append(" 早上 ")
                } else if (hour in 7..11) {
                    sb.append(" 上午 ")
                } else if (hour in 12..13) {
                    sb.append(" 中午 ")
                } else if (hour in 14..18) {
                    sb.append(" 下午 ")
                } else if (hour == 19L) {
                    sb.append(" 傍晚 ")
                } else if (hour in 20..24) {
                    sb.append(" 晚上 ")
                } else {
                    sb.append("今天 ")
                }
            } else if (day == 1L) { // 昨天
                sb.append(" 昨天 ")
            } else if (day == 2L) { // 前天
                sb.append(" 前天 ")
            } else {
                sb.append(" " + DateToWeek(milliseconds) + " ")
            }
        } else { // 一周之前
            if (isShowWeek) {
                sb.append((if (day % 7 == 0L) day / 7 else day / 7 + 1).toString() + "周前")
            } else {
                formatBuilder = SimpleDateFormat("MM-dd")
                val time = formatBuilder!!.format(milliseconds)
                sb.append(time)
            }
        }
        Log.v("sb---", sb.toString() + "")
        return sb.toString()
    }

    /**
     * UTM转换成日期描述，如三周前，上午，昨天等
     *
     * @param milliseconds    时间
     * @return UTM转换成日期描述，如三周前，上午，昨天等
     */
    fun getTimeDesc(milliseconds: Long): String? {
        return getTimeDesc(milliseconds, true)
    }

    /**
     * UTM转换成日期 ,hh:mm
     *
     * @param milliseconds  milliseconds
     * @return UTM转换成日期 ,hh:mm
     */
    fun getDisplayTime(milliseconds: Long): String? {
        formatBuilder = SimpleDateFormat("HH:mm")
        return formatBuilder!!.format(milliseconds)
    }

    /**
     * UTM转换成带描述的日期
     *
     * @param milliseconds  milliseconds
     * @return   UTM转换成带描述的日期
     */
    fun getDisplayTimeAndDesc(milliseconds: Long): String? {
        formatBuilder = SimpleDateFormat("HH:mm")
        val time = formatBuilder!!.format(milliseconds)
        val sb = StringBuffer()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        val hour = calendar[Calendar.HOUR_OF_DAY].toLong()
        Log.v(
            "---------->---",
            System.currentTimeMillis().toString() + "----------" + milliseconds
        )
        val datetime = System.currentTimeMillis() - milliseconds
        val day =
            Math.ceil(datetime / 24 / 60 / 60 / 1000.0f.toDouble()).toLong() // 天前
        Log.v("day---hour---time---", "$day----$hour-----$time")
        if (day <= 7) { // 一周内
            if (day == 0L) { // 今天
                if (hour <= 4) {
                    sb.append(" 凌晨 $time")
                } else if (hour in 5..6) {
                    sb.append(" 早上 $time")
                } else if (hour in 7..11) {
                    sb.append(" 上午 $time")
                } else if (hour in 12..13) {
                    sb.append(" 中午 $time")
                } else if (hour in 14..18) {
                    sb.append(" 下午 $time")
                } else if (hour == 19L) {
                    sb.append(" 傍晚 $time")
                } else if (hour in 20..24) {
                    sb.append(" 晚上 $time")
                } else {
                    sb.append("今天 $time")
                }
            } else if (day == 1L) { // 昨天
                sb.append("昨天 $time")
            } else if (day == 2L) { // 前天
                sb.append("前天 $time")
            } else {
                sb.append(DateToWeek(milliseconds) + time)
            }
        } else { // 一周之前
            sb.append((day % 7).toString() + "周前")
        }
        Log.v("sb---", sb.toString() + "")
        return sb.toString()
    }

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param milliseconds    data
     * @return  日期变量转成对应的星期字符串
     */
    fun DateToWeek(milliseconds: Long): String? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        val dayIndex = calendar[Calendar.DAY_OF_WEEK]
        return if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            null
        } else WEEK[dayIndex - 1]
    }

    /**
     * 将时间间隔转换成描述性字符串，如2天前，3月1天后等。
     * @param toDate 相对的日期
     * @param isFull 是否全部显示 true 全部显示 false 简单显示
     * @return  将时间间隔转换成描述性字符串，如2天前，3月1天后等。
     */
    fun diffDateAsDesc(toDate: Date, isFull: Boolean): String? {
        var diffDesc = ""
        var fix = ""
        var diffTime: Long
        val curDate = Date()
        if (curDate.time > toDate.time) {
            diffTime = curDate.time - toDate.time
            fix = "前"
        } else {
            diffTime = toDate.time - curDate.time
            fix = "后"
        }

        //换算成分钟数，防止Int溢出。
        diffTime = diffTime / 1000 / 60
        val year = diffTime / (60 * 24 * 30 * 12)
        diffTime %= (60 * 24 * 30 * 12)
        if (year > 0) {
            diffDesc = diffDesc + year + "年"
            if (!isFull) {
                return diffDesc + fix
            }
        }
        val month = diffTime / (60 * 24 * 30)
        diffTime = diffTime % (60 * 24 * 30)
        if (month > 0) {
            diffDesc = diffDesc + month + "月"
            if (!isFull) {
                return diffDesc + fix
            }
        }
        val day = diffTime / 60 / 24
        diffTime %= (60 * 24)
        if (day > 0) {
            diffDesc = diffDesc + day + "天"
            if (!isFull) {
                return diffDesc + fix
            }
        }
        val hour = diffTime / 60
        diffTime %= 60
        if (hour > 0) {
            diffDesc = diffDesc + hour + "时"
            if (!isFull) {
                return diffDesc + fix
            }
        }
        val minitue = diffTime
        if (minitue > 0) {
            diffDesc = diffDesc + minitue + "分"
            if (!isFull) {
                return diffDesc + fix
            }
        }
        return diffDesc + fix
    }
}
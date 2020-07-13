package com.oplus.fwandroid.common.utils

import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：日期工具类,主要实现了日期的常用操作
 * version: 1.0
 */
object DateUtil {
    /**
     * yyyy-MM-dd HH:mm:ss字符串
     */
    const val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    /**
     * yyyy-MM-dd字符串
     */
    const val DEFAULT_FORMAT_DATE = "yyyy-MM-dd"

    /**
     * HH:mm:ss字符串
     */
    const val DEFAULT_FORMAT_TIME = "HH:mm:ss"

    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    val defaultDateTimeFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT)
            }
        }

    /**
     * yyyy-MM-dd格式
     */
    val defaultDateFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat(DEFAULT_FORMAT_DATE)
            }
        }

    /**
     * HH:mm:ss格式
     */
    val defaultTimeFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat(DEFAULT_FORMAT_TIME)
            }
        }

    private fun DateUtil() {
        throw RuntimeException("￣ 3￣")
    }

    /**
     * 将long时间转成yyyy-MM-dd HH:mm:ss字符串<br></br>
     *
     * @param timeInMillis 时间long值
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun getDateTimeFromMillis(timeInMillis: Long): String? {
        return getDateTimeFormat(java.util.Date(timeInMillis))
    }

    /**
     * 将long时间转成yyyy-MM-dd字符串<br></br>
     *
     * @param timeInMillis
     * @return yyyy-MM-dd
     */
    fun getDateFromMillis(timeInMillis: Long): String? {
        return getDateFormat(java.util.Date(timeInMillis))
    }

    /**
     * 将date转成yyyy-MM-dd HH:mm:ss字符串
     * <br></br>
     *
     * @param date Date对象
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun getDateTimeFormat(date: java.util.Date?): String? {
        return dateSimpleFormat(date, defaultDateTimeFormat.get())
    }

    /**
     * 将年月日的int转成yyyy-MM-dd的字符串
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     * 注：月表示Calendar的月，比实际小1
     * 对输入项未做判断
     */
    fun getDateFormat(year: Int, month: Int, day: Int): String? {
        return getDateFormat(getDate(year, month, day))
    }

    /**
     * 将date转成yyyy-MM-dd字符串<br></br>
     *
     * @param date Date对象
     * @return yyyy-MM-dd
     */
    fun getDateFormat(date: java.util.Date?): String {
        return dateSimpleFormat(date, defaultDateFormat.get())
    }

    /**
     * 获得HH:mm:ss的时间
     *
     * @param date
     * @return
     */
    fun getTimeFormat(date: java.util.Date?): String? {
        return dateSimpleFormat(date, defaultTimeFormat.get())
    }

    /**
     * 格式化日期显示格式
     *
     * @param sdate  原始日期格式 "yyyy-MM-dd"
     * @param format 格式化后日期格式
     * @return 格式化后的日期显示
     */
    fun dateFormat(sdate: String?, format: String?): String? {
        val formatter = SimpleDateFormat(format)
        val date = Date.valueOf(sdate)
        return dateSimpleFormat(date, formatter)
    }

    /**
     * 格式化日期显示格式
     *
     * @param date   Date对象
     * @param format 格式化后日期格式
     * @return 格式化后的日期显示
     */
    fun dateFormat(date: java.util.Date?, format: String?): String? {
        val formatter = SimpleDateFormat(format)
        return dateSimpleFormat(date, formatter)
    }

    /**
     * 将date转成字符串
     *
     * @param date   Date
     * @param format SimpleDateFormat
     * <br></br>
     * 注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun dateSimpleFormat(
        date: java.util.Date?,
        format: SimpleDateFormat?
    ): String {
        var format = format
        if (format == null) format = defaultDateTimeFormat.get()
        return if (date == null) "" else format!!.format(date)
    }

    /**
     * 将"yyyy-MM-dd HH:mm:ss" 格式的字符串转成Date
     *
     * @param strDate 时间字符串
     * @return Date
     */
    fun getDateByDateTimeFormat(strDate: String): java.util.Date? {
        return getDateByFormat(strDate, defaultDateTimeFormat.get())
    }

    /**
     * 将"yyyy-MM-dd" 格式的字符串转成Date
     *
     * @param strDate
     * @return Date
     */
    fun getDateByDateFormat(strDate: String): java.util.Date? {
        return getDateByFormat(strDate, defaultDateFormat.get())
    }

    /**
     * 将指定格式的时间字符串转成Date对象
     *
     * @param strDate 时间字符串
     * @param format  格式化字符串
     * @return Date
     */
    fun getDateByFormat(strDate: String, format: String?): java.util.Date? {
        return getDateByFormat(strDate, SimpleDateFormat(format))
    }

    /**
     * 将String字符串按照一定格式转成Date<br></br>
     * 注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     *
     * @param strDate 时间字符串
     * @param format  SimpleDateFormat对象
     * @throws ParseException 日期格式转换出错
     */
    private fun getDateByFormat(
        strDate: String,
        format: SimpleDateFormat?
    ): java.util.Date? {
        var format = format
        if (format == null) format = defaultDateTimeFormat.get()
        try {
            return format!!.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将年月日的int转成date
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     * 注：月表示Calendar的月，比实际小1
     */
    fun getDate(year: Int, month: Int, day: Int): java.util.Date? {
        val mCalendar = Calendar.getInstance()
        mCalendar[year, month - 1] = day
        return mCalendar.time
    }

    /**
     * 求两个日期相差天数
     *
     * @param strat 起始日期，格式yyyy-MM-dd
     * @param end   终止日期，格式yyyy-MM-dd
     * @return 两个日期相差天数
     */
    fun getIntervalDays(strat: String?, end: String?): Long {
        return (Date.valueOf(end).time - Date
            .valueOf(strat).time) / (3600 * 24 * 1000)
    }

    /**
     * 求两个日期相差毫秒数
     *
     * @param strat 起始日期，格式yyyy-MM-dd HH:mm:ss
     * @param end   终止日期，格式yyyy-MM-dd HH:mm:ss
     * @return 两个日期相差毫秒数
     */
    fun getIntervalMills(strat: String, end: String): Long {
        return getDateByDateTimeFormat(end)!!.time - getDateByDateTimeFormat(strat)!!.time
    }

    /**
     * 当前时间距离目标时间相差毫秒数
     * >0 还没到目标时间
     * <0 超过目标时间了
     *
     * @param end   终止日期，格式yyyy-MM-dd HH:mm:ss
     * @return 两个日期相差毫秒数
     */
    fun getIntervalMills(end: String): Long {
        return getDateByDateTimeFormat(end)!!.time - Date().time
    }

    /**
     * 求两个日期相差时间
     *
     * @param strat 起始日期，格式yyyy-MM-dd HH:mm:ss
     * @param end   终止日期，格式yyyy-MM-dd HH:mm:ss
     * @return 两个日期相差时间 x天x时x分x秒
     */
    fun getIntervalTime(strat: String, end: String): LongArray? {
        val times = LongArray(4)
        val intervalTime =
            getDateByDateTimeFormat(end)!!.time - getDateByDateTimeFormat(strat)!!.time
        val day = intervalTime / (3600 * 24 * 1000)
        val hour = intervalTime / (3600 * 1000) % 24
        val minute = intervalTime / (60 * 1000) % 60
        val second = intervalTime / 1000 % 60
        times[0] = day
        times[1] = hour
        times[2] = minute
        times[3] = second
        return times
    }

    /**
     * 将毫秒转化为HH:mm:ss
     *
     * @param mills
     * @return
     */
    fun getHHmmss(mills: Long): String? {
        val hour = mills / (3600 * 1000)
        val minute = mills / (60 * 1000) % 60
        val second = mills / 1000 % 60
        return String.format("%02d", hour) + ":" + String.format(
            "%02d",
            minute
        ) + ":" + String.format("%02d", second)
    }

    /**
     * 获得当前年份
     *
     * @return year(int)
     */
    fun getCurrentYear(): Int {
        val mCalendar = Calendar.getInstance()
        return mCalendar[Calendar.YEAR]
    }

    /**
     * 获得当前月份
     *
     * @return month(int) 1-12
     */
    fun getCurrentMonth(): Int {
        val mCalendar = Calendar.getInstance()
        return mCalendar[Calendar.MONTH] + 1
    }

    /**
     * 获得当月几号
     *
     * @return day(int)
     */
    fun getDayOfMonth(): Int {
        val mCalendar = Calendar.getInstance()
        return mCalendar[Calendar.DAY_OF_MONTH]
    }

    /**
     * 获得今天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    fun getToday(): String {
        val mCalendar = Calendar.getInstance()
        return getDateFormat(mCalendar.time)
    }

    /**
     * 获得昨天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    fun getYesterday(): String? {
        val mCalendar = Calendar.getInstance()
        mCalendar.add(Calendar.DATE, -1)
        return getDateFormat(mCalendar.time)
    }

    /**
     * 获得前天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    fun getBeforeYesterday(): String? {
        val mCalendar = Calendar.getInstance()
        mCalendar.add(Calendar.DATE, -2)
        return getDateFormat(mCalendar.time)
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return
     */
    fun getOtherDay(diff: Int): String? {
        val mCalendar = Calendar.getInstance()
        mCalendar.add(Calendar.DATE, diff)
        return getDateFormat(mCalendar.time)
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param sDate  给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    fun getCalcDateFormat(sDate: String, amount: Int): String? {
        val date = getCalcDate(getDateByDateFormat(sDate), amount)
        return getDateFormat(date)
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param date   给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    fun getCalcDate(date: java.util.Date?, amount: Int): java.util.Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, amount)
        return cal.time
    }

    /**
     * 获得一个计算十分秒之后的日期对象
     *
     * @param date
     * @param hOffset 时偏移量，可为负
     * @param mOffset 分偏移量，可为负
     * @param sOffset 秒偏移量，可为负
     * @return
     */
    fun getCalcTime(
        date: java.util.Date?,
        hOffset: Int,
        mOffset: Int,
        sOffset: Int
    ): java.util.Date? {
        val cal = Calendar.getInstance()
        if (date != null) cal.time = date
        cal.add(Calendar.HOUR_OF_DAY, hOffset)
        cal.add(Calendar.MINUTE, mOffset)
        cal.add(Calendar.SECOND, sOffset)
        return cal.time
    }

    /**
     * 根据指定的年月日小时分秒，返回一个java.Util.Date对象。
     *
     * @param year      年
     * @param month     月 0-11
     * @param date      日
     * @param hourOfDay 小时 0-23
     * @param minute    分 0-59
     * @param second    秒 0-59
     * @return 一个Date对象
     */
    fun getDate(
        year: Int, month: Int, date: Int, hourOfDay: Int,
        minute: Int, second: Int
    ): java.util.Date? {
        val cal = Calendar.getInstance()
        cal[year, month, date, hourOfDay, minute] = second
        return cal.time
    }

    /**
     * 获得年月日数据
     *
     * @param sDate yyyy-MM-dd格式
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    fun getYearMonthAndDayFrom(sDate: String): IntArray {
        return getYearMonthAndDayFromDate(getDateByDateFormat(sDate))
    }

    /**
     * 获得年月日数据
     *
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    fun getYearMonthAndDayFromDate(date: java.util.Date?): IntArray {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val arr = IntArray(3)
        arr[0] = calendar[Calendar.YEAR]
        arr[1] = calendar[Calendar.MONTH]
        arr[2] = calendar[Calendar.DAY_OF_MONTH]
        return arr
    }

    /**
     * 比较两个日期先后，比较的日期时间格式是yyyy-MM-dd HH:mm:ss
     *
     * @param time1
     * @param time2
     * @return >0 time1较近 <0 time2较近 =0 一样近
     */
    fun compareDate(time1: String?, time2: String?): Int {
        val simpleDateFormat = defaultDateTimeFormat.get()
        try {
            val time_1 = simpleDateFormat!!.parse(time1).time
            val time_2 = simpleDateFormat.parse(time2).time
            return if (time_1 > time_2) 1 else if (time_1 < time_2) -1 else 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 比较两个日期先后，比较的日期时间格式是yyyy-MM-dd HH:mm-HH:mm
     *
     * @param time1
     * @param time2
     * @return >0 time1较近 <0 time2较近 =0 一样近
     */
    fun compareDate2(time1: String, time2: String): Int {
        val simpleDateFormat = defaultDateFormat.get()
        try {
            val time_1 =
                simpleDateFormat!!.parse(time1.split(" ").toTypedArray()[0]).time
            val time_2 =
                simpleDateFormat.parse(time2.split(" ").toTypedArray()[0]).time
            return if (time_1 > time_2) 1 else if (time_1 < time_2) -1 else {
                val t1 = time1.replace(":", "").replace("-", "").toInt()
                val t2 = time2.replace(":", "").replace("-", "").toInt()
                t1 - t2
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 比较日期是否和今天相差某个天数
     *
     * @param end       (格式：yyyy-MM-dd)
     * @param intervals
     * @return
     */
    fun compareDate(end: String?, vararg intervals: Int): Boolean {
        val intervalDays = getIntervalDays(getToday(), end)
        for (intervalDay in intervals) {
            if (intervalDays == intervalDay.toLong()) return true
        }
        return false
    }

    /**
     * 转化时间
     * 如果是同一天显示HH:mm,不同天是MM月dd日
     *
     * @param date
     * @return
     */
    fun formatDate(date: String): String? {
        return try {
            val yearMonthAndDayFrom =
                getYearMonthAndDayFrom(date.split(" ").toTypedArray()[0])
            val split = getToday().split("-").toTypedArray()
            if (yearMonthAndDayFrom[0] == split[0]
                    .toInt() && yearMonthAndDayFrom[1] + 1 == split[1]
                    .toInt() && yearMonthAndDayFrom[2] == split[2].toInt()
            ) {
                //是同一天
                val s = date.split(" ").toTypedArray()[1]
                s.substring(0, s.lastIndexOf(":"))
            } else {
                "" + (yearMonthAndDayFrom[1] + 1) + "月" + yearMonthAndDayFrom[2] + "日"
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            ""
        }
    }
}
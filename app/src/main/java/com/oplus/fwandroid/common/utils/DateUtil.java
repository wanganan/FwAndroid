package com.oplus.fwandroid.common.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h3>日期工具类</h3>
 * <p>主要实现了日期的常用操作
 */
@SuppressLint("SimpleDateFormat")
public final class DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss字符串
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd字符串
     */
    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd";

    /**
     * HH:mm:ss字符串
     */
    public static final String DEFAULT_FORMAT_TIME = "HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    public static final ThreadLocal<SimpleDateFormat> defaultDateTimeFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        }

    };

    /**
     * yyyy-MM-dd格式
     */
    public static final ThreadLocal<SimpleDateFormat> defaultDateFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_FORMAT_DATE);
        }

    };

    /**
     * HH:mm:ss格式
     */
    public static final ThreadLocal<SimpleDateFormat> defaultTimeFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_FORMAT_TIME);
        }

    };

    private DateUtil() {
        throw new RuntimeException("￣ 3￣");
    }

    /**
     * 将long时间转成yyyy-MM-dd HH:mm:ss字符串<br>
     *
     * @param timeInMillis 时间long值
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeFromMillis(long timeInMillis) {
        return getDateTimeFormat(new Date(timeInMillis));
    }

    /**
     * 将long时间转成yyyy-MM-dd字符串<br>
     *
     * @param timeInMillis
     * @return yyyy-MM-dd
     */
    public static String getDateFromMillis(long timeInMillis) {
        return getDateFormat(new Date(timeInMillis));
    }

    /**
     * 将date转成yyyy-MM-dd HH:mm:ss字符串
     * <br>
     *
     * @param date Date对象
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeFormat(Date date) {
        return dateSimpleFormat(date, defaultDateTimeFormat.get());
    }

    /**
     * 将年月日的int转成yyyy-MM-dd的字符串
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     *              注：月表示Calendar的月，比实际小1
     *              对输入项未做判断
     */
    public static String getDateFormat(int year, int month, int day) {
        return getDateFormat(getDate(year, month, day));
    }

    /**
     * 将date转成yyyy-MM-dd字符串<br>
     *
     * @param date Date对象
     * @return yyyy-MM-dd
     */
    public static String getDateFormat(Date date) {
        return dateSimpleFormat(date, defaultDateFormat.get());
    }

    /**
     * 获得HH:mm:ss的时间
     *
     * @param date
     * @return
     */
    public static String getTimeFormat(Date date) {
        return dateSimpleFormat(date, defaultTimeFormat.get());
    }

    /**
     * 格式化日期显示格式
     *
     * @param sdate  原始日期格式 "yyyy-MM-dd"
     * @param format 格式化后日期格式
     * @return 格式化后的日期显示
     */
    public static String dateFormat(String sdate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        java.sql.Date date = java.sql.Date.valueOf(sdate);
        return dateSimpleFormat(date, formatter);
    }

    /**
     * 格式化日期显示格式
     *
     * @param date   Date对象
     * @param format 格式化后日期格式
     * @return 格式化后的日期显示
     */
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return dateSimpleFormat(date, formatter);
    }

    /**
     * 将date转成字符串
     *
     * @param date   Date
     * @param format SimpleDateFormat
     *               <br>
     *               注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateSimpleFormat(Date date, SimpleDateFormat format) {
        if (format == null)
            format = defaultDateTimeFormat.get();
        return (date == null ? "" : format.format(date));
    }

    /**
     * 将"yyyy-MM-dd HH:mm:ss" 格式的字符串转成Date
     *
     * @param strDate 时间字符串
     * @return Date
     */
    public static Date getDateByDateTimeFormat(String strDate) {
        return getDateByFormat(strDate, defaultDateTimeFormat.get());
    }

    /**
     * 将"yyyy-MM-dd" 格式的字符串转成Date
     *
     * @param strDate
     * @return Date
     */
    public static Date getDateByDateFormat(String strDate) {
        return getDateByFormat(strDate, defaultDateFormat.get());
    }

    /**
     * 将指定格式的时间字符串转成Date对象
     *
     * @param strDate 时间字符串
     * @param format  格式化字符串
     * @return Date
     */
    public static Date getDateByFormat(String strDate, String format) {
        return getDateByFormat(strDate, new SimpleDateFormat(format));
    }

    /**
     * 将String字符串按照一定格式转成Date<br>
     * 注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     *
     * @param strDate 时间字符串
     * @param format  SimpleDateFormat对象
     * @throws ParseException 日期格式转换出错
     */
    private static Date getDateByFormat(String strDate, SimpleDateFormat format) {
        if (format == null)
            format = defaultDateTimeFormat.get();
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将年月日的int转成date
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     *              注：月表示Calendar的月，比实际小1
     */
    public static Date getDate(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, day);
        return mCalendar.getTime();
    }

    /**
     * 求两个日期相差天数
     *
     * @param strat 起始日期，格式yyyy-MM-dd
     * @param end   终止日期，格式yyyy-MM-dd
     * @return 两个日期相差天数
     */
    public static long getIntervalDays(String strat, String end) {
        return ((java.sql.Date.valueOf(end)).getTime() - (java.sql.Date
                .valueOf(strat)).getTime()) / (3600 * 24 * 1000);
    }

    /**
     * 求两个日期相差毫秒数
     *
     * @param strat 起始日期，格式yyyy-MM-dd HH:mm:ss
     * @param end   终止日期，格式yyyy-MM-dd HH:mm:ss
     * @return 两个日期相差毫秒数
     */
    public static long getIntervalMills(String strat, String end) {
        return getDateByDateTimeFormat(end).getTime() - getDateByDateTimeFormat(strat).getTime();
    }

    /**
     * 当前时间距离目标时间相差毫秒数
     * >0 还没到目标时间
     * <0 超过目标时间了
     *
     * @param end   终止日期，格式yyyy-MM-dd HH:mm:ss
     * @return 两个日期相差毫秒数
     */
    public static long getIntervalMills(String end) {
        return getDateByDateTimeFormat(end).getTime() - new Date().getTime();
    }

    /**
     * 求两个日期相差时间
     *
     * @param strat 起始日期，格式yyyy-MM-dd HH:mm:ss
     * @param end   终止日期，格式yyyy-MM-dd HH:mm:ss
     * @return 两个日期相差时间 x天x时x分x秒
     */
    public static long[] getIntervalTime(String strat, String end) {
        long[] times = new long[4];
        long intervalTime = getDateByDateTimeFormat(end).getTime() - getDateByDateTimeFormat(strat).getTime();
        long day = intervalTime / (3600 * 24 * 1000);
        long hour = (intervalTime / (3600 * 1000)) % 24;
        long minute = (intervalTime / (60 * 1000)) % 60;
        long second = (intervalTime / 1000) % 60;
        times[0] = day;
        times[1] = hour;
        times[2] = minute;
        times[3] = second;
        return times;
    }

    /**
     * 将毫秒转化为HH:mm:ss
     *
     * @param mills
     * @return
     */
    public static String getHHmmss(long mills) {
        long hour = (mills / (3600 * 1000));
        long minute = (mills / (60 * 1000)) % 60;
        long second = (mills / 1000) % 60;
        return String.format("%02d", hour)+":"+String.format("%02d", minute)+":"+String.format("%02d", second);
    }

    /**
     * 获得当前年份
     *
     * @return year(int)
     */
    public static int getCurrentYear() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     *
     * @return month(int) 1-12
     */
    public static int getCurrentMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当月几号
     *
     * @return day(int)
     */
    public static int getDayOfMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得今天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static String getToday() {
        Calendar mCalendar = Calendar.getInstance();
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 获得昨天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static String getYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -1);
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 获得前天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static String getBeforeYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -2);
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return
     */
    public static String getOtherDay(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param sDate  给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static String getCalcDateFormat(String sDate, int amount) {
        Date date = getCalcDate(getDateByDateFormat(sDate), amount);
        return getDateFormat(date);
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param date   给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static Date getCalcDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
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
    public static Date getCalcTime(Date date, int hOffset, int mOffset, int sOffset) {
        Calendar cal = Calendar.getInstance();
        if (date != null)
            cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hOffset);
        cal.add(Calendar.MINUTE, mOffset);
        cal.add(Calendar.SECOND, sOffset);
        return cal.getTime();
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
    public static Date getDate(int year, int month, int date, int hourOfDay,
                               int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }

    /**
     * 获得年月日数据
     *
     * @param sDate yyyy-MM-dd格式
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    public static int[] getYearMonthAndDayFrom(String sDate) {
        return getYearMonthAndDayFromDate(getDateByDateFormat(sDate));
    }

    /**
     * 获得年月日数据
     *
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    public static int[] getYearMonthAndDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] arr = new int[3];
        arr[0] = calendar.get(Calendar.YEAR);
        arr[1] = calendar.get(Calendar.MONTH);
        arr[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return arr;
    }

    /**
     * 比较两个日期先后，比较的日期时间格式是yyyy-MM-dd HH:mm:ss
     *
     * @param time1
     * @param time2
     * @return >0 time1较近 <0 time2较近 =0 一样近
     */
    public static int compareDate(String time1, String time2) {
        SimpleDateFormat simpleDateFormat = defaultDateTimeFormat.get();
        try {
            long time_1 = simpleDateFormat.parse(time1).getTime();
            long time_2 = simpleDateFormat.parse(time2).getTime();
            if (time_1 > time_2)
                return 1;
            else if (time_1 < time_2)
                return -1;
            else
                return 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较两个日期先后，比较的日期时间格式是yyyy-MM-dd HH:mm-HH:mm
     *
     * @param time1
     * @param time2
     * @return >0 time1较近 <0 time2较近 =0 一样近
     */
    public static int compareDate2(String time1, String time2) {
        SimpleDateFormat simpleDateFormat = defaultDateFormat.get();
        try {
            long time_1 = simpleDateFormat.parse(time1.split(" ")[0]).getTime();
            long time_2 = simpleDateFormat.parse(time2.split(" ")[0]).getTime();
            if (time_1 > time_2)
                return 1;
            else if (time_1 < time_2)
                return -1;
            else {
                int t1 = Integer.parseInt(time1.replace(":", "").replace("-", ""));
                int t2 = Integer.parseInt(time2.replace(":", "").replace("-", ""));
                return t1 - t2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较日期是否和今天相差某个天数
     *
     * @param end       (格式：yyyy-MM-dd)
     * @param intervals
     * @return
     */
    public static boolean compareDate(String end, int... intervals) {
        long intervalDays = getIntervalDays(getToday(), end);
        for (int intervalDay : intervals) {
            if (intervalDays == intervalDay)
                return true;
        }
        return false;
    }

    /**
     * 转化时间
     * 如果是同一天显示HH:mm,不同天是MM月dd日
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        try {
            int[] yearMonthAndDayFrom = getYearMonthAndDayFrom(date.split(" ")[0]);
            String[] split = getToday().split("-");
            if (yearMonthAndDayFrom[0] == Integer.parseInt(split[0]) && yearMonthAndDayFrom[1] + 1 == Integer.parseInt(split[1]) && yearMonthAndDayFrom[2] == Integer.parseInt(split[2])) {
                //是同一天
                String s = date.split(" ")[1];
                return s.substring(0, s.lastIndexOf(":"));
            } else {
                return yearMonthAndDayFrom[1] + 1 + "月" + yearMonthAndDayFrom[2] + "日";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }
}

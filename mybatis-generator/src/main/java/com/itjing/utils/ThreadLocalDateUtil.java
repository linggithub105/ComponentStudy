package com.itjing.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年07月20日 11:51
 * @description
 */
public class ThreadLocalDateUtil {

    private ThreadLocalDateUtil() {
    }

    /**
     * 初始化部分格式
     */
    private static final String[] date_format = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "dd"};

    private static ThreadLocal<Map<String, DateFormat>> threadLocal = new ThreadLocal<>();

    //private static  Map<String, DateFormat> df = new HashMap<>();

    static {
        init();
    }

    private static Map<String, DateFormat> init() {
        Map<String, DateFormat> df = new HashMap<>();
        df.put(date_format[0], new SimpleDateFormat(date_format[0]));
        df.put(date_format[1], new SimpleDateFormat(date_format[1]));
        df.put(date_format[2], new SimpleDateFormat(date_format[2]));
        threadLocal.set(df);
        return df;
    }

    public static Map<String, DateFormat> dfValue() {
        Map<java.lang.String, java.text.DateFormat> value = threadLocal.get();
        if (value == null) {
            return init();
        }
        return value;
    }

    /**
     * 获取时间格式
     *
     * @param key
     * @return
     */
    public static DateFormat getDateFormat(String key) {
        Map<String, DateFormat> df = dfValue();
        if (!df.containsKey(key)) {
            df.put(key, new SimpleDateFormat(key));
        }
        return df.get(key);
    }

    /**
     * 获取一个时间字符串 自定义时分秒
     *
     * @param date 时间
     * @param hour 小时
     * @param min  分组
     * @param sec  秒
     * @param key  格式
     * @return
     */
    public static String getDateString(Date date, int hour, int min, int sec, String key) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        return getDateFormat(key).format(cal.getTime());
    }

    /**
     * 获取一个时间 自定义时分秒
     *
     * @param date 时间
     * @param hour 小时
     * @param min  分钟
     * @param sec  秒
     * @return 时间
     */
    public static Date getDate(Date date, int hour, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        return cal.getTime();
    }

    /**
     * 获取一个自定义的时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param hour  时
     * @param min   分
     * @param sec   秒
     * @param mi    毫秒
     * @return 时间
     */
    public static Date getDate(int year, int month, int day, int hour, int min, int sec, int mi) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, mi);
        return cal.getTime();
    }

    /**
     * 将时间转换成 年月日格式字符串
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return getDateFormat(date_format[0]).format(date);
    }

    /**
     * 将；一个时间对象转换成 年月日字符串
     *
     * @param obj 时间对象
     * @return
     */
    public static String formatDate(Object obj) {
        if (obj instanceof Date) {
            return getDateFormat(date_format[0]).format(obj);
        }
        return "";
    }

    /**
     * 将一个 时间对象转换成 任意指定格式的字符串
     *
     * @param obj 时间对象
     * @param key 格式
     * @return
     */
    public static String formatDate(Object obj, String key) {
        if (obj instanceof Date) {
            return getDateFormat(key).format(obj);
        }
        return "";
    }

    /**
     * 将时间转换成 年月日时分秒 格式的字符串
     *
     * @param date 时间
     * @return
     */
    public static String formatDateTime(Date date) {
        return getDateFormat(date_format[1]).format(date);
    }

    /**
     * 将时间对象转换成年月日时分秒格式字符串
     *
     * @param obj 时间对象
     * @return
     */
    public static String formatDateTime(Object obj) {
        if (obj instanceof Date) {
            return getDateFormat(date_format[1]).format(obj);
        }
        return "";
    }

    /**
     * 将时间转换成指定格式的字符串
     *
     * @param date 时间格式
     * @param i    格式
     * @return
     */
    public static String formatDate(Date date, String i) {
        return getDateFormat(i).format(date);
    }

    /**
     * 将字符串格式转换为时间格式
     *
     * @param strDate 时间字符串
     * @param i       格式
     * @return
     */
    public static Date parse(String strDate, String i) {
        try {
            return getDateFormat(i).parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取一个时间 指定日期 和格式
     *
     * @param i 天
     * @param j 格式
     * @return
     * @throws ParseException
     */
    public static String getNextDay(int i, String j) throws ParseException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return ThreadLocalDateUtil.formatDate(date, j);
    }

    /**
     * 获取一个时间 指定 时间 日期 和格式
     *
     * @param date 时间
     * @param i    天
     * @param j    格式
     * @return
     * @throws ParseException
     */
    public static String getNextDay(Date date, int i, String j) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return ThreadLocalDateUtil.formatDate(date, j);
    }


    /**
     * 获取一个时间 指定 时间和格式
     *
     * @param date
     * @param i
     * @return
     * @throws ParseException
     */
    public static Date getNextDate(Date date, int i) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取一个时间 指定天数
     *
     * @param i
     * @return
     * @throws ParseException
     */
    public static Date getNextDate(int i) throws ParseException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }


    public static boolean isNull(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    public static boolean isNull(Object str) {
        return str == null || isNull(str.toString());
    }

    public static String strvalueOf(String str) {
        if (isNull(str)) {
            return "";
        }
        return str;
    }

    public static String intvalueOf(String str) {
        if (isNull(str)) {
            return "0";
        }
        return str;
    }

    /**
     * 将时间转换为可读字符串
     *
     * @param ms 毫秒数
     * @return
     */
    public static String getTimeDes(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuilder str = new StringBuilder();

        if (day > 365 * 100) {
            str.append(day / (365 * 100)).append("世纪");
        } else if (day > 365) {
            str.append(day / 365).append("年");
        } else if (day > 0) {
            str.append(day).append("天");
        } else if (hour > 0) {
            str.append(hour).append("小时");
        } else if (minute > 0) {
            str.append(minute).append("分钟");
        } else if (second > 0) {
            str.append(second).append("秒");
        } else if (milliSecond > 0) {
            str.append(1).append("秒");
        }

        return str.toString();
    }

    /**
     * 获取上周五时间
     */
    public static Date lastFirday() {

        //作用防止周日得到本周日期
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 7 - dayOfWeek;
        calendar.add(Calendar.DATE, offset - 9);

        return getFirstDayOfWeek(calendar.getTime(), 6);//这是从上周日开始数的到本周五为6

    }


    /**
     * 获取上周一时间
     */
    public static Date lastMonday() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 1 - dayOfWeek;
        calendar.add(Calendar.DATE, offset - 7);
        return getFirstDayOfWeek(calendar.getTime(), 2);
    }


    /**
     * 得到某一天的该星期的第一日 00:00:00
     *
     * @param firstDayOfWeek 一个星期的第一天为星期几
     */
    public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(firstDayOfWeek);//设置一星期的第一天是哪一天
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);//指示一个星期中的某天
        cal.set(Calendar.HOUR_OF_DAY,
                0);//指示一天中的小时。HOUR_OF_DAY 用于 24 小时制时钟。例如，在 10:04:15.250 PM 这一时刻，HOUR_OF_DAY 为 22。
        cal.set(Calendar.MINUTE, 0);//指示一小时中的分钟。例如，在 10:04:15.250 PM 这一时刻，MINUTE 为 4。
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


}

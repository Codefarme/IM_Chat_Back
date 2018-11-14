package com.codefarme.imchat.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {


    /**
     * 时间格式转换
     *
     * @param time
     * @param sourceFormat
     * @param targetFormat
     * @return
     * @throws Exception
     */
    public static String format(String time, String sourceFormat, String targetFormat) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat);
        Date date = sdf.parse(time);

        SimpleDateFormat sdf2 = new SimpleDateFormat();
        sdf.applyPattern(targetFormat);
        return sdf.format(date);
    }


    public static Integer getCurrentYear() {
        Calendar can = Calendar.getInstance();
        return can.get(can.YEAR);
    }

    /**
     * 获取time对应的的年份
     *
     * @param time
     * @param format
     * @return
     * @throws Exception
     */
    public static Integer getYear(String time, String format) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            Calendar can = Calendar.getInstance();
            can.setTime(date);
            return can.get(can.YEAR);
        } catch (Exception e) {
            throw e;
        }
    }


    public static Integer getCurrentMonth() {
        Calendar can = Calendar.getInstance();
        return can.get(can.MONTH);
    }


    public static Integer getCurrentDay() {
        Calendar can = Calendar.getInstance();
        return can.get(can.DAY_OF_MONTH);
    }

    /**
     * 获取时间的月份
     *
     * @param time 格式为  yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static Integer getMonth(String time, String format) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            Calendar can = Calendar.getInstance();
            can.setTime(date);
            return can.get(can.MONTH);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取时间在当月的天数
     *
     * @return
     * @throws Exception
     */
    public static Integer getDay(String time, String format) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            Calendar can = Calendar.getInstance();
            can.setTime(date);
            return can.get(can.DAY_OF_MONTH);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 获取时间的小时,24小时制
     *
     * @return
     * @throws Exception
     */
    public static Integer getHour(String time, String format) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            Calendar can = Calendar.getInstance();
            can.setTime(date);
            return can.get(can.HOUR_OF_DAY);
        } catch (Exception e) {
            throw e;
        }
    }

    public static Integer getCurrentHour_Of_Day() {
        Calendar can = Calendar.getInstance();
        return can.get(can.HOUR_OF_DAY);
    }


    public static Integer getCurrentMinute() {
        Calendar can = Calendar.getInstance();
        return can.get(can.MINUTE);
    }


    public static Integer getCurrentSecond() throws Exception {
        try {
            Calendar can = Calendar.getInstance();
            return can.get(can.SECOND);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 判断time是否是当天
     *
     * @return
     */
    public static boolean isCurrentDay(String time, String format) throws Exception {
        try {
            Calendar can = Calendar.getInstance();
            int year = can.get(can.YEAR);
            int month = can.get(can.MONTH);
            int day = can.get(can.DAY_OF_MONTH);

            if (getYear(time, format) - year == 0 && getMonth(time, format) - month == 0 && getDay(time, format) - day == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 返回当前日期
     *
     * @return
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 返回当前时间的字符串   yyyy-MM-dd HH:mm:ss格式
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    /**
     * 按yyyy-MM-dd的格式返回当前日期的字符串
     */
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(date);
    }


    /**
     * 返回指定天数后的按yyyy-MM-dd的 列如2018-10-07
     *
     * @param day
     * @return
     */

    public static String getBackTime(int day) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        sdf.applyPattern("yyyy-MM-dd");
        String s = sdf.format(c.getTime());
        return s;
    }

    /**
     * 获取两个日期之间相距的天数 当前时间减去结束时间
     *
     * @param finishTime  会员结束时间
     * @param currentTime 当前日期
     * @return
     */
    public static long getBetweenDate(String finishTime, String currentTime) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        Date d1 = df.parse(finishTime);
        Date d2 = df.parse(currentTime);

        long day = (d2.getTime() - d1.getTime()) / (60 * 60 * 1000 * 24);

        return day;
    }


    /**
     * 按format的格式返回当前时间的字符串
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        return sdf.format(date);
    }


    /**
     * 返回yyyy-MM-dd HH:mm:ss:SSS格式的当前时间
     */
    public static String getCurrentTimeSSS() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss:SSS");
        return sdf.format(date);
    }

    /**
     * 返回yyyyMMddHHmmss格式的当前时间
     */
    public static String getCurrentTimeS() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     * 根据毫秒millisecond返回对应format格式的时间
     *
     * @param millisecond
     * @param format
     * @return
     */
    public static String getTimeByMillisecond(long millisecond, String format) {
        Calendar can = Calendar.getInstance();
        can.setTimeInMillis(millisecond);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        return sdf.format(can.getTime());
    }

    /**
     * 根据time返回对应的毫秒数
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long getMillisecondByTime(String time, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date data = sdf.parse(time);
            return data.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据time返回现在和当前时间的差返回值天，小时，分钟之一
     *
     * @param time1
     */
    public static String timeDifference(String time1) {
        String time2 = getCurrentTime();
        String format = "yyyy-MM-dd HH:mm:ss";
        long between = DateUtil.getDifferentInMillisecond(time1, time2, format) / 1000;
        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        //System.out.println(""+day+"天"+hour+"小时"+minute+"分");
        String string = null;
        if (day > 0) {
            string = day + "天前";
        } else if (day == 0 && hour > 0) {
            string = hour + "小时前";
        } else if (day == 0 && hour == 0 && minute > 0) {
            string = minute + "分钟前";
        }
        return string;
    }

    /**
     * 根据time返回对应的秒数
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long getSecondByTime(String time, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format);
            Date data = sdf.parse(time);
            return data.getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取时间差的毫秒数  time2-time1
     *
     * @param time1
     * @param time2
     * @param format 两者的日期格式必须都为format
     * @return
     * @throws Exception
     */
    public static long getDifferentInMillisecond(String time1, String time2, String format) {
        try {
            long msTime1 = getMillisecondByTime(time1, format);
            long msTime2 = getMillisecondByTime(time2, format);
            return msTime2 - msTime1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 返回当前时间的毫秒数
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getBetweenDate("2018-9-9", getCurrentTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 前小于后true
     *
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static boolean compare(String start, String end) throws ParseException {
        // 如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时间格式为24小时
        // 将字符串形式的时间转化为Date类型的时间
        Date a = sdf.parse(start);
        Date b = sdf.parse(end);
        if (a.getTime() - b.getTime() < 0) {
            return true;
        } else {
            return false;
        }
    }





}

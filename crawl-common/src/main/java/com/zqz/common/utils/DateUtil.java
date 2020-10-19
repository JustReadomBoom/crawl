package com.zqz.common.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: zqz
 * @Description: 时间日期工具类，可扩展增加
 * @Date: Created in 15:00 2020/8/28
 */
public class DateUtil {

    public static ThreadLocal<SimpleDateFormat> format1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static ThreadLocal<SimpleDateFormat> format2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    public static ThreadLocal<SimpleDateFormat> format3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static ThreadLocal<SimpleDateFormat> format4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    public static ThreadLocal<SimpleDateFormat> format5 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmm");
        }
    };

    public static ThreadLocal<SimpleDateFormat> format6 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日");
        }
    };

    public static ThreadLocal<SimpleDateFormat> format7 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };




    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyy-MM-dd HH:mm:ss格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 15:03 2020/8/28
     */
    public static String getDateFormat1Str(Date date) {
        return format1.get().format(date);
    }

    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyyMMdd格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 15:03 2020/8/28
     */
    public static String getDateFormat2Str(Date date) {
        return format2.get().format(date);
    }

    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyy-MM-dd格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 15:03 2020/8/28
     */
    public static String getDateFormat3Str(Date date) {
        return format3.get().format(date);
    }

    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyyMMddHHmmss格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 15:03 2020/8/28
     */
    public static String getDateFormat4Str(Date date) {
        return format4.get().format(date);
    }

    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyyMMddHHmm格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 15:03 2020/8/28
     */
    public static String getDateFormat5Str(Date date) {
        return format5.get().format(date);
    }

    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyy年MM月dd日格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 19:48 2020/9/10
     */
    public static String getDateFormat6Str(Date date) {
        return format6.get().format(date);
    }

    /**
     * @Author: zqz
     * @Description: 获取指定日期 yyyyMMddHHmmssSSS格式字符串
     * @Param: [date]
     * @Return: java.lang.String
     * @Date: 19:48 2020/9/10
     */
    public static String getDateFormat7Str(Date date) {
        return format7.get().format(date);
    }


    /**
     * @Author: zqz
     * @Description: 获取时间年月日字符串 1-年 2-月 3-日
     * @Param: [type]
     * @Return: java.lang.String
     * @Date: 17:12 2020/9/17
     */
    public static String getDateStrByType(Integer type){
        String result = null;
        if(1 == type){
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            result = String.valueOf(year);
        }else if(2 == type){
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH) + 1;
            result = String.valueOf(month);
        }else if(3 == type){
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            result = String.valueOf(day);
        }
        return result;
    }

}

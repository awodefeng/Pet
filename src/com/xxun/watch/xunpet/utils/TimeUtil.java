/**
 * Creation Date:2015-2-12
 * <p>
 * Copyright
 */
package com.xxun.watch.xunpet.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Description Of The Class<br>
 *
 * @author liutianxiang
 * @version 1.000, 2015-2-12
 *
 */
public class TimeUtil {

    public static String getTimeStampLocal() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(d);
    }

    @SuppressWarnings("deprecation")
    public static String getTimeStampGMT() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        return format.format(d);
    }

    public static String getTimeStampDayLocal() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(d);
    }

    public static String getTimeSlotByHour() {
        String slot = "0";
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("HH");
        int hour = Integer.valueOf(format.format(d));
        if (hour < 12)
            slot = "0";
        else if (hour < 18)
            slot = "1";
        else
            slot = "2";
        return slot;
    }

    /**
     * 获取today和other的日期间隔
     */
    public static int getInterval(String todayStr, String otherDayStr) {

        int interval = 0;

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date today = null;
            today = format.parse(todayStr);
            Date other = format.parse(otherDayStr);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTime(today);
            int currentYear = calendar.get(Calendar.YEAR);
            int currentToday = calendar.get(Calendar.DAY_OF_YEAR);
            int totalDay = calendar.getMaximum(Calendar.DAY_OF_YEAR);   //366
            calendar.setTime(other);
            int otherYear = calendar.get(Calendar.YEAR);
            int otherDay = calendar.get(Calendar.DAY_OF_YEAR);
            int otherTotalDay = calendar.getMaximum(Calendar.DAY_OF_YEAR);

            if (otherYear == currentYear)
                interval = currentToday - otherDay;
            else if ((currentYear - otherYear) == 1)
                interval = currentToday + (otherTotalDay - otherDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return interval;
    }
}

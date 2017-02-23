package com.xiaoantech.electrombile.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by yangxu on 2016/11/16.
 */

public class TimeUtil {

    /**
     * 获取时间字符串，格式为：yyyy-mm-dd,HH:mm:ss
     *
     * @param timestamp long
     *
     * @return dataStr String类的实例
     */
    public static String getMinuteStringFromTimeStamp(long timestamp){
        Date date = new Date(timestamp*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
        return sdf.format(date);
    }
    public static String getDateStringFromTimeStamp(long timestamp){
        Date date = new Date(timestamp*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static long getZeroTimeStamp(){
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
        gc.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
        return gc.getTime().getTime()/1000;
    }

    public static long getCurrentTime(){
        Date date = new Date();
        return date.getTime()/1000;
    }
}

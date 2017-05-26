package com.traffic.wifiapp.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wxy on 16/6/21.
 */
public class MyDateUtils {
    public final static int TIME_DAY_MILLISECOND = 86400000;
    // /
    // 定义时间日期显示格式
    // /
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String TAG = "DateUtils";

    //获取时间戳精确到毫秒
    public static long getTimestamp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
        return now.getTime();
    }

    public static long getTimeMill(String timeStr){
        if (StringUtil.isEmpty(timeStr)){
            timeStr = getPickerTime(new Date());
        }
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT);
        Date date = null;
        try {
            date = fmt.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = getMessageTime();
        if (date != null) {
            time = date.getTime();
        }
        return time;
    }

    //date转为string
    public static String getPickerTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    //string类型转回为date
    public static Date getDateTime(String timeStr) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT);
        Date date = null;
        try {
            date = fmt.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMessageTime(String timeStr) {
        DateFormat fmt = new SimpleDateFormat(TIME_FORMAT);
        Date date = null;
        try {
            date = fmt.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        return format.format(date);
    }

    public static String getDateTimes(long timeMillis) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(TIME_FORMAT);
        return mDateFormat.format(new Date(timeMillis * 1000));
    }

    public static long getMessageTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        return calendar.getTimeInMillis() / 1000;
    }

    //获取当前时间Str
    public static String getNowDateStr() {
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);//设置日期格式
        return df.format(new Date());
    }


 /*   public static String getReplyTime(String timeStr, Context context) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        Date date = null;
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getPastTime(date.getTime(), context, timeStr);
    }
*/
    public static int getNowHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

   /* *//**
     * 得到时间差
     *//*
    public static String getPastTime(long date, Context context, String timeStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long time = (calendar.getTime().getTime() - date);
        L.e(TAG, "现在的时刻-进来的时刻==" + time + "-" + date);
        if ((time >= 0 && time <= 60) || time < 0) {
            return context.getString(R.string.just);//刚刚
        } else if (time > 60 && time < 3600) {
            return String.format(context.getString(R.string.minute), (time / 60) + "");//分钟
        } else if (time >= 3600 && time < 3600 * 24) {
            return String.format(context.getString(R.string.hour), (time / 3600) + "");//小时
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return context.getString(R.string.yesterday) + timeStr.substring(0, 12);//昨天
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return context.getString(R.string.day_before_yesterday) + timeStr.substring(0, 12);//前天
        }
//        else if (time >= 3600 * 72 && time < 3600 * 24 * 30) {
//            return timeStr;
//            return String.format(context.getString(R.string.day), (time / (3600 * 24)) + "");//天
//        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
//            return String.format(context.getString(R.string.month), (time / (3600 * 24 * 30)) + "");//月
//        }
        else {
            return timeStr;
        }
    }*/

    /**
     * 获得两个String型日期之间相差的天数（第2个减第1个）
     * first, String second
     *
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(String first, String second) throws Exception {
        Date d1 = getFormatDateTime(first, DATE_FORMAT);
        Date d2 = getFormatDateTime(second, DATE_FORMAT);
        Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);
        return mils.intValue();
    }

    /**
     * 获得两个Date型日期之间相差的天数（第2个减第1个）
     *
     * @param first, Date second
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(Date first, Date second) throws Exception {
        Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
        Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);
        Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);
        return mils.intValue();
    }

    public static Date getFormatDateTime(String currDate, String format) {
        if (currDate == null) {
            return null;
        }
        SimpleDateFormat dtFormatdB = null;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            return dtFormatdB.parse(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat(TIME_FORMAT);
            try {
                return dtFormatdB.parse(currDate);
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
     * 根据格式得到格式化后的日期
     *
     * @param currDate 要格式化的日期
     * @param format   日期格式，如yyyy-MM-dd
     * @return Date 返回格式化后的日期，格式由参数<code>format</code>
     * 定义，如yyyy-MM-dd，如2006-02-15
     * @see java.text.SimpleDateFormat#parse(java.lang.String)
     */
    public static Date getFormatDate(String currDate, String format) {
        if (currDate == null) {
            return null;
        }
        SimpleDateFormat dtFormatdB = null;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            return dtFormatdB.parse(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat(DATE_FORMAT);
            try {
                return dtFormatdB.parse(currDate);
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static String getFormatDate(Date currDate) {
        return getFormatDate(currDate, DATE_FORMAT);
    }

    /**
     * 根据格式得到格式化后的日期
     *
     * @param currDate 要格式化的日期
     * @param format   日期格式，如yyyy-MM-dd
     * @return String 返回格式化后的日期，格式由参数<code>format</code>
     * 定义，如yyyy-MM-dd，如2006-02-15
     * @see java.text.SimpleDateFormat#format(java.util.Date)
     */
    public static String getFormatDate(java.util.Date currDate, String format) {
        if (currDate == null) {
            return "";
        }
        SimpleDateFormat dtFormatdB = null;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            return dtFormatdB.format(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat(DATE_FORMAT);
            try {
                return dtFormatdB.format(currDate);
            } catch (Exception ex) {
            }
        }
        return null;
    }

    //string类型转回为date
    public static String getDateYear(String timeStr) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        String yearStr = "";
        try {
            date = fmt.parse(timeStr);
            date = fmt.parse(timeStr);
            // 获取日期实例
            Calendar calendar = Calendar.getInstance();
            // 将日历设置为指定的时间
            calendar.setTime(date);
            // 获取年
            int year = calendar.get(Calendar.YEAR);
            yearStr = year + "年";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yearStr;
    }


    public static String getDateMothDay(String timeStr) {
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        String mothDayStr = "";
        try {
            date = fmt.parse(timeStr);
            // 获取日期实例
            Calendar calendar = Calendar.getInstance();
            // 将日历设置为指定的时间
            calendar.setTime(date);
            // 获取年
            int year = calendar.get(Calendar.YEAR);
            // 这里要注意，月份是从0开始。
            int month = calendar.get(Calendar.MONTH);
            if (month > 1) {
                month++;
            }
            if (month <= 1) {
                month = 1;
            }
            // 获取天
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mothDayStr = month + "月" + day + "日";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mothDayStr;
    }
}

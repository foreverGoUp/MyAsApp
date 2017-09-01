package com.kc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {


    /**
     * <p>
     * 将录像时长转化为列表显示模式，比如"01:01:30"
     * </p>
     *
     * @param diffSeconds
     * @return
     * @author hanlifeng 2014-6-16 下午4:01:04
     */
    public static String convToUIDuration(long diffSeconds) {
        long min = diffSeconds / 60;
        String minStr = "";
        long sec = diffSeconds % 60;
        String secStr = "";
        String hStr = "";

        if (min >= 59) {
            long hour = min / 60;
            long temp = min % 60;
            if (hour < 10) {
                if (hour > 0) {
                    hStr = "0" + hour;
                } else {
                    hStr = "00";
                }
            } else {
                hStr = "" + hour;
            }
            if (temp < 10) {
                if (temp > 0) {
                    minStr = "0" + temp;
                } else {
                    minStr = "00";
                }
            } else {
                minStr = "" + temp;
            }
            if (sec < 10) {
                if (sec > 0) {
                    secStr = "0" + sec;
                } else {
                    secStr = "00";
                }
            } else {
                secStr = "" + sec;
            }
            return hStr + ":" + minStr + ":" + secStr;
        } else {
            hStr = "00";
            if (min < 10) {
                if (min > 0) {
                    minStr = "0" + min;
                } else {
                    minStr = "00";
                }
            } else {
                minStr = "" + min;
            }
            if (sec < 10) {
                if (sec > 0) {
                    secStr = "0" + sec;
                } else {
                    secStr = "00";
                }
            } else {
                secStr = "" + sec;
            }
            return hStr + ":" + minStr + ":" + secStr;
        }
    }

    /**
     * <p>
     * 将时间转化为月和日格式，比如 6月17号
     * </p>
     *
     * @param queryDate
     * @return
     * @author hanlifeng 2014-6-17 下午1:34:23
     */
    public static String converToMonthAndDay(Date queryDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(queryDate);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        StringBuffer buffer = new StringBuffer();
        return buffer.append(month).append("月").append(day).append("日").toString();
    }

    private static SimpleDateFormat mFormatMDHM = new SimpleDateFormat("MM月dd日 HH:mm");

    public static String getMDHM(long ms) {
        return mFormatMDHM.format(new Date(ms));
    }

    public static String getYMDHMS(long ms) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return format.format(new Date(ms));
    }


    public static final int FLAG_DAY_BEGIN = 0;
    public static final int FLAG_DAY_END = 1;

    /**
     * 凌晨
     *
     * @param date
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    public static Date getDateBeginOrEnd(Date date, int flag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);

        if (flag == 0) {
            return cal.getTime();
        } else if (flag == 1) {
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
        }
        return cal.getTime();
    }

    private static SimpleDateFormat mFormatY = new SimpleDateFormat("yyyy");

    public static String getYear(long ms) {
        return mFormatY.format(new Date(ms));
    }
}

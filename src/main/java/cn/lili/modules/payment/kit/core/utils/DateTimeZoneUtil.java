package cn.lili.modules.payment.kit.core.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * 时间工具类
 * 依赖 Hutool
 *
 * @author YunGouOS
 */
public class DateTimeZoneUtil implements Serializable {

    private static final long serialVersionUID = -1331008203306650395L;

    /**
     * 时间转 TimeZone
     * <p>
     * 2020-08-17T16:46:37+08:00
     *
     * @param time 时间戳
     * @return {@link String}  TimeZone 格式时间字符串
     * @throws Exception 异常信息
     */
    public static String dateToTimeZone(long time) throws Exception {
        return dateToTimeZone(new Date(time));
    }

    /**
     * 时间转 TimeZone
     * <p>
     * 2020-08-17T16:46:37+08:00
     *
     * @param date {@link Date}
     * @return {@link String} TimeZone 格式时间字符串
     * @throws Exception 异常信息
     */
    public static String dateToTimeZone(Date date) throws Exception {
        String time;
        if (date == null) {
            throw new Exception("date is not null");
        }
        // 使用 Hutool 将 Date 转换为 DateTime，再格式化为带时区的字符串
        DateTime dateTime = DateUtil.date(date);
        time = dateTime.toString(DatePattern.UTC_PATTERN);
        return time;
    }

    /**
     * TimeZone 时间转标准时间
     * <p>
     * 2020-08-17T16:46:37+08:00 to 2020-08-17 16:46:37
     *
     * @param str TimeZone格式时间字符串
     * @return {@link String} 标准时间字符串
     * @throws Exception 异常信息
     */
    public static String timeZoneDateToStr(String str) throws Exception {
        String time;
        if (StrUtil.isBlank(str)) {
            throw new Exception("str is not null");
        }
        // 使用 Hutool 解析带时区的字符串为 DateTime，再格式化为标准时间字符串
        DateTime dateTime = DateUtil.parse(str);
        if (dateTime == null) {
            throw new Exception("str to dateTime fail");
        }
        time = dateTime.toString(DatePattern.NORM_DATETIME_PATTERN);
        return time;
    }


    public static void main(String[] args) throws Exception {
        String timeZone = dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
        String timeZone2 = dateToTimeZone(new Date());
        System.out.println(timeZone + " " + timeZone2);
        String date = timeZoneDateToStr(timeZone);
        System.out.println(date);
    }
}

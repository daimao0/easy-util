package com.easy.util;

import com.easy.convert.Convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 日期工具
 *
 * @author: yanchenyang958@hellobike.com
 * @date: 2024-10-31 14:03
 */
@SuppressWarnings("ALL")
public class DateUtils {

    private DateUtils(){

    }

    /**
     * 加一天
     *
     * @param today  当天
     * @param amount 日期间隔 (1、加一天)（-1、减1天）
     * @return 加一天
     */
    public static LocalDateTime addDays(LocalDateTime today, int amount) {
        return today.plusDays(amount);
    }

    /**
     * 加一小时
     *
     * @param today  当天
     * @param amount 日期间隔 (1、加一小时)（-1、减1小时）
     * @return 加一小时
     */
    public static LocalDateTime addHours(LocalDateTime today, int amount) {
        return today.plusHours(amount);
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static String getCurrentDatetimeStr() {
        return Convert.toDateTimeStr(LocalDateTime.now());
    }

    /**
     * 从给定的日期字符串中提取数字，并构建一个Date对象。
     * 第1，2，3，4个出现的数字当作年，第5，6个出现当作月,第7,8个当作日,第9，10个当作时，第11，12个当作分，第13，14个当作秒
     *
     * @param dateString 输入的日期字符串
     * @return 解析后的Date对象，如果无法解析则返回null
     */
    public static Date toDate(String dateString) {
        StringBuilder year = new StringBuilder();
        StringBuilder month = new StringBuilder();
        StringBuilder day = new StringBuilder();
        StringBuilder hour = new StringBuilder();
        StringBuilder minute = new StringBuilder();
        StringBuilder second = new StringBuilder();
        // 记录已经提取的数字数量
        int count = 0;

        for (char c : dateString.toCharArray()) {
            if (Character.isDigit(c)) {
                count++;
                switch (count) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        year.append(c);
                        break;
                    case 5:
                    case 6:
                        month.append(c);
                        break;
                    case 7:
                    case 8:
                        day.append(c);
                        break;
                    case 9:
                    case 10:
                        hour.append(c);
                        break;
                    case 11:
                    case 12:
                        minute.append(c);
                        break;
                    case 13:
                    case 14:
                        second.append(c);
                        break;
                    default:
                        // 忽略多余的数字
                        break;
                }
            }
        }
        // 构建日期时间字符串
        StringBuilder sb = new StringBuilder();
        if (StrUtils.isNotBlank(year)) {
            sb.append(year);
        }
        if (StrUtils.isNotBlank(month)) {
            sb.append("-").append(month);
        }
        if (StrUtils.isNotBlank(day)) {
            sb.append("-").append(day);
        }
        if (StrUtils.isNotBlank(hour)) {
            sb.append(" ").append(hour);
        }
        if (StrUtils.isNotBlank(minute)) {
            sb.append(":").append(minute);
        }
        if (StrUtils.isNotBlank(second)) {
            sb.append(":").append(second);
        }
        try {
            int length = sb.length();
            SimpleDateFormat sdf;
            switch (length) {
                case 4:
                    sdf = new SimpleDateFormat("yyyy");
                    return sdf.parse(sb.toString());
                case 7:
                    sdf = new SimpleDateFormat("yyyy-MM");
                    return sdf.parse(sb.toString());
                case 10:
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.parse(sb.toString());
                case 13:
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH");
                    return sdf.parse(sb.toString());
                case 16:
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    return sdf.parse(sb.toString());
                case 19:
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return sdf.parse(sb.toString());
                default:
            }
            // 使用 SimpleDateFormat 解析日期时间字符串
        } catch (ParseException e) {
            // 解析失败，返回 null
            return null;
        }
        return null;
    }
}
package com.easy.convert;


import com.easy.constant.StrConstant;
import com.easy.datetime.DatePattern;
import com.easy.util.StrUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author: daimao
 * @date: 2024-11-20 19:36
 */
@SuppressWarnings("ALL")
public class Convert {
    private Convert() {

    }

    /**
     * 转换为字符串
     *
     * @param object 被转换的值
     * @return 结果
     */
    public static String toStr(Object object) {
        if (object == null) {
            return "";
        }
        return object.toString();
    }

    /**
     * 转换为时间字符串，默认格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param object 被转换的值
     * @return 结果
     */
    public static String toDatetimeStr(Object object) {
        return toDateStr(object, DatePattern.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 转换为时间字符串
     *
     * @param object  被转换的值
     * @param pattern 转换的格式
     * @return 结果
     */
    public static String toDateStr(Object object, DatePattern pattern) {
        if (object == null) {
            return StrConstant.BLANK;
        }
        if (object instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) object;
            SimpleDateFormat simpleFormatter = new SimpleDateFormat(pattern.getPattern());
            return simpleFormatter.format(new Date(timestamp.getTime()));
        }
        if (object instanceof Time) {
            Time time = (Time) object;
            SimpleDateFormat simpleFormatter = new SimpleDateFormat(pattern.getPattern());
            return simpleFormatter.format(new Date(time.getTime()));
        }
        if (object instanceof java.sql.Date) {
            java.sql.Date date = (java.sql.Date) object;
            SimpleDateFormat simpleFormatter = new SimpleDateFormat(pattern.getPattern());
            return simpleFormatter.format(date);
        }
        if (object instanceof Date) {
            Date date = (Date) object;
            SimpleDateFormat simpleFormatter = new SimpleDateFormat(pattern.getPattern());
            return simpleFormatter.format(date);
        }
        if (object instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) object;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.getPattern());
            return localDateTime.format(formatter);
        }
        if (object instanceof LocalDate) {
            LocalDate localDate = (LocalDate) object;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.getPattern());
            return localDate.format(formatter);
        }
        if (object instanceof LocalTime) {
            LocalTime localTime = (LocalTime) object;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.getPattern());
            return localTime.format(formatter);
        }
        if (object instanceof OffsetDateTime) {
            OffsetDateTime offsetDateTime = (OffsetDateTime) object;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.getPattern());
            return offsetDateTime.atZoneSimilarLocal(ZoneId.systemDefault()).format(formatter);
        }
        if (object instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) object;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.getPattern());
            return zonedDateTime.format(formatter);
        }
        return object.toString();
    }

    /**
     * 转时间戳
     *
     * @param localDateTime localDateTime
     * @return 时间戳
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Timestamp.from(instant);
    }

    /**
     * 解析字符串,支持多重日期格式
     * 解析失败返回null
     *
     * @param datetimeStr 能支持大部分日期字符格式
     * @return localDateTime
     */
    public static LocalDateTime parse(String datetimeStr) {
        Date date = toDate(datetimeStr);
        if (date == null) {
            return null;
        }
        //实时
        Instant instant = date.toInstant();
        //指定时区
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        //转化为localDateTime
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * 从给定的日期字符串中提取数字，并构建一个Date对象。
     * 第1，2，3，4个出现的数字当作年，第5，6个出现当作月,第7,8个当作日,第9，10个当作时，第11，12个当作分，第13，14个当作秒
     *
     * @param dateString 输入的日期字符串
     * @return 解析后的Date对象，如果无法解析则返回null
     */
    public static Date toDate(String dateString) {
        if (StrUtils.isBlank(dateString)) {
            return null;
        }
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

    /**
     * 将字符串转换为Long类型
     * @param str 字符串
     * @return Long类型
     */
    public static Long toLong(String str) {
        return Long.valueOf(str);
    }

    /**
     * 将字符串转换为Integer类型
     * @param str 字符串
     * @return Integer类型
     */
    public static Integer toInteger(String str) {
        return Integer.valueOf(str);
    }
}

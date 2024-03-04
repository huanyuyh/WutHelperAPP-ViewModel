package com.huanyu.hycnew.utils;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    /**
     * 计算两个日期之间的天数和周数
     * @param dateStr 日期字符串，格式为：yyyy-MM-dd
     * @return 一个包含天数和周数的Map
     */
    public static Map<String, Integer> calculateDaysAndWeeksFromToday(String dateStr) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.parse(dateStr);

        int daysBetween = 0;

        daysBetween = (int)ChronoUnit.DAYS.between(endDate, startDate);
        int weeksBetween = (int) ChronoUnit.WEEKS.between(endDate, startDate);

        Map<String, Integer> result = new HashMap<>();
        result.put("days", daysBetween);
        result.put("weeks", weeksBetween);

        return result;
    }

    /**
     * 在给定的日期上添加指定的天数
     * @param dateStr 日期字符串，格式为：yyyy-MM-dd
     * @param daysToAdd 要添加的天数
     * @return 添加天数后的日期字符串
     */
    public static String addDaysToDate(String dateStr, long daysToAdd) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDate newDate = date.plusDays(daysToAdd);
        return newDate.toString();
    }

    public static String addDaysToDateReturnMonthDay(String dateStr, long daysToAdd) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDate newDate = date.plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return newDate.format(formatter);
    }

    /**
     * 获取当前日期的年、月、日信息
     * @return 一个包含年月日的Map
     */
    public static Map<String, Integer> getCurrentDateInfo() {
        LocalDate today = LocalDate.now();
        Map<String, Integer> dateInfo = new HashMap<>();
        dateInfo.put("year", today.getYear());
        dateInfo.put("month", today.getMonthValue());
        dateInfo.put("day", today.getDayOfMonth());

        return dateInfo;
    }

    // 你可以在这里继续添加更多日期相关的工具方法
    public static String getFormattedDate(int year, int month, int day) {
        String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
        String dayStr = (day < 10) ? "0" + day : String.valueOf(day);
        return year + "-" + monthStr + "-" + dayStr;
    }
    public static Map<String, Integer> calculateDaysAndWeeksFromDay(String startDateStr,String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        int daysBetween = 0;

        daysBetween = (int)ChronoUnit.DAYS.between(startDate,endDate );
        int weeksBetween = (int) ChronoUnit.WEEKS.between(startDate,endDate );

        Map<String, Integer> result = new HashMap<>();
        result.put("days", daysBetween);
        result.put("weeks", weeksBetween);

        return result;
    }

}
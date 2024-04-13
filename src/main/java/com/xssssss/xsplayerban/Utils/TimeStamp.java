package com.xssssss.xsplayerban.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xssssss.xsplayerban.Main.config;
import static com.xssssss.xsplayerban.Main.logger;

public class TimeStamp {
    public static int getByStr(String input) {
        Pattern pattern = Pattern.compile("(\\d+y)?(\\d+d)?(\\d+h)?(\\d+m)?(\\d+s)?");
        Matcher matcher = pattern.matcher(input);
        int totalSeconds = 0;
        while (matcher.find()) {
            // 从匹配结果中提取数字和单位
            String yearStr = matcher.group(1);
            String dayStr = matcher.group(2);
            String hourStr = matcher.group(3);
            String minuteStr = matcher.group(4);
            String secondStr = matcher.group(5);

            // 转换时间单位为秒
            int years = parseUnit(yearStr, 'y', 365 * 24 * 60 * 60);
            int days = parseUnit(dayStr, 'd', 24 * 60 * 60);
            int hours = parseUnit(hourStr, 'h', 60 * 60);
            int minutes = parseUnit(minuteStr, 'm', 60);
            int seconds = parseUnit(secondStr, 's', 1);

            // 累加秒数
            totalSeconds += years + days + hours + minutes + seconds;
        }
        return totalSeconds;
    }
    private static int parseUnit(String str, char unitChar, int multiplier) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        int value = Integer.parseInt(str.substring(0, str.length() - 1));
        return value * multiplier;
    }
    public static int getTimeStampByDate(String dateTimeString){
        SimpleDateFormat sdf = new SimpleDateFormat(config.getString("SimpleDateFormat"));
        try {
            Date date = sdf.parse(dateTimeString);
            long timestamp = date.getTime()/1000L;
            return (int)timestamp;
        } catch (ParseException e) {
            logger.warning("[XsPlayerBan] 日期格式解析失败！请检查指令中的格式是否符合config.yml中SimpleDateFormat设置的值");
            logger.warning("[XsPlayerBan] 已自动返回当前时间！");
            e.printStackTrace();
            return (int)(System.currentTimeMillis()/1000L);
        }
    }
    public static String getDateByTimeStamp(int timeStamp){
        Date date = new Date((long) timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(config.getString("SimpleDateFormat"));
        return sdf.format(date);
    }
}

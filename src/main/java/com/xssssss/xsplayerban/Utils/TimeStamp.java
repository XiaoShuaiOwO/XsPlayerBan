package com.xssssss.xsplayerban.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xssssss.xsplayerban.Main.config;
import static com.xssssss.xsplayerban.Main.logger;

public class TimeStamp {
    public static Long getByStr(String input) {
        Pattern pattern = Pattern.compile("(\\d+y)?(\\d+d)?(\\d+h)?(\\d+m)?(\\d+s)?");
        Matcher matcher = pattern.matcher(input);
        Long totalSeconds = 0L;
        while (matcher.find()) {
            // 从匹配结果中提取数字和单位
            String yearStr = matcher.group(1);
            String dayStr = matcher.group(2);
            String hourStr = matcher.group(3);
            String minuteStr = matcher.group(4);
            String secondStr = matcher.group(5);

            // 转换时间单位为秒
            Long years = parseUnit(yearStr, 'y', 365L * 24 * 60 * 60);
            Long days = parseUnit(dayStr, 'd', 24L * 60 * 60);
            Long hours = parseUnit(hourStr, 'h', 60L * 60);
            Long minutes = parseUnit(minuteStr, 'm', 60L);
            Long seconds = parseUnit(secondStr, 's', 1L);

            // 累加秒数
            totalSeconds += years + days + hours + minutes + seconds;
        }
        return totalSeconds;
    }
    private static Long parseUnit(String str, char unitChar, Long multiplier) {
        if (str == null || str.isEmpty()) {
            return 0L;
        }
        Long value = Long.valueOf(str.substring(0, str.length() - 1));
        return value * multiplier;
    }
    public static Long getTimeStampByDate(String dateTimeString){
        SimpleDateFormat sdf = new SimpleDateFormat(config.getString("SimpleDateFormat"));
        try {
            Date date = sdf.parse(dateTimeString);
            long timestamp = date.getTime()/1000L;
            return timestamp;
        } catch (ParseException e) {
            logger.warning("[XsPlayerBan] 日期格式解析失败！请检查指令中的格式是否符合config.yml中SimpleDateFormat设置的值");
            logger.warning("[XsPlayerBan] 已自动返回当前时间！");
            e.printStackTrace();
            return (System.currentTimeMillis()/1000L);
        }
    }
    public static String getDateByTimeStamp(Long timeStamp){
        Date date = new Date((long) timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(config.getString("SimpleDateFormat"));
        return sdf.format(date);
    }
}

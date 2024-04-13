package com.xssssss.xsplayerban.Utils;

import com.xssssss.xsplayerban.EventRecord.EventRecord;
import com.xssssss.xsplayerban.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.xssssss.xsplayerban.Main.logger;
import static com.xssssss.xsplayerban.Main.message;

public class Message {
    public static String getBanMessage(String PlayerName,Long expiryTime, String MsgType, String Reason, String BanID, String Enforcer){
        String outMsg;
        //System.out.println("获取到expiryTime的值为："+expiryTime);
        if (expiryTime < 0) {
            List<String> MsgList = getMsgListInMessage("PermBan",MsgType);
            outMsg = messageBuild(MsgList);
        }else {
            //临时封禁
            List<String> MsgList = getMsgListInMessage("TempBan",MsgType);
            outMsg = messageBuild(MsgList);
        }
        return repMsg(outMsg,PlayerName,expiryTime,Reason,BanID,Enforcer);
        //return outMsg;
    }
    private static List<String> getMsgListInMessage(String BanType, String MsgType) {
        List<String> Msg = message.getStringList(BanType+"."+MsgType);
        if (!(Msg == null || Msg.isEmpty())) {
            return Msg;
        }else {
            Msg = message.getStringList(BanType+"."+"default");
            if (!(Msg == null|| Msg.isEmpty())) {
                logger.warning("[XsPlayerBan] 无法获取到 "+BanType+"."+MsgType+" ,请检查messages.yml！已更换为使用 "+BanType+".default ！");
                return Msg;
            }else{
                //BanType.default也不存在，设置BanType.default为插件中的默认值
                Msg = getDefaultBanMessage(BanType);
                logger.warning("[XsPlayerBan] 无法获取到 "+BanType+"."+MsgType+" ,并且无法获取到 "+BanType+".default"+" ，请检查messages.yml！已更换为使用 "+BanType+".default ！");
                return Msg;
            }
        }

    }
    private static String repMsg(String msg, String PlayerName, Long expiryTime, String Reason, String ID, String Enforcer){
        //更换变量,颜色代码
        msg = msg.replaceAll("&","§");
        msg = msg.replaceAll("(?i)\\{Player}",PlayerName);
        msg = msg.replaceAll("(?i)\\{Reason}",Reason);
        msg = msg.replaceAll("(?i)\\{ID}",ID);
        msg = msg.replaceAll("(?i)\\{Enforcer}",Enforcer);
        msg = msg.replaceAll("(?i)\\{expiryTime}",toTimeStr(expiryTime));
        return msg;
    }
    private static List<String> getDefaultBanMessage(String BanType) {
        // 加载插件中的默认消息文件并获取其中的默认消息
        InputStream stream = Main.plugin.getResource("message.yml");
        if (stream != null) {
            YamlConfiguration defaultMessage = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            List<String> defaultMessageList = defaultMessage.getStringList(BanType+".default");
            message.set(BanType+".default",defaultMessageList);
            logger.warning("[XsPlayerBan] 已自动生成默认 "+BanType+".default !");
            return defaultMessageList;
        }
        return null;
    }
    private static String messageBuild(List<String> messageList) {
        StringBuilder messageBuilder = new StringBuilder();
        for (String message : messageList) {
            messageBuilder.append(message).append("\n");
        }
        return messageBuilder.toString();
    }
    public static String toTimeStr(long time) {
        //时间为-1
        if (time < 0) {
            return "null";
        }
        //将时间错转为正常日期(输入的还是以秒为单位的时间戳)
        long timestamp = time*1000;
        String year = message.getString("TimeFormat.year");
        String month = message.getString("TimeFormat.month");
        String day = message.getString("TimeFormat.day");
        String hour = message.getString("TimeFormat.hour");
        String minute = message.getString("TimeFormat.minute");
        String second = message.getString("TimeFormat.second");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+year+"MM"+month+"dd"+day+"HH"+hour+"mm"+minute+"ss"+second);
        String date = sdf.format(new Date(timestamp));
        return date;
    }
    public enum TimeAccuracy {
        ALL,YEAR,MONTH,DAY,HOUR,MINUTE,SECOND
    }
    public static String toTimeStr(long time, TimeAccuracy type) {
        //时间为-1
        if (time < 0) {
            return "null";
        }
        //将时间错转为正常日期(输入的还是以秒为单位的时间戳)
        long timestamp = time*1000;
        String year = message.getString("TimeFormat.year");
        String month = message.getString("TimeFormat.month");
        String day = message.getString("TimeFormat.day");
        String hour = message.getString("TimeFormat.hour");
        String minute = message.getString("TimeFormat.minute");
        String second = message.getString("TimeFormat.second");
        SimpleDateFormat sdf = null;
        switch (type) {
            case ALL:
                return toTimeStr(time);
            case YEAR:
                sdf = new SimpleDateFormat("yyyy"+year);
                break;
            case MONTH:
                sdf = new SimpleDateFormat("yyyy"+year+"MM"+month);
                break;
            case DAY:
                sdf = new SimpleDateFormat("yyyy"+year+"MM"+month+"dd"+day);
                break;
            case HOUR:
                sdf = new SimpleDateFormat("yyyy"+year+"MM"+month+"dd"+day+"HH"+hour);
                break;
            case MINUTE:
                sdf = new SimpleDateFormat("yyyy"+year+"MM"+month+"dd"+day+"HH"+hour+"mm"+minute);
                break;
            case SECOND:
                sdf = new SimpleDateFormat("yyyy"+year+"MM"+month+"dd"+day+"HH"+hour+"mm"+minute+"ss"+second);
                break;
        }
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    public static String getBroadcastMessage(String Type,String model, String playerName, String Reason, String ID, String Enforcer, String expiryTime){
        String broMsg;
        String msg = message.getString(Type+"Broadcast."+model);
        if (!(msg == null || msg.isEmpty())) {
            broMsg = repMsg(msg,playerName,Long.valueOf(expiryTime),Reason,ID,Enforcer);
        }else{
            msg = message.getString(Type+"Broadcast.default");
            if (!(msg == null || msg.isEmpty())){
                broMsg = repMsg(msg,playerName,Long.valueOf(expiryTime),Reason,ID,Enforcer);
                logger.warning("[XsPlayerBan] 无法获取到 "+Type+"Broadcast."+model+" ,请检查messages.yml！已更换为使用 "+Type+"Broadcast.default ！");
            }else {
                msg = getDefaultBroadcastMessage(Type);
                broMsg = repMsg(msg,playerName,Long.valueOf(expiryTime),Reason,ID,Enforcer);
                logger.warning("[XsPlayerBan] 无法获取到 "+Type+"Broadcast."+model+" ,并且无法获取到 "+Type+"Broadcast.default"+" ，请检查messages.yml！已更换为使用 "+Type+"Broadcast.default ！");
            }
        }
        return broMsg;
    }
    private static String getDefaultBroadcastMessage(String BroType) {
        // 加载插件中的默认消息文件并获取其中的默认消息
        InputStream stream = Main.plugin.getResource("message.yml");
        if (stream != null) {
            YamlConfiguration defaultMessage = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            String msg = defaultMessage.getString(BroType+"Broadcast.default");
            message.set(BroType+"Broadcast.default",defaultMessage);
            logger.warning("[XsPlayerBan] 已自动生成默认 "+BroType+"Broadcast.default !");
            return msg;
        }
        return null;
    }

    public static void sendJSONMessage(CommandSender player, String message, String part, String changeMsg, String showMsg, String cmd, boolean useCmd) {
        if (message.contains(part)) {
            TextComponent all = new TextComponent("");
            boolean first = true;
            while (message.contains(part)) {
                if (first) {
                    try {
                        all.addExtra(new TextComponent(message.split(part)[0].replaceAll("&","§")));
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
                TextComponent parts = new TextComponent(new TextComponent(changeMsg.replaceAll("&","§")));
                parts.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(showMsg.replaceAll("&","§")).create()));
                if (useCmd) {
                    parts.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
                }else {
                    parts.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
                }
                all.addExtra(parts);
                try {
                    all.addExtra(new TextComponent(message.split(part)[1].replaceAll("&","§")));
                } catch (ArrayIndexOutOfBoundsException ignored) {}
                first = false;
                message = message.replaceFirst(part, "");
            }
            if (player == null) {
                Bukkit.spigot().broadcast(all);
            } else {
                player.spigot().sendMessage(all);
            }
        } else {
            player.sendMessage(message.replaceAll("&","§"));
        }
    }
}

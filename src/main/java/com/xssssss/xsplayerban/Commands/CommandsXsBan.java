package com.xssssss.xsplayerban.Commands;

import com.xssssss.xsplayerban.EventRecord.EventRecord;
import com.xssssss.xsplayerban.Main;
import com.xssssss.xsplayerban.Utils.Message;
import com.xssssss.xsplayerban.Utils.TimeStamp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xssssss.xsplayerban.Main.*;
import static com.xssssss.xsplayerban.Utils.FlagValue.getFlagBoolValue;
import static com.xssssss.xsplayerban.Utils.FlagValue.getFlagStrValue;
import static com.xssssss.xsplayerban.Utils.Message.getBroadcastMessage;
import static com.xssssss.xsplayerban.Utils.Message.toTimeStr;
import static com.xssssss.xsplayerban.Utils.PlayerBan.doPlayerBan;
import static com.xssssss.xsplayerban.Utils.PlayerUUID.getPlayerUUIDByName;
import static com.xssssss.xsplayerban.Utils.TimeStamp.getDateByTimeStamp;
import static com.xssssss.xsplayerban.Utils.TimeStamp.getTimeStampByDate;

public class CommandsXsBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (!sender.hasPermission("XsPlayerBan.ban")) {
            sender.sendMessage("§7§l=========== §3§XsPlayerBan §7§l===========");
            sender.sendMessage("§3§lBy: XiaoShuaiOwO");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§7§l=========== §3§lXsPlayerBan §7§l===========");
            sender.sendMessage("§3§lBy: XiaoShuaiOwO");
            sender.sendMessage(" ");
            sender.sendMessage("§e命令列表:");
            sender.sendMessage("§e/XsBan <player> [st:startTime] [t:time] [m:model] [r:reason] [b:true/false] [k:true/false] §f- §7封禁玩家");
            sender.sendMessage("§7* 可使用简拼/ban代替/XsBan");
            sender.sendMessage("§7* 封禁离线玩家时请注意大小写！");
            sender.sendMessage("§7* 当reason中包含空格时，请使用/_代替");
            sender.sendMessage("§e/XsPlayerBan §f- §7查看插件其他指令");
            return true;
        }
        if (args.length >= 1) {
            //默认值
            String Type = "Ban";
            String playerName = args[0];
            String Enforcer = sender.getName();
            String startTime = Long.toString(System.currentTimeMillis()/1000L);
            String time = "perm";
            String model = "default";
            String reason = "null";
            String BanExpiry = "-1";
            boolean broadcast = true;
            boolean kick = true;
            //查看用户是否有绕过权限

            /*try {
                if (getPlayerByName(args[0]).hasPermission("XsPlayerBan.bypass")) {
                    sender.sendMessage("§3§l[XsPlayerBan] §c你不能封禁该用户，因为对方拥有绕过权限！");
                    return true;
                }
            }catch (NullPointerException e){

            }*/

            //查询用户是否还在封禁中
            Player player = Bukkit.getPlayer(args[0]);
            UUID uuid;
            if (player != null) {
                uuid = player.getUniqueId();
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                uuid = offlinePlayer.getUniqueId();
            }
            if ((!(data.getString("PlayerList."+uuid+".BanExpiry") == null)) && config.getBoolean("Setting.checkBanned")) {
                int expiryTime = Integer.parseInt(Main.data.getString("PlayerList."+uuid+".BanExpiry"));
                //System.out.println("data中获取到的expiryTime: "+expiryTime);
                //System.out.println("System.currentTimeMillis()/1000L: "+System.currentTimeMillis()/1000L);
                if ((System.currentTimeMillis()/1000L) < expiryTime || expiryTime < 0){
                    sender.sendMessage("§3§l[XsPlayerBan] §c该用户正在封禁中...请先取消封禁再执行该操作！");
                    return true;
                }
            }
            //设置参数
            List<String> flagList = Arrays.asList(args);
            for (String flag : flagList) {
                time = getFlagStrValue(flag,"t:","time:",time);
                startTime = Integer.toString(getTimeStampByDate(getFlagStrValue(flag,"st:","startTime:",getDateByTimeStamp(Integer.parseInt(startTime)))));
                model = getFlagStrValue(flag,"m:","model:",model);
                reason = getFlagStrValue(flag,"r:","reason:",reason)
                        .replaceAll("/_"," ")
                        .replaceAll("&","§");
                broadcast = getFlagBoolValue(flag,"r:","reason:",broadcast);
                kick = getFlagBoolValue(flag,"k:","kick:",kick);
            }
            //设置data.yml
            if (time.equalsIgnoreCase("perm")) {
                BanExpiry = "-1";
            }else{
                BanExpiry = Integer.toString(Integer.parseInt(startTime) + TimeStamp.getByStr(time));
            }
            //执行封禁操作
            int nowTime = (int)(System.currentTimeMillis()/1000L);
            String ID = doPlayerBan(playerName,startTime,time,BanExpiry,model,reason,Enforcer,nowTime,broadcast,kick);
            sender.sendMessage("§3§l[XsPlayerBan] §a成功封禁玩家 §e"+playerName+"§a ，详细信息如下：");
            EventRecord eventRecord = new EventRecord().getRecord(ID);
            //System.out.println("输出详情前获取到的eventRecord对象"+eventRecord);
            //sender.sendMessage(EventRecord.getBanInfo(eventRecord));
            String message = EventRecord.getBanInfo(eventRecord);
            String doCmd = "/XsPlayerBan remove "+ID;
            Message.sendJSONMessage(sender,message,"删除记录","§8[§4删除记录§8]","§7点击删除记录§e "+ID,doCmd,false);

            return true;
        }
        return false;
    }





}

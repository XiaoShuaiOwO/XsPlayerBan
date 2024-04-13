package com.xssssss.xsplayerban.Utils;

import com.xssssss.xsplayerban.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.xssssss.xsplayerban.Utils.Message.getBroadcastMessage;
import static com.xssssss.xsplayerban.Utils.PlayerUUID.getPlayerUUIDByName;

public class PlayerBan {
    public static String doPlayerBan(String playerName, String startTime, String time, String BanExpiry, String model, String reason, String Enforcer, int nowTime, boolean broadcast, boolean kick){
        //设置data.yml
        Player player = Bukkit.getPlayer(playerName);
        UUID uuid;
        if (player != null) {
            uuid = player.getUniqueId();
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            uuid = offlinePlayer.getUniqueId();
        }
        String path = "PlayerList."+uuid+".";
        Main.data.set(path+"Name",playerName);
        Main.data.set(path+"BanStart",startTime);
        Main.data.set(path+"BanExpiry",BanExpiry);
        Main.data.set(path+"BanTemplate",model);
        Main.data.set(path+"Reason",reason);
        Main.data.set(path+"Enforcer",Enforcer);
        Main.data.set(path+"ID","tempID");
        Main.data.set(path+"History."+nowTime+".Type","Ban");
        Main.data.set(path+"History."+nowTime+".BanDuration",time);
        Main.data.set(path+"History."+nowTime+".Name",playerName);
        Main.data.set(path+"History."+nowTime+".UUID",getPlayerUUIDByName(playerName).toString());
        Main.data.set(path+"History."+nowTime+".BanStart",startTime);
        Main.data.set(path+"History."+nowTime+".BanExpiry",BanExpiry);
        Main.data.set(path+"History."+nowTime+".BanTemplate",model);
        Main.data.set(path+"History."+nowTime+".Reason",reason);
        Main.data.set(path+"History."+nowTime+".Enforcer",Enforcer);
        //生成BanID
        String tempID = ID.createID("Ban");
        Main.data.set(path+"ID",tempID);
        Main.data.set(path+"History."+nowTime+".ID",tempID);
        Main.saveData();
        //是否发送公告
        if (broadcast) {
            if (time.equalsIgnoreCase("perm")) {
                Bukkit.broadcastMessage(getBroadcastMessage("PermBan",model,playerName,reason,tempID,Enforcer,BanExpiry));
            }else {
                Bukkit.broadcastMessage(getBroadcastMessage("TempBan",model,playerName,reason,tempID,Enforcer,BanExpiry));
            }
        }
        //是否踢出
        if (kick && player != null) {
            player.kickPlayer(Message.getBanMessage(playerName,Integer.valueOf(BanExpiry),model,reason,tempID,Enforcer));
        }
        return tempID;
    }
}

package com.xssssss.xsplayerban.Commands;

import com.xssssss.xsplayerban.Main;
import com.xssssss.xsplayerban.Utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

import static com.xssssss.xsplayerban.Utils.FlagValue.getFlagBoolValue;
import static com.xssssss.xsplayerban.Utils.FlagValue.getFlagStrValue;
import static com.xssssss.xsplayerban.Utils.Message.getBroadcastMessage;
import static com.xssssss.xsplayerban.Utils.PlayerUUID.getPlayerUUIDByName;

public class CommandsXsUnBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (!sender.hasPermission("XsPlayerBan.unban")) {
            sender.sendMessage("§7§l=========== §3§XsPlayerBan §7§l===========");
            sender.sendMessage("§3§lBy: XiaoShuaiOwO");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§7§l=========== §3§lXsPlayerBan §7§l===========");
            sender.sendMessage("§3§lBy: XiaoShuaiOwO");
            sender.sendMessage(" ");
            sender.sendMessage("§e命令列表:");
            sender.sendMessage("§e/XsUnBan <player> [m:model] [r:reason] [b:true/false] §f- §7取消封禁玩家");
            sender.sendMessage("§7* 可使用简拼/unban代替/XsUnBan");
            sender.sendMessage("§7* 可同时连接多个玩家ID，以解封多位玩家");
            sender.sendMessage("§e/XsPlayerBan §f- §7查看插件其他指令");
            return true;
        }
        if (args.length > 0) {
            //默认值
            String Enforcer = sender.getName();
            boolean broadcast = true;
            String model = "default";
            String reason = "null";

            //重新赋值
            for (String flag : args) {
                broadcast = getFlagBoolValue(flag,"b:","broadcast:",broadcast);
                model = getFlagStrValue(flag,"m:","model:",model);
                reason = getFlagStrValue(flag,"r:","reason:",reason);
            }
            for (String flag : args) {
                if (!(flag.startsWith("b:") || flag.startsWith("broadcast:"))) {
                    String playerName = flag;
                    UUID uuid = getPlayerUUIDByName(playerName);
                    int oldBanExpiry = Integer.parseInt(Main.data.getString("PlayerList."+uuid+".BanExpiry"));
                    if (oldBanExpiry != 0 && (System.currentTimeMillis() / 1000) < oldBanExpiry) {
                        Main.data.set("PlayerList."+uuid+".BanExpiry",0);
                        sender.sendMessage("§3§l[XsPlayerBan] §a已成功解封玩家: §e"+playerName);
                        if (broadcast) {
                            Bukkit.broadcastMessage(getBroadcastMessage("UnBan",model,playerName,reason,"null",Enforcer,"null"));
                        }
                    }else {
                        sender.sendMessage("§3§l[XsPlayerBan] §c玩家§e "+playerName+" §c解封失败，该玩家当前未处于封禁状态！");
                    }
                }
            }
            return true;
        }
        return false;
    }
}

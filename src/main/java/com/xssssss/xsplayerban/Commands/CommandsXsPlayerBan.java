package com.xssssss.xsplayerban.Commands;

import com.xssssss.xsplayerban.EventRecord.EventRecord;
import com.xssssss.xsplayerban.Utils.EventRecordLoader;
import com.xssssss.xsplayerban.Utils.Message;
import com.xssssss.xsplayerban.Utils.PlayerUUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.beans.EventHandler;
import java.util.List;
import java.util.UUID;

public class CommandsXsPlayerBan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length == 0) {
            if (sender.hasPermission("XsPlayerBan.help")) {
                sender.sendMessage("§7§l=========== §3§lXsPlayerBan §7§l===========");
                sender.sendMessage("§3§lBy: XiaoShuaiOwO");
                sender.sendMessage(" ");
                sender.sendMessage("§e命令列表:");
                sender.sendMessage("§e/XsBan §f- §7查看封禁玩家指令帮助");
                sender.sendMessage("§e/XsUnBan §f- §7查看取消封禁玩家指令帮助");
                sender.sendMessage("§e/XsPlayerBan help §f- §7查看插件全部指令");
                sender.sendMessage("§e/XsPlayerBan history <player> §f- §7查看玩家处罚历史记录");
                sender.sendMessage("§e/XsPlayerBan info <ID> §f- §7查看编号ID的详细信息");
                sender.sendMessage("§e/XsPlayerBan remove <ID> §f- §7从历史记录中删除指定记录(不会解封)");
                sender.sendMessage("§e/XsPlayerBan last <number> §f- §7查看最近的n条记录");
                sender.sendMessage("§7* 可使用简拼/xspb代替/XsPlayerBan");
                return true;
            }else {
                sender.sendMessage("§7§l=========== §3§lXsPlayerBan §7§l===========");
                sender.sendMessage("§3§lBy: XiaoShuaiOwO");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("XsPlayerBan.help")) {
                sender.sendMessage("§7§l=========== §3§lXsPlayerBan §7§l===========");
                sender.sendMessage("§3§lBy: XiaoShuaiOwO");
                sender.sendMessage(" ");
                sender.sendMessage("§e命令列表:");
                sender.sendMessage("§e/XsBan §f- §7查看封禁玩家指令帮助");
                sender.sendMessage("§e/XsUnBan §f- §7查看取消封禁玩家指令帮助");
                sender.sendMessage("§e/XsPlayerBan help §f- §7查看插件全部指令");
                sender.sendMessage("§e/XsPlayerBan history <player> §f- §7查看玩家处罚历史记录");
                sender.sendMessage("§e/XsPlayerBan info <ID> §f- §7查看编号ID的详细信息");
                sender.sendMessage("§e/XsPlayerBan remove <ID> §f- §7从历史记录中删除指定记录(不会解封)");
                sender.sendMessage("§e/XsPlayerBan last <number> §f- §7查看最近的n条记录");
                sender.sendMessage("§7* 可使用简拼/xspb代替/XsPlayerBan");
                return true;
            }else {
                sender.sendMessage("§7§l=========== §3§lXsPlayerBan §7§l===========");
                sender.sendMessage("§3§lBy: XiaoShuaiOwO");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (sender.hasPermission("XsPlayerBan.info")) {
                if (args.length >= 2) {
                    String id = args[1];
                    try {
                        EventRecord eventRecord = new EventRecord().getRecord(id);
                        String message = EventRecord.getBanInfo(eventRecord);
                        String doCmd = "/XsPlayerBan remove "+id;
                        Message.sendJSONMessage(sender,message,"删除记录","§8[§4删除记录§8]","§7点击删除记录§e "+id,doCmd,false);
                        return true;
                    } catch (NullPointerException e) {
                        sender.sendMessage("§3§l[XsPlayerBan] §c未查询到ID为§e "+id+" §c的记录！");
                        return true;
                    }
                }else {
                    sender.sendMessage("§3§l[XsPlayerBan] §c缺少参数！");
                    sender.sendMessage("§3§l[XsPlayerBan] §e/XsPlayerBan info <ID> §f- §7查看编号ID的详细信息");
                    return true;
                }
            }else {
                sender.sendMessage("§3§l[XsPlayerBan] §c你没有权限执行此命令！");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("history")) {
            if (sender.hasPermission("XsPlayerBan.history")) {
                if (args.length >= 2) {
                    String playerName = args[1];
                    UUID playerUUID = PlayerUUID.getPlayerUUIDByName(args[1]);
                    try {
                        List<EventRecord> eventRecords = new EventRecord().getRecords(playerUUID);
                        //System.out.println(eventRecords);
                        //System.out.println(eventRecords.size());
                        if(eventRecords.size() != 0) {
                            sender.sendMessage("§3§l[XsPlayerBan] §a玩家§e "+playerName+" §a的历史记录: ");
                            int i = 0;
                            for (EventRecord eventRecord : eventRecords) {
                                i++;
                                String msg = "§e"+i+". "+EventRecord.getSimpleBanInfo(eventRecord)+" 事件编号";
                                String doCmd = "/XsPlayerBan info "+eventRecord.getID();
                                Message.sendJSONMessage(sender,msg,"事件编号","§f[§c"+eventRecord.getID()+"§f]",EventRecord.getBanInfo(eventRecord).replaceAll("§3§l\\[XsPlayerBan] ","").replaceAll("删除记录","")+"\n§7点击输出到聊天栏！",doCmd,true);
                            }
                            return true;
                        }else {
                            sender.sendMessage("§3§l[XsPlayerBan] §a玩家§e "+playerName+" §a未找到历史记录！");
                            return true;
                        }
                    } catch (NullPointerException e) {
                        sender.sendMessage("§3§l[XsPlayerBan] §a玩家§e "+playerName+" §a未找到历史记录！");
                        return true;
                    }
                }else {
                    sender.sendMessage("§3§l[XsPlayerBan] §c缺少参数！");
                    sender.sendMessage("§3§l[XsPlayerBan] §e/XsPlayerBan history <player> §f- §7查看玩家处罚历史记录");
                    return true;
                }
            }else {
                sender.sendMessage("§3§l[XsPlayerBan] §c你没有权限执行此命令！");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("last")) {
            if (sender.hasPermission("XsPlayerBan.last")) {
                if (args.length >= 2) {
                    int allRecordsInt = EventRecordLoader.getAllEventRecords().size();
                    if (allRecordsInt != 0) {
                        int number = Integer.parseInt(args[1]);
                        EventRecord[] records = EventRecord.getLastRecords(number);
                        sender.sendMessage("§3§l[XsPlayerBan] §a最近的§e "+number+" §a条记录: ");
                        for (int i = 0; i < records.length; i++) {
                            String msg = "§e"+(i+1)+". §e玩家: "+records[i].getPlayerName()+" "+EventRecord.getSimpleBanInfo(records[i])+" 事件编号";
                            String doCmd = "/XsPlayerBan info "+records[i].getID();
                            Message.sendJSONMessage(sender,msg,"事件编号","§f[§c"+records[i].getID()+"§f]",EventRecord.getBanInfo(records[i]).replaceAll("§3§l\\[XsPlayerBan] ","")+"\n§7点击输出到聊天栏！",doCmd,true);
                        }
                        return true;
                    }else {
                        sender.sendMessage("§3§l[XsPlayerBan] §a未查询到历史记录！");
                        return true;
                    }

                }else {
                    sender.sendMessage("§3§l[XsPlayerBan] §c缺少参数！");
                    sender.sendMessage("§3§l[XsPlayerBan] §e/XsPlayerBan last <number> §f- §7查看最近的n条记录");
                    return true;
                }
            }else {
                sender.sendMessage("§3§l[XsPlayerBan] §c你没有权限执行此命令！");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (sender.hasPermission("XsPlayerBan.remove")) {
                if (args.length >= 2) {
                    String removeID = args[1];
                    EventRecord eventRecord = EventRecord.getRecord(removeID);
                    if (eventRecord != null) {
                        if (EventRecord.removeRecord(removeID)) {
                            sender.sendMessage("§3§l[XsPlayerBan] §a成功移除ID为§e "+removeID+" §a的记录！");
                            return true;
                        }else {
                            sender.sendMessage("§3§l[XsPlayerBan] §c移除失败！");
                            return true;
                        }
                    }else {
                        sender.sendMessage("§3§l[XsPlayerBan] §c未查询到ID为§e "+removeID+" §c的记录！");
                        return true;
                    }
                }else {
                    sender.sendMessage("§3§l[XsPlayerBan] §c缺少参数！");
                    sender.sendMessage("§3§l[XsPlayerBan] §e/XsPlayerBan remove <ID> §f- §7从历史记录中删除指定记录(不会解封)");
                    return true;
                }
            }else {
                sender.sendMessage("§3§l[XsPlayerBan] §c你没有权限执行此命令！");
                return true;
            }
        }
        return false;
    }
}

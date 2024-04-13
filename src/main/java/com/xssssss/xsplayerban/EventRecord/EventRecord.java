package com.xssssss.xsplayerban.EventRecord;

import com.xssssss.xsplayerban.Main;
import com.xssssss.xsplayerban.Utils.EventRecordLoader;
import com.xssssss.xsplayerban.Utils.Message;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.xssssss.xsplayerban.Main.plugin;
import static com.xssssss.xsplayerban.Utils.Message.toTimeStr;

public class EventRecord {
    private String ID;
    private String playerName;
    private String playerUUID;
    private Long BanStart;
    private String BanDuration;
    private Long BanExpiry;
    private String model;
    private String reason;
    private String Enforcer;
    private Long RecordTime;
    // 构造方法
    public EventRecord(String ID, String playerName, String playerUUID, Long BanStart, String BanDuration, Long BanExpiry,
                       String model, String reason, String Enforcer, Long RecordTime) {
        this.ID = ID;
        this.playerName = playerName;
        this.playerUUID = playerUUID;
        this.BanStart = BanStart;
        this.BanDuration = BanDuration;
        this.BanExpiry = BanExpiry;
        this.model = model;
        this.reason = reason;
        this.Enforcer = Enforcer;
        this.RecordTime = RecordTime;
    }
    public  EventRecord () {

    }
    // 获取记录
    public static EventRecord getRecord(String ID) {
        List<EventRecord> records = EventRecordLoader.getAllEventRecords();
        //System.out.println("[getRecord(String ID)] records是: "+records);
        try {
            for (EventRecord record : records) {
                //System.out.println("遍历了一个record，它的ID是："+record.getID());
                if (record.getID().equals(ID)) {
                    return record;
                }
            }
        }catch (NullPointerException e) {
            return null;
        }
        return null;
    }
    public static List<EventRecord> getRecords(UUID uuid) {
        List<EventRecord> records = EventRecordLoader.getAllEventRecords();
        List<EventRecord> compliantRecords = new ArrayList<>();
        try {
            for (EventRecord record : records) {
                if (record.getPlayerUUID().equals(uuid.toString())) {
                    compliantRecords.add(record);
                }
            }
            return compliantRecords;
        }catch (NullPointerException e) {
            return null;
        }
    }
    //删除记录
    public static boolean removeRecord(String ID) {
        EventRecord eventRecord = EventRecord.getRecord(ID);
        if (eventRecord == null) {return false;}
        String playerUUID = eventRecord.getPlayerUUID();
        String recordTime = eventRecord.getRecordTime().toString();
        //设置data.yml
        try {
            ConfigurationSection playerListSection = Main.data.getConfigurationSection("PlayerList");
            if (playerListSection == null) {
                return false;
            }
            ConfigurationSection historySection = playerListSection.getConfigurationSection(playerUUID + ".History");
            if (historySection == null) {
                return false;
            }
            //移除UsedID
            List<String> usedIDs = Main.data.getStringList("UsedID");
            if (usedIDs.contains(ID)) {
                usedIDs.remove(ID);
                Main.data.set("UsedID",usedIDs);
                Main.saveData();
            }
            //移除history
            historySection.set(recordTime, null);
            if (historySection.getKeys(false).size() == 0) {
                // 如果移除子键后，该节点下没有其他子键了，就连同该节点一起移除
                playerListSection.set(playerUUID + ".History", null);
            }
            Main.data.save(new File(plugin.getDataFolder(), "data.yml"));
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    //获取最后n条记录
    public static EventRecord[] getLastRecords(int number) {
        List<EventRecord> allRecordsList = EventRecordLoader.getAllEventRecords();
        EventRecord[] allRecords = allRecordsList.toArray(new EventRecord[0]);
        if (number > allRecords.length) {number = allRecords.length;}
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < allRecords.length - 1 - i; j++) {
                if (allRecords[j].getRecordTime() > allRecords[j+1].getRecordTime()) {
                    EventRecord temp = allRecords[j+1];
                    allRecords[j+1] = allRecords[j];
                    allRecords[j] = temp;
                }
            }
        }
        EventRecord[] lastRecords = new EventRecord[number];
        for (int i = 0; i < number; i++) {
            lastRecords[i] = allRecords[allRecords.length - 1 -i];
        }
        return lastRecords;
    }
    //获取记录详细信息
    public static String getBanInfo(EventRecord eventRecord) {
        String info ="§3§l[XsPlayerBan] §3=================================================\n"+
                        "§3§l[XsPlayerBan] §a事件ID: §c"+eventRecord.getID()+"  删除记录"+"\n"+
                        "§3§l[XsPlayerBan] §a执行时间：§e"+toTimeStr(eventRecord.getRecordTime())+"\n"+
                        "§3§l[XsPlayerBan] §a游戏名: §e"+eventRecord.getPlayerName()+"\n"+
                        "§3§l[XsPlayerBan] §aUUID: §e"+eventRecord.getPlayerUUID()+"\n"+
                        "§3§l[XsPlayerBan] §a封禁信息显示格式: §e"+eventRecord.getModel()+"\n"+
                        "§3§l[XsPlayerBan] §a封禁理由: §e"+eventRecord.getReason()+"\n"+
                        "§3§l[XsPlayerBan] §a封禁时长: §e"+eventRecord.getBanDuration()+"\n"+
                        "§3§l[XsPlayerBan] §a封禁生效时间: §e"+toTimeStr(eventRecord.getBanStart())+"\n"+
                        "§3§l[XsPlayerBan] §a封禁截止时间: §e"+toTimeStr(eventRecord.getBanExpiry())+"\n"+
                        "§3§l[XsPlayerBan] §a执行者: §e"+eventRecord.getEnforcer()+"\n"+
                        "§3§l[XsPlayerBan] §3=================================================";
        return info;
    }
    public static String getSimpleBanInfo(EventRecord eventRecord) {
        String info = "§a"+toTimeStr(eventRecord.getBanStart(), Message.TimeAccuracy.DAY)+" 至 "+toTimeStr(eventRecord.getBanExpiry(), Message.TimeAccuracy.DAY);
        return info;
    }
    // 获取ID
    public String getID() {
        return ID;
    }

    // 设置ID
    public void setID(String ID) {
        this.ID = ID;
    }

    // 获取玩家名称
    public String getPlayerName() {
        return playerName;
    }

    // 设置玩家名称
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    //获取玩家UUID
    public String getPlayerUUID() {
        return playerUUID;
    }
    //设置玩家UUID
    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }
    // 获取开始时间
    public Long getBanStart() {
        return BanStart;
    }

    // 设置开始时间
    public void setBanStart(Long BanStart) {
        this.BanStart = BanStart;
    }

    // 获取持续时间
    public String getBanDuration() {
        return BanDuration;
    }

    // 设置时间
    public void setBanDuration(String BanDuration) {
        this.BanDuration = BanDuration;
    }

    // 获取封禁到期时间
    public Long getBanExpiry() {
        return BanExpiry;
    }

    // 设置封禁到期时间
    public void setBanExpiry(Long banExpiry) {
        BanExpiry = banExpiry;
    }

    // 获取模型
    public String getModel() {
        return model;
    }

    // 设置模型
    public void setModel(String model) {
        this.model = model;
    }

    // 获取封禁原因
    public String getReason() {
        return reason;
    }

    // 设置封禁原因
    public void setReason(String reason) {
        this.reason = reason;
    }

    // 获取执行者
    public String getEnforcer() {
        return Enforcer;
    }

    // 设置执行者
    public void setEnforcer(String enforcer) {
        Enforcer = enforcer;
    }

    // 获取记录执行时间
    public Long getRecordTime() {
        return RecordTime;
    }

    // 设置记录执行时间
    public void setRecordTime(Long RecordTime) {
        this.RecordTime = RecordTime;
    }
}

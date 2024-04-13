package com.xssssss.xsplayerban.Utils;

import com.xssssss.xsplayerban.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xssssss.xsplayerban.EventRecord.EventRecord;
import org.bukkit.configuration.ConfigurationSection;

public class EventRecordLoader {
    public static List<EventRecord> getAllEventRecords() {
        List<EventRecord> eventRecords = new ArrayList<>();

        if (Main.data == null || !Main.data.contains("PlayerList")) {
            return eventRecords;
        }

        Map<String, Object> playerList = Main.data.getConfigurationSection("PlayerList").getValues(false);
        for (String playerUUID : playerList.keySet()) {
            Map<String, Object> playerHistory = new HashMap<>();
            try {
                playerHistory = Main.data.getConfigurationSection("PlayerList." + playerUUID + ".History").getValues(false);
            }catch (NullPointerException e) {
                continue;
            }
            for (String recordTime : playerHistory.keySet()) {
                ConfigurationSection recordTimeNode = Main.data.getConfigurationSection("PlayerList." + playerUUID + ".History." + recordTime);
                Map<String, Object> recordData = new HashMap<>();
                if (recordTimeNode != null) {
                    for (String key : recordTimeNode.getKeys(false)) {
                        Object value = recordTimeNode.get(key);
                        recordData.put(key, value);
                    }
                }
                EventRecord eventRecord = createEventRecord(recordTime, recordData);
                eventRecords.add(eventRecord);
            }

        }
        return eventRecords;
    }

    private static EventRecord createEventRecord(String RecordTime, Map<String, Object> historyData) {
        // 检查是否包含必需的字段
        if (!historyData.containsKey("Type") || !historyData.containsKey("Name")  || !historyData.containsKey("UUID")|| !historyData.containsKey("BanExpiry") || !historyData.containsKey("BanTemplate") || !historyData.containsKey("Reason") || !historyData.containsKey("Enforcer") || !historyData.containsKey("BanDuration") || !historyData.containsKey("ID")) {
            return null;
        }
        // 构建 EventRecord 对象
        EventRecord eventRecord = new EventRecord();
        eventRecord.setID((String) historyData.get("ID"));
        eventRecord.setPlayerName((String) historyData.get("Name"));
        eventRecord.setPlayerUUID((String) historyData.get("UUID"));
        eventRecord.setBanStart(Long.valueOf((String)historyData.get("BanStart")));
        eventRecord.setBanDuration((String) historyData.get("BanDuration"));
        eventRecord.setBanExpiry(Long.valueOf((String)historyData.get("BanExpiry")));
        eventRecord.setModel((String) historyData.get("BanTemplate"));
        eventRecord.setReason((String) historyData.get("Reason"));
        eventRecord.setEnforcer((String) historyData.get("Enforcer"));
        eventRecord.setRecordTime(Long.valueOf(RecordTime));

        /*System.out.println("=========================================");
        System.out.println("ID: " + historyData.get("ID"));
        System.out.println("Name: " + historyData.get("Name"));
        System.out.println("UUID: " + historyData.get("UUID"));
        System.out.println("BanStart: " + historyData.get("BanStart"));
        System.out.println("BanDuration: " + historyData.get("BanDuration"));
        System.out.println("BanExpiry: " + historyData.get("BanExpiry"));
        System.out.println("BanTemplate: " + historyData.get("BanTemplate"));
        System.out.println("Reason: " + historyData.get("Reason"));
        System.out.println("Enforcer: " + historyData.get("Enforcer"));
        System.out.println("RecordTime: " + RecordTime);
        System.out.println("=========================================");*/
        return eventRecord;
    }
}

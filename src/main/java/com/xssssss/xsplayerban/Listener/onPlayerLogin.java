package com.xssssss.xsplayerban.Listener;

import com.xssssss.xsplayerban.Main;
import com.xssssss.xsplayerban.Utils.Message;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class onPlayerLogin implements Listener {
    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        String PlayerName = e.getName();
        String path = "PlayerList."+uuid+".";
        if (Main.data != null && Main.data.isConfigurationSection("PlayerList")) {
            Set<String> playerKeys = Main.data.getConfigurationSection("PlayerList").getKeys(false);
            List<String> playerList = new ArrayList<>(playerKeys);
            if (playerList.contains(uuid.toString())) {
                Main.data.set(path + "Name", PlayerName);
                Main.saveData();
                int expiryTime = Integer.valueOf(Main.data.getString(path + "BanExpiry"));
                String MsgType = Main.data.getString(path + "BanTemplate");
                String Reason = Main.data.getString(path + "Reason");
                String BanID = Main.data.getString(path + "ID");
                String Enforcer = Main.data.getString(path + "Enforcer");
                //System.out.println("获取到玩家的expiryTime为 "+expiryTime);
                if ((System.currentTimeMillis() / 1000) < expiryTime || expiryTime < 0) {
                    e.setKickMessage(Message.getBanMessage(PlayerName, expiryTime, MsgType, Reason, BanID, Enforcer));
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                }
            }
        }
    }
}

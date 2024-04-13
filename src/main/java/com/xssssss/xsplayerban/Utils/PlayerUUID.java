package com.xssssss.xsplayerban.Utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUUID {
    public static UUID getPlayerUUIDByName(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return player.getUniqueId();
        }else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            return offlinePlayer.getUniqueId();
        }
    }
}

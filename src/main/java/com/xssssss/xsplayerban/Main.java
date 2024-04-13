package com.xssssss.xsplayerban;

import com.xssssss.xsplayerban.Commands.CommandsXsBan;
import com.xssssss.xsplayerban.Commands.CommandsXsPlayerBan;
import com.xssssss.xsplayerban.Commands.CommandsXsUnBan;
import com.xssssss.xsplayerban.Listener.onPlayerLogin;

import com.xssssss.xsplayerban.Metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public static Plugin plugin = null;
    public static YamlConfiguration config;
    public static YamlConfiguration data;
    public static YamlConfiguration message;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = getLogger();
        plugin = this;
        config = createResource(this,"","config.yml",false);
        data = createResource(this,"","data.yml",false);
        message = createResource(this,"","messages.yml",false);
        int pluginId = 21580;
        Metrics metrics = new Metrics(this, pluginId);
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "\n"+
                "__   __    ______ _                      ______             \n" +
                "\\ \\ / /    | ___ \\ |                     | ___ \\            \n" +
                " \\ V / ___ | |_/ / | __ _ _   _  ___ _ __| |_/ / __ _ _ __  \n" +
                " /   \\/ __||  __/| |/ _` | | | |/ _ \\ '__| ___ \\/ _` | '_ \\ \n" +
                "/ /^\\ \\__ \\| |   | | (_| | |_| |  __/ |  | |_/ / (_| | | | |\n" +
                "\\/   \\/___/\\_|   |_|\\__,_|\\__, |\\___|_|  \\____/ \\__,_|_| |_|\n" +
                "                           __/ |                            \n" +
                "                          |___/                             "+"\n");
        getLogger().info("XsPlayerBan已加载！");
        getLogger().info("作者：XiaoShuaiOwO");
        getLogger().info("版本："+getDescription().getVersion());

        getCommand("XsBan").setExecutor(new CommandsXsBan());
        getCommand("XsUnBan").setExecutor(new CommandsXsUnBan());
        getCommand("XsPlayerBan").setExecutor(new CommandsXsPlayerBan());

        getServer().getPluginManager().registerEvents(new onPlayerLogin(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("XsPlayerBan已卸载！");
    }

    public YamlConfiguration createResource(Plugin plugin, String dir, String fileName, boolean cover) {
        File dataFolder = plugin.getDataFolder();
        File folder = new File(dataFolder, dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, fileName);
        if (!file.exists() || cover) {
            try {
                if (file.exists() && cover) {
                    file.delete();
                }
                if (!file.exists()) {
                    try (InputStream in = plugin.getResource(fileName)) {
                        Files.copy(in, file.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }
    public static void saveData() {
        try {
            data.save(new File(plugin.getDataFolder(), "data.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("[XsPlayerBan] 无法保存data.yml文件: " + e.getMessage());
        }
    }
}

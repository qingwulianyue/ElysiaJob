package com.elysia.elysiajob.filemanager;

import com.elysia.elysiajob.ElysiaJob;
import com.elysia.elysiajob.filemanager.data.ConfigData;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private ConfigManager(){}
    private static final ConfigManager instance = new ConfigManager();
    private ConfigData configData;
    public static ConfigManager getInstance(){
        return instance;
    }
    public ConfigData getConfigData(){
        if (configData == null)
            loadConfig();
        return configData;
    }
    public void loadConfig(){
        configData = null;
        ElysiaJob.getInstance().reloadConfig();
        FileConfiguration config = ElysiaJob.getInstance().getConfig();
        configData = new ConfigData(
                config.getBoolean("debug"),
                config.getString("prefix"),
                config.getInt("save_timer"),
                config.getBoolean("save_tips"),
                config.getInt("default_mana"),
                config.getInt("default_mana_regen")
        );
        logConfigInfoIfDebug();
    }
    private void logConfigInfoIfDebug(){
        if (configData.isDebug()){
            ElysiaJob.getInstance().getLogger().info("§eDebug: §a" + configData.isDebug());
            ElysiaJob.getInstance().getLogger().info("§ePrefix: §a" + configData.getPrefix());
            ElysiaJob.getInstance().getLogger().info("§eSave Timer: §a" + configData.getSaveTimer());
            ElysiaJob.getInstance().getLogger().info("§eSave Tips: §a" + configData.isSaveTips());
            ElysiaJob.getInstance().getLogger().info("§eDefault Mana: §a" + configData.getDefaultMana());
            ElysiaJob.getInstance().getLogger().info("§eDefault Mana Regen: §a" + configData.getDefaultManaRegen());
        }
    }
}

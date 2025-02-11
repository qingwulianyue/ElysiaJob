package com.elysia.elysiajob;

import com.elysia.elysiajob.command.CommandManager;
import com.elysia.elysiajob.filemanager.ConfigManager;
import com.elysia.elysiajob.filemanager.PlayerDataManager;
import com.elysia.elysiajob.hook.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public final class ElysiaJob extends JavaPlugin {
    private BukkitRunnable manaRegenerationTask;
    private static ElysiaJob instance;
    public static ConfigManager configManager;
    public static PlayerDataManager playerDataManager;
    public static ElysiaJob getInstance() {
        return instance;
    }
    public static ConfigManager getConfigManager(){
        return configManager;
    }
    public static PlayerDataManager getPlayerDataManager(){
        return playerDataManager;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        createFile();
        configManager = ConfigManager.getInstance();
        playerDataManager = PlayerDataManager.getInstance();
        configManager.loadConfig();
        Bukkit.getPluginCommand("ElysiaJob").setExecutor(new CommandManager());
        manaRegenerationTask = new ManaRegenerationTask();
        manaRegenerationTask.runTaskTimerAsynchronously(this, 0, 20);
        checkDepend();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manaRegenerationTask.cancel();
    }
    private void createFile() {
        saveDefaultConfig();
        createDefaultFile();
    }
    private void createDefaultFile(){
        saveDefaultConfig();
        Path folderPath = getDataFolder().toPath().resolve("PlayerData");
        createDirectoryIfNotExists(folderPath);
    }
    private void createDirectoryIfNotExists(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create directory.", e);
            }
        }
    }
    private static class ManaRegenerationTask extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()){
                UUID uuid = player.getUniqueId();
                if (playerDataManager.getPlayerMana(uuid) >= playerDataManager.getPlayerMaxMana(uuid)) continue;
                playerDataManager.setPlayerMana(uuid, Math.min(playerDataManager.getPlayerMana(uuid) + playerDataManager.getPlayerManaRegen(uuid), playerDataManager.getPlayerMaxMana(uuid)));
            }
        }
    }
    private void checkDepend(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            getLogger().info("§a检测到 PlaceholderAPI，正在注册变量");
            new PlaceholderAPIHook().register();
        } else {
            getLogger().warning("§c未检测到 PlaceholderAPI，变量将无法使用");
        }
    }
}

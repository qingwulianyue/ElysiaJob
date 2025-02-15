package com.elysia.elysiajob;

import com.elysia.elysiajob.command.handlers.CommandHandler;
import com.elysia.elysiajob.filemanager.ConfigManager;
import com.elysia.elysiajob.filemanager.PlayerDataManager;
import com.elysia.elysiajob.hook.PlaceholderAPIHook;
import com.elysia.elysiajob.listener.ClientListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public final class ElysiaJob extends JavaPlugin {
    private int manaRegenerationTaskId;
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
//        Bukkit.getPluginCommand("ElysiaJob").setExecutor(new CommandManager());
//        Bukkit.getPluginCommand("ElysiaJob").setTabCompleter(new CommandTabComplete());
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "ElysiaJob", new ClientListener());
        new CommandHandler()
                // 注册
                .register(this);
        manaRegenerationTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()){
                UUID uuid = player.getUniqueId();
                // 如果玩家的魔法值已经达到最大值，则跳过
                if (playerDataManager.getPlayerMana(uuid) >= playerDataManager.getPlayerMaxMana(uuid))
                    return; // 直接返回，不再处理
                // 更新魔法值，确保不超过最大值
                int newMana = Math.min(
                        playerDataManager.getPlayerMana(uuid) + playerDataManager.getPlayerManaRegen(uuid),
                        playerDataManager.getPlayerMaxMana(uuid)
                );
                playerDataManager.setPlayerMana(uuid, newMana);
            }
        }, 0L, 20L).getTaskId();
        checkDepend();
//        new HelpCommand().register();
//        new ReloadCommand().register();
//        new SetCommand().register();
//        new TakeCommand().register();
//        new MaxCommand().register();
//        new RegenCommand().register();
//        new AddCommand().register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTask(manaRegenerationTaskId);
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
    private void checkDepend(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            getLogger().info("§a检测到 PlaceholderAPI，正在注册变量");
            new PlaceholderAPIHook().register();
        } else {
            getLogger().warning("§c未检测到 PlaceholderAPI，变量将无法使用");
        }
    }
}

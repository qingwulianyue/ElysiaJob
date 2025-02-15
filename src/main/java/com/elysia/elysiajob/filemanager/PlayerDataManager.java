package com.elysia.elysiajob.filemanager;

import com.elysia.elysiajob.ElysiaJob;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private PlayerDataManager(){}
    private static final PlayerDataManager instance = new PlayerDataManager();
    public static PlayerDataManager getInstance() {
        return instance;
    }
    // 使用线程安全的 ConcurrentHashMap
    private final ConcurrentHashMap<UUID, Integer> playerMana = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> playerManaRegen = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> playerMaxMana = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> playerStamina = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> playerStaminaRegen = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> playerMaxStamina = new ConcurrentHashMap<>();
    public Integer getPlayerMana(UUID uuid) {
        return playerMana.computeIfAbsent(uuid, key -> ElysiaJob.getConfigManager().getConfigData().getDefaultMana());
    }

    public Integer getPlayerManaRegen(UUID uuid) {
        return playerManaRegen.computeIfAbsent(uuid, key -> ElysiaJob.getConfigManager().getConfigData().getDefaultManaRegen());
    }

    public Integer getPlayerMaxMana(UUID uuid) {
        return playerMaxMana.computeIfAbsent(uuid, key -> ElysiaJob.getConfigManager().getConfigData().getDefaultMana());
    }
    public Integer getPlayerStamina(UUID uuid) {
        return playerStamina.computeIfAbsent(uuid, key -> ElysiaJob.getConfigManager().getConfigData().getDefaultStamina());
    }
    public Integer getPlayerStaminaRegen(UUID uuid) {
        return playerStaminaRegen.computeIfAbsent(uuid, key -> ElysiaJob.getConfigManager().getConfigData().getDefaultStaminaRegen());
    }
    public Integer getPlayerMaxStamina(UUID uuid) {
        return playerMaxStamina.computeIfAbsent(uuid, key -> ElysiaJob.getConfigManager().getConfigData().getDefaultStamina());
    }
    public void setPlayerMana(UUID uuid, Integer mana) {
        playerMana.put(uuid, mana);
    }
    public void setPlayerManaRegen(UUID uuid, Integer manaRegen) {
        playerManaRegen.put(uuid, manaRegen);
    }
    public void setPlayerMaxMana(UUID uuid, Integer maxMana) {
        playerMaxMana.put(uuid, maxMana);
    }
    public void setPlayerStamina(UUID uuid, Integer stamina) {
        playerStamina.put(uuid, stamina);
    }
    public void setPlayerStaminaRegen(UUID uuid, Integer staminaRegen) {
        playerStaminaRegen.put(uuid, staminaRegen);
    }
    public void setPlayerMaxStamina(UUID uuid, Integer maxStamina) {
        playerMaxStamina.put(uuid, maxStamina);
    }
}

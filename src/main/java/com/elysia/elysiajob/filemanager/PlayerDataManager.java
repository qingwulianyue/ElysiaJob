package com.elysia.elysiajob.filemanager;

import com.elysia.elysiajob.ElysiaJob;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    private PlayerDataManager(){}
    private static final PlayerDataManager instance = new PlayerDataManager();
    public static PlayerDataManager getInstance() {
        return instance;
    }
    private final HashMap<UUID, Integer> playerMana = new HashMap<>();
    private final HashMap<UUID, Integer> playerManaRegen = new HashMap<>();
    private final HashMap<UUID, Integer> playerMaxMana = new HashMap<>();
    public Integer getPlayerMana(UUID uuid) {
        if (!playerMana.containsKey(uuid))
            playerMana.put(uuid, ElysiaJob.getConfigManager().getConfigData().getDefaultMana());
        return playerMana.get(uuid);
    }
    public Integer getPlayerManaRegen(UUID uuid) {
        if (!playerManaRegen.containsKey(uuid))
            playerManaRegen.put(uuid, ElysiaJob.getConfigManager().getConfigData().getDefaultManaRegen());
        return playerManaRegen.get(uuid);
    }
    public Integer getPlayerMaxMana(UUID uuid) {
        if (!playerMaxMana.containsKey(uuid))
            playerMaxMana.put(uuid, ElysiaJob.getConfigManager().getConfigData().getDefaultMana());
        return playerMaxMana.get(uuid);
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
}

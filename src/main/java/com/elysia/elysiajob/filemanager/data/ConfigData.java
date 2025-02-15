package com.elysia.elysiajob.filemanager.data;

public class ConfigData {
    private final boolean debug;
    private final String prefix;
    private final int saveTimer;
    private final boolean saveTips;
    private final int defaultMana;
    private final int defaultManaRegen;
    private final int defaultStamina;
    private final int defaultStaminaRegen;

    public ConfigData(boolean debug, String prefix, int saveTimer, boolean saveTips, int defaultMana, int defaultManaRegen, int defaultStamina, int defaultStaminaRegen) {
        this.debug = debug;
        this.prefix = prefix;
        this.saveTimer = saveTimer;
        this.saveTips = saveTips;
        this.defaultMana = defaultMana;
        this.defaultManaRegen = defaultManaRegen;
        this.defaultStamina = defaultStamina;
        this.defaultStaminaRegen = defaultStaminaRegen;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getSaveTimer() {
        return saveTimer;
    }

    public boolean isSaveTips() {
        return saveTips;
    }

    public int getDefaultMana() {
        return defaultMana;
    }

    public int getDefaultManaRegen() {
        return defaultManaRegen;
    }

    public int getDefaultStamina() {
        return defaultStamina;
    }

    public int getDefaultStaminaRegen() {
        return defaultStaminaRegen;
    }
}

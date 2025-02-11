package com.elysia.elysiajob.hook;

import com.elysia.elysiajob.ElysiaJob;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    /**
     * %ElysiaJob_mana%:玩家的当前魔力
     * %ElysiaJob_max_mana%:玩家的魔力最大值
     * %ElysiaJob_mana_regen%:玩家的每秒魔力恢复量
     **/
    @Override
    public @org.jetbrains.annotations.NotNull String getIdentifier() {
        return "ElysiaJob";
    }

    @Override
    public @org.jetbrains.annotations.NotNull String getAuthor() {
        return "Elysia";
    }

    @Override
    public @org.jetbrains.annotations.NotNull String getVersion() {
        return ElysiaJob.getInstance().getDescription().getVersion();
    }
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null || params.isEmpty()) return null;
        if (params.equals("mana")) return ElysiaJob.getPlayerDataManager().getPlayerMana(player.getUniqueId()).toString();
        if (params.equals("max_mana")) return ElysiaJob.getPlayerDataManager().getPlayerMaxMana(player.getUniqueId()).toString();
        if (params.equals("mana_regen")) return ElysiaJob.getPlayerDataManager().getPlayerManaRegen(player.getUniqueId()).toString();
        return null;
    }
}

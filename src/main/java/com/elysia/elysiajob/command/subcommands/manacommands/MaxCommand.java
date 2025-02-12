package com.elysia.elysiajob.command.subcommands.manacommands;

import com.elysia.elysiajob.ElysiaJob;
import com.elysia.elysiajob.command.subcommands.HelpCommand;
import com.elysia.elysiajob.command.subcommands.SubCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaxCommand implements ManaSubCommand{
    @Override
    public String getName() {
        return "max";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"max"};
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 4) {
            SubCommandManager.get(HelpCommand.class).execute(sender, args);
            return;
        }
        String playerName = args[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(args[3]);
        ElysiaJob.getPlayerDataManager().setPlayerMaxMana(player.getUniqueId(), value);
    }
}

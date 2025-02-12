package com.elysia.elysiajob.command.subcommands.manacommands;

import org.bukkit.command.CommandSender;

public interface ManaSubCommand {
    String getName();
    default String[] getAliases() {
        return new String[0];
    }
    void execute(CommandSender sender, String[] args);
    default void register(){
        ManaCommandManager.register(this);
    }
}

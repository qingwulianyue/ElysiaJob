package com.elysia.elysiajob.command.subcommands.manacommands;

import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;

public class ManaCommandManager {
    private static final HashMap<String, ManaSubCommand> COMMAND_MAP = new HashMap<>();
    public static ManaSubCommand get(String nameOrAlias){
        return COMMAND_MAP.values()
                .stream()
                .filter(subCommand -> subCommand.getName().equalsIgnoreCase(nameOrAlias) ||
                        (subCommand.getAliases().length > 0 && ArrayUtils.contains(subCommand.getAliases(), nameOrAlias)))
                .findAny().orElse(null);
    }
    public static ManaSubCommand get(Class<? extends ManaSubCommand> clz){
        return COMMAND_MAP.values()
                .stream()
                .filter(subCommand -> subCommand.getClass() == clz)
                .findAny().orElse(null);
    }
    public static void register(ManaSubCommand... commands){
        for (ManaSubCommand command : commands){
            COMMAND_MAP.put(command.getName(), command);
        }
    }
}

package com.elysia.elysiajob.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> subCommands = new ArrayList<>();
        if (strings.length == 1) {
            if (strings[0].startsWith("h"))
                subCommands.add("help");
            else if (strings[0].startsWith("r"))
                subCommands.add("reload");
            else if (strings[0].startsWith("m"))
                subCommands.add("mana");
            else {
                subCommands.add("help");
                subCommands.add("reload");
                subCommands.add("mana");
            }
        }
        else if (strings.length == 2){
            if (strings[0].equals("mana")){
                if (strings[1].startsWith("r"))
                    subCommands.add("regen");
                else if (strings[1].startsWith("s"))
                    subCommands.add("set");
                else if (strings[1].startsWith("t"))
                    subCommands.add("take");
                else if (strings[1].startsWith("m"))
                    subCommands.add("max");
                else if (strings[1].startsWith("add"))
                    subCommands.add("a");
                else {
                    subCommands.add("regen");
                    subCommands.add("set");
                    subCommands.add("take");
                    subCommands.add("max");
                    subCommands.add("add");
                }
            }
        }
        else if (strings.length == 3){
            if (strings[0].equals("mana")){
                for (Player player : Bukkit.getOnlinePlayers())
                    subCommands.add(player.getName());
            }
        }
        return subCommands;
    }
}

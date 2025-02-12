package com.elysia.elysiajob.command;

import com.elysia.elysiajob.command.subcommands.HelpCommand;
import com.elysia.elysiajob.command.subcommands.ISubCommand;
import com.elysia.elysiajob.command.subcommands.SubCommandManager;
import com.elysia.elysiajob.command.subcommands.manacommands.ManaCommandManager;
import com.elysia.elysiajob.command.subcommands.manacommands.ManaSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0){
            ISubCommand iSubCommand = SubCommandManager.get(HelpCommand.class);
            iSubCommand.execute(commandSender, strings);
            return true;
        }
        String subCommand = strings[0];
        if (subCommand.equals("mana")){
            ManaSubCommand manaSubCommand = ManaCommandManager.get(strings[1]);
            if (manaSubCommand == null){
                SubCommandManager.get(HelpCommand.class).execute(commandSender, strings);
                return true;
            }
            manaSubCommand.execute(commandSender, strings);
            return true;
        }
        return true;
    }
}

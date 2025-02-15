package com.elysia.elysiajob.command.wrappers;

import com.elysia.elysiajob.command.interfaces.AbstractCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 执行器
public class CommandInvoker implements TabExecutor {

    private final AbstractCommandHandler commandHandler;

    public AbstractCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public CommandInvoker(AbstractCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        CommandExecuteInfo commandExecuteInfo = new CommandExecuteInfo(commandSender, args, label, command);
        if (args.length == 0){
            commandHandler.executeWithNoArg(commandExecuteInfo);
            return true;
        }
        Map<Integer, CommandWrapped> commandWrapped = this.commandHandler.getCommandWrapped(commandExecuteInfo);
        Integer max = commandWrapped.keySet().stream().max(Comparator.comparingInt(it -> it))
                .orElse(null);
        if (max != null){
            CommandWrapped lastCommandWrapped = commandWrapped.get(max);
            lastCommandWrapped.execute(commandExecuteInfo);
        }else{
            commandHandler.executeWithNoMatchSubCommand(commandExecuteInfo);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        if (args.length == 0){
            return Collections.emptyList();
        }
        if (args.length == 1){
            return this.commandHandler.getHighestCommandWrapped()
                    .stream()
                    .filter(it -> it.getCommandRegister().name().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(it -> it.getCommandRegister().name())
                    .collect(Collectors.toList());
        }
        CommandExecuteInfo commandExecuteInfo = new CommandExecuteInfo(commandSender, args, label, command);
        Map<Integer, CommandWrapped> commandWrapped = this.commandHandler.getCommandWrapped(commandExecuteInfo);
        Integer max = commandWrapped.keySet().stream().max(Comparator.comparingInt(it -> it))
                .orElse(null);
        if (max != null){
            CommandWrapped lastCommandWrapped = commandWrapped.get(max);
            if (!this.commandHandler.getSubCommandChainMap().get(lastCommandWrapped).isEmpty()){
                return this.commandHandler.getSubCommandChainMap().get(lastCommandWrapped)
                        .values()
                        .stream()
                        .map(it -> it.getCommandRegister().name())
                        .collect(Collectors.toList());
            }
            return lastCommandWrapped.executeTab(commandExecuteInfo);
        }
        return Collections.emptyList();
    }
}

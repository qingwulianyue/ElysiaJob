package com.elysia.elysiajob.command.wrappers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecuteInfo {

    private CommandSender sender;
    private String[] args;
    private String label;
    private Command command;

    public CommandExecuteInfo(CommandSender sender, String[] args, String label, Command command) {
        this.sender = sender;
        this.args = args;
        this.label = label;
        this.command = command;
    }

    public boolean isPlayer(){
        return sender instanceof Player;
    }
    public Player getPlayer(){
        return (Player) sender;
    }


    public CommandSender getSender() {
        return sender;
    }

    public void setSender(CommandSender sender) {
        this.sender = sender;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public CommandExecuteInfo sendMessage(String... messages){
        for (String message : messages){
            this.sender.sendMessage(message.replace('&', ChatColor.COLOR_CHAR));
        }
        return this;
    }

}

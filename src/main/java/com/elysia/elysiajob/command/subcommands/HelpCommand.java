package com.elysia.elysiajob.command.subcommands;

import org.bukkit.command.CommandSender;

public class HelpCommand implements ISubCommand{
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"help"};
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("/ElysiaJob help   -   获取帮助");
        sender.sendMessage("/ElysiaJob mana set {player} {value}   -   设置指定玩家的当前魔力数值，value为-1时设置为全满");
        sender.sendMessage("/ElysiaJob mana take {player} {value}   -   扣除指定玩家的魔力数值，value为-1时全部扣除");
        sender.sendMessage("/ElysiaJob mana add {player} {value}   -   增加指定玩家的魔力数值，value为-1时增加至全满");
        sender.sendMessage("/ElysiaJob mana max {player} {value}   -   设置指定玩家的最大魔力数值");
        sender.sendMessage("/ElysiaJob mana regen {player} {value}   -   设置指定玩家的每秒魔力恢复数值");
    }
}

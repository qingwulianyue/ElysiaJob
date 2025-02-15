package com.elysia.elysiajob.command.handlers;


import com.elysia.elysiajob.ElysiaJob;
import com.elysia.elysiajob.command.annotations.CommandRegister;
import com.elysia.elysiajob.command.interfaces.AbstractCommandHandler;
import com.elysia.elysiajob.command.wrappers.CommandExecuteInfo;
import com.elysia.elysiajob.command.wrappers.CommandWrapped;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// 注册主命令
@CommandRegister(name = "ElysiaJob",aliases = {"ej"},permission = "server.admin",desc = "命令主体",usage = "/@command",level = 0)
public class CommandHandler extends AbstractCommandHandler {

    @CommandRegister(name = "reload",aliases = {"r"},permission = "server.admin",desc = "插件重载",usage = "/@parent-command @command")
    public void reload(CommandExecuteInfo info){
        ElysiaJob.getInstance().reloadConfig();
        info.getSender()
                .sendMessage("重载完成");
    }

    @CommandRegister(name = "help",aliases = {"h"},permission = "server.admin",desc = "插件帮助",usage = "/@parent-command @command")
    public void help(CommandExecuteInfo info){
        info.sendMessage("ElysiaJob &6命令帮助");
        info.sendMessage(" ");
        for (CommandWrapped wrapped : getHighestCommandWrapped()){
            sendHelpMessage(info,wrapped);
        }
    }

    @CommandRegister(name = "mana",aliases = {"m"},permission = "server.admin",desc = "魔力值相关操作",usage = "/@parent-command @command")
    public void mana(CommandExecuteInfo info){
        info.sendMessage("ElysiaJob mana &6命令帮助");
        info.sendMessage(" ");
        for (CommandWrapped wrapped : getLevelCommandWrapped("mana",2)){
            sendHelpMessage(info,wrapped);
        }
    }
    @CommandRegister(name = "stamina",aliases = {"s"},permission = "server.admin",desc = "体力值相关操作",usage = "/@parent-command @command")
    public void stamina(CommandExecuteInfo info){
        info.sendMessage("ElysiaJob mana &6命令帮助");
        info.sendMessage(" ");
        for (CommandWrapped wrapped : getLevelCommandWrapped("stamina",2)){
            sendHelpMessage(info,wrapped);
        }
    }

    @CommandRegister(name = "add",aliases = {"a"},permission = "server.admin",desc = "增加指定玩家的魔力数值，value为-1时增加至全满",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "mana",tabExecuteMethod = "tabCommandPlayer")
    public void manaAdd(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        int value = Integer.parseInt(info.getArgs()[3]);
        Player player = Bukkit.getPlayer(playerName);
        if (player == null){
            info.getSender()
                    .sendMessage("玩家不存在");
            return;
        }
        if (value == -1)
            ElysiaJob.getPlayerDataManager().setPlayerMana(player.getUniqueId(), ElysiaJob.getPlayerDataManager().getPlayerMaxMana(player.getUniqueId()));
        else
            ElysiaJob.getPlayerDataManager().setPlayerMana(player.getUniqueId(), Math.min(ElysiaJob.getPlayerDataManager().getPlayerMana(player.getUniqueId()) + value, ElysiaJob.getPlayerDataManager().getPlayerMaxMana(player.getUniqueId())));
    }
    @CommandRegister(name = "max",aliases = {"m"},permission = "server.admin",desc = "设置指定玩家的最大魔力数值",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "mana",tabExecuteMethod = "tabCommandPlayer")
    public void manaMax(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        ElysiaJob.getPlayerDataManager().setPlayerMaxMana(player.getUniqueId(), value);
    }
    @CommandRegister(name = "regen",aliases = {"r"},permission = "server.admin",desc = "设置指定玩家的每秒魔力恢复数值",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "mana",tabExecuteMethod = "tabCommandPlayer")
    public void manaRegen(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        ElysiaJob.getPlayerDataManager().setPlayerManaRegen(player.getUniqueId(), value);
    }
    @CommandRegister(name = "set",aliases = {"s"},permission = "server.admin",desc = "设置指定玩家的当前魔力数值，value为-1时设置为全满",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "mana",tabExecuteMethod = "tabCommandPlayer")
    public void manaSet(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        if (value == -1)
            ElysiaJob.getPlayerDataManager().setPlayerMana(player.getUniqueId(), ElysiaJob.getPlayerDataManager().getPlayerMaxMana(player.getUniqueId()));
        else
            ElysiaJob.getPlayerDataManager().setPlayerMana(player.getUniqueId(), Math.min(value, ElysiaJob.getPlayerDataManager().getPlayerMaxMana(player.getUniqueId())));
    }
    @CommandRegister(name = "take",aliases = {"t"},permission = "server.admin",desc = "扣除指定玩家的魔力数值，value为-1时全部扣除",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "mana",tabExecuteMethod = "tabCommandPlayer")
    public void manaTake(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        if (value == -1)
            ElysiaJob.getPlayerDataManager().setPlayerMana(player.getUniqueId(), 0);
        else
            ElysiaJob.getPlayerDataManager().setPlayerMana(player.getUniqueId(), Math.max(ElysiaJob.getPlayerDataManager().getPlayerMana(player.getUniqueId()) - value, 0));
    }
    @CommandRegister(name = "add",aliases = {"a"},permission = "server.admin",desc = "增加指定玩家的耐力数值，value为-1时增加至全满",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "stamina",tabExecuteMethod = "tabCommandPlayer")
    public void staminaAdd(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        int value = Integer.parseInt(info.getArgs()[3]);
        Player player = Bukkit.getPlayer(playerName);
        if (player == null){
            info.getSender()
                    .sendMessage("玩家不存在");
            return;
        }
        if (value == -1)
            ElysiaJob.getPlayerDataManager().setPlayerStamina(player.getUniqueId(), ElysiaJob.getPlayerDataManager().getPlayerMaxStamina(player.getUniqueId()));
        else
            ElysiaJob.getPlayerDataManager().setPlayerStamina(player.getUniqueId(), Math.min(ElysiaJob.getPlayerDataManager().getPlayerStamina(player.getUniqueId()) + value, ElysiaJob.getPlayerDataManager().getPlayerMaxStamina(player.getUniqueId())));
    }
    @CommandRegister(name = "max",aliases = {"m"},permission = "server.admin",desc = "设置指定玩家的最大耐力数值",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "stamina",tabExecuteMethod = "tabCommandPlayer")
    public void staminaMax(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        ElysiaJob.getPlayerDataManager().setPlayerMaxStamina(player.getUniqueId(), value);
    }
    @CommandRegister(name = "regen",aliases = {"r"},permission = "server.admin",desc = "设置指定玩家的每秒耐力恢复数值",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "stamina",tabExecuteMethod = "tabCommandPlayer")
    public void staminaRegen(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        ElysiaJob.getPlayerDataManager().setPlayerStaminaRegen(player.getUniqueId(), value);
    }
    @CommandRegister(name = "set",aliases = {"s"},permission = "server.admin",desc = "设置指定玩家的当前耐力数值，value为-1时设置为全满",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "stamina",tabExecuteMethod = "tabCommandPlayer")
    public void staminaSet(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        if (value == -1)
            ElysiaJob.getPlayerDataManager().setPlayerStamina(player.getUniqueId(), ElysiaJob.getPlayerDataManager().getPlayerMaxStamina(player.getUniqueId()));
        else
            ElysiaJob.getPlayerDataManager().setPlayerStamina(player.getUniqueId(), Math.min(value, ElysiaJob.getPlayerDataManager().getPlayerMaxStamina(player.getUniqueId())));
    }
    @CommandRegister(name = "take",aliases = {"t"},permission = "server.admin",desc = "扣除指定玩家的耐力数值，value为-1时全部扣除",usage = "/@main-command @parent-command @command {player} {value}",level = 2,parentName = "stamina",tabExecuteMethod = "tabCommandPlayer")
    public void staminaTake(CommandExecuteInfo info){
        if (info.getArgs().length < 4) return;
        String playerName = info.getArgs()[2];
        Player player = Bukkit.getPlayer(playerName);
        int value = Integer.parseInt(info.getArgs()[3]);
        if (value == -1)
            ElysiaJob.getPlayerDataManager().setPlayerStamina(player.getUniqueId(), 0);
        else
            ElysiaJob.getPlayerDataManager().setPlayerStamina(player.getUniqueId(), Math.max(ElysiaJob.getPlayerDataManager().getPlayerStamina(player.getUniqueId()) - value, 0));
    }

    public List<String> tabCommandPlayer(CommandExecuteInfo info){
        List<String> result = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            result.add(player.getName());
        return result;
    }
}

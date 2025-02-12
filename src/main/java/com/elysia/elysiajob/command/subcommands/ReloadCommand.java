package com.elysia.elysiajob.command.subcommands;

import com.elysia.elysiajob.ElysiaJob;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements ISubCommand{
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"reload"};
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        ElysiaJob.getConfigManager().loadConfig();
        sender.sendMessage("重载成功");
    }
}

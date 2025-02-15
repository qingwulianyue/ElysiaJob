package com.elysia.elysiajob.listener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ClientListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        String message = new String(bytes);
        switch (message){
            case "skill1": player.sendMessage("释放了技能1");break;
            case "skill2": player.sendMessage("释放了技能2");break;
            case "skill3": player.sendMessage("释放了技能3");break;
            case "skill4": player.sendMessage("释放了技能4");break;
            case "skill5": player.sendMessage("释放了技能5");break;
            case "special_skill": player.sendMessage("释放了特殊技能");break;
        }
    }
}

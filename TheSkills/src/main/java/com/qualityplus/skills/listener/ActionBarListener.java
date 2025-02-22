package com.qualityplus.skills.listener;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.base.event.ActionBarMessageEvent;
import com.qualityplus.assistant.base.event.ActionBarMessageEvent.ActionBarType;
import com.qualityplus.assistant.base.nms.AbstractNMS;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Component
public final class ActionBarListener implements Listener {
    @EventHandler
    public void onActionBar(ActionBarMessageEvent event) {
        ActionBarType type = event.getType();

        //if (type != ActionBarType.GAME_INFO) return;

        ((AbstractNMS) TheAssistantPlugin.getAPI().getNms()).blacklist(event.getPlayer().getUniqueId());
    }
}

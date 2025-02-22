package com.qualityplus.enchanting.listener;

import com.qualityplus.enchanting.base.event.TheEnchantingEnchantEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class EcoSkillsInternalListener implements Listener {
    @EventHandler
    public void onPlayerEnchant(final TheEnchantingEnchantEvent event) {
        final Player player = event.getPlayer();

    }
}

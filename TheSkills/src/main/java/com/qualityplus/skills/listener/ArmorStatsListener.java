package com.qualityplus.skills.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.base.event.ArmorEquipEvent;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.api.box.Box;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


@Component
public final class ArmorStatsListener implements Listener {
    private @Inject Box box;

    @EventHandler(ignoreCancelled = true)
    public void onEquip(ArmorEquipEvent e) {
        Player player = e.getPlayer();

        if (!BukkitItemUtil.isNull(e.getNewArmorPiece()))
            TheSkills.getApi()
                    .getItemStats(e.getNewArmorPiece())
                    .forEach((key, value1) -> box.service().getData(player.getUniqueId())
                    .ifPresent(data -> data.getSkills().addArmor(key.getId(), value1)));


        if (!BukkitItemUtil.isNull(e.getOldArmorPiece()))
            TheSkills.getApi()
                    .getItemStats(e.getOldArmorPiece())
                    .forEach((key, value1) -> box.service().getData(player.getUniqueId())
                    .ifPresent(data -> data.getSkills().removeArmor(key.getId(), value1)));

    }
}

package com.qualityplus.dragon.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.location.LocationUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.structure.type.DragonAltar;
import com.qualityplus.dragon.api.service.AltarSetupService;
import com.qualityplus.dragon.base.game.structure.DragonAltarImpl;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;
import java.util.UUID;

/**
 * Handles the Setup
 */
@Component
public final class AltarEditListener implements Listener {
    private @Inject AltarSetupService altarEditService;
    private @Inject Box box;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        UUID uuid = player.getUniqueId();

        if (!altarEditService.playerIsInEditMode(uuid)) return;

        e.setCancelled(true);

        Action action = e.getAction();

        Block block = e.getClickedBlock();

        if (block == null) return;

        if (e.getHand() == EquipmentSlot.HAND && action.equals(Action.LEFT_CLICK_BLOCK)) {
            addAltar(player, block);

        } else if (e.getHand() == EquipmentSlot.OFF_HAND && action.equals(Action.RIGHT_CLICK_BLOCK)) {
            removeAltar(player, block);
        }
    }

    private void addAltar(Player player, Block block) {
        Location location = block.getLocation();

        Optional<DragonAltar> altar = box.structures().getAltar(location);

        if (altar.isPresent()) {
            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.thereIsAnAltarPlaced.replace("%prefix%", box.files().config().prefix)));
        } else {
            box.structures().addStructure(new DragonAltarImpl(location, false));

            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.altarSet
                    .replace("%location%", LocationUtils.toString(location))
                    .replace("%prefix%", box.files().config().prefix)));
        }
    }


    private void removeAltar(Player player, Block block) {
        Location location = block.getLocation();

        Optional<DragonAltar> altar = box.structures().getAltar(location);

        if (altar.isPresent()) {
            box.structures().removeStructure(altar.get());
            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.altarRemoved.replace("%prefix%", box.files().config().prefix)));

        } else {
            player.sendMessage(StringUtils.color(box.files().messages().setupMessages.thereIsntAnAltarToRemove.replace("%prefix%", box.files().config().prefix)));
        }
    }
}

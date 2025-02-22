package com.qualityplus.runes.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.base.gui.runetable.RuneTableGUI;
import com.qualityplus.runes.base.session.RuneSessionImpl;
import com.qualityplus.runes.util.RunesUtils;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Optional;

@Component
public final class RuneTableClickAndRemoveListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onPlace(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();

        if (!(e.getRightClicked() instanceof ArmorStand)) return;

        ArmorStand armorStand = (ArmorStand) e.getRightClicked();

        String name = Optional.ofNullable(armorStand.getName()).orElse("");

        if (!name.equals("RunePart")) return;

        ItemStack inHand = player.getItemInHand();

        if (!BukkitItemUtil.isNull(inHand) && RunesUtils.hasNBTData(inHand, "runeTableRemover")) {

            if (!player.hasPermission("therunes.remove.table")) {
                player.sendMessage(StringUtils.color(box.files().messages().pluginMessages.noPermission));
                return;
            }

            e.setCancelled(true);

            removeRuneTable(armorStand.getLocation());

            armorStand.remove();

            return;
        }

        e.setCancelled(true);

        player.openInventory(new RuneTableGUI(box, new RuneSessionImpl(player.getUniqueId(), null, null, null, null)).getInventory());
    }

    private void removeRuneTable(Location location) {
        Optional.ofNullable(location.getWorld())
                .ifPresent(world -> world
                        .getNearbyEntities(location, 3, 3 ,3)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(entity -> Optional.ofNullable(entity.getCustomName())
                                .orElse("")
                                .equals("RunePart"))
                        .forEach(Entity::remove));
    }
}

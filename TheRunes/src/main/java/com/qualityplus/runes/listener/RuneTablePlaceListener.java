package com.qualityplus.runes.listener;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.util.RunesUtils;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Component
public final class RuneTablePlaceListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Player player = e.getPlayer();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        Block block = e.getClickedBlock();

        if (RunesUtils.hasNBTData(inHand, "runeTableItem")) {
            e.setCancelled(true);

            box.runesService().createRuneTable(block.getLocation());

            try {
                if (!box.files().config().removeRuneTableFromInventoryOnPlace) return;

                player.setItemInHand(BukkitItemUtil.getItemWithout(inHand, 1));
            } catch (Exception ex) {

            }
        }
    }


    @EventHandler
    public void onPlaceRune(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        ItemStack inHand = player.getItemInHand();

        if (BukkitItemUtil.isNull(inHand)) return;

        if (!RunesUtils.isRune(inHand)) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {

        for (Entity entity : e.getEntity().getNearbyEntities(4, 4, 4)) {
            if (entity instanceof ArmorStand && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("RunePart"))
                entity.remove();
        }
    }
}

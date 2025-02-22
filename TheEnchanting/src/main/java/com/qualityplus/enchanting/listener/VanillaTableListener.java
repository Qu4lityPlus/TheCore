package com.qualityplus.enchanting.listener;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.enchanting.api.box.Box;
import com.qualityplus.enchanting.base.gui.enchantmain.EnchantMainGUI;
import com.qualityplus.enchanting.util.EnchantingFinderUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@Component
public final class VanillaTableListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onOpenBrewingStand(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block bl = e.getClickedBlock();

        if (bl == null || bl.getType() != XMaterial.ENCHANTING_TABLE.parseMaterial()) return;

        if (!box.files().config().openAsVanillaEnchantmentTable) return;

        e.setCancelled(true);

        e.getPlayer().openInventory(new EnchantMainGUI(box, 1, EnchantingFinderUtil.getBookShelfPower(bl.getLocation()), null).getInventory());
    }
}

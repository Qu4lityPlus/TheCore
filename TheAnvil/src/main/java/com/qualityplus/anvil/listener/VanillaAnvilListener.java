package com.qualityplus.anvil.listener;

import com.qualityplus.anvil.api.box.Box;
import com.qualityplus.anvil.base.gui.anvilmain.AnvilMainGUI;
import com.qualityplus.anvil.base.session.AnvilSessionImpl;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.assistant.util.block.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public final class VanillaAnvilListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void onOpenBrewingStand(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = e.getClickedBlock();

        if (BlockUtils.isNull(block)) return;

        if (!blockIsAnvil(block)) return;

        if (!box.files().config().openAsVanillaAnvil) return;

        e.setCancelled(true);

        e.getPlayer().openInventory(new AnvilMainGUI(box, new AnvilSessionImpl(null, null, null)).getInventory());
    }

    private boolean blockIsAnvil(Block block) {
        return Stream.of(XMaterial.ANVIL, XMaterial.CHIPPED_ANVIL, XMaterial.DAMAGED_ANVIL)
                .map(XMaterial::parseMaterial)
                .collect(Collectors.toList())
                .contains(block.getType());
    }
}

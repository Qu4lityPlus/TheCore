package com.qualityplus.runes.base.gui.derune.handler;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RemoveSession;
import com.qualityplus.runes.base.gui.ClickHandler;
import com.qualityplus.runes.base.gui.derune.RemoveRuneGUI;
import com.qualityplus.runes.base.session.RemoveSessionImpl;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@AllArgsConstructor
public final class RShiftClickHandler implements ClickHandler {
    private final Box box;
    private final RemoveRuneGUI gui;
    private final RemoveSession session;

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() != gui.getConfig().getToUpgradeSlot()) return;

        player.openInventory(new RemoveRuneGUI(box, new RemoveSessionImpl(null, false)).getInventory());
    }

    @Override
    public void handleOutSide(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        final ItemStack current = Optional.ofNullable(event.getCurrentItem()).map(ItemStack::clone).orElse(null);

        if (!BukkitItemUtil.isNull(session.getItemToRemove())) {
            player.sendMessage(StringUtils.color(box.files().messages().runeMessages.thereIsAnItemPlaced));
            event.setCancelled(true);
            return;
        }

        gui.setGiveItem(false);
        event.setCurrentItem(null);
        player.openInventory(new RemoveRuneGUI(box, new RemoveSessionImpl(current, false)).getInventory());

    }
}

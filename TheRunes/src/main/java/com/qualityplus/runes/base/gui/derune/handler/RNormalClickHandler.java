package com.qualityplus.runes.base.gui.derune.handler;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.session.RemoveSession;
import com.qualityplus.runes.base.gui.ClickHandler;
import com.qualityplus.runes.base.gui.derune.RemoveRuneGUI;
import com.qualityplus.runes.base.session.RemoveSessionImpl;
import com.qualityplus.runes.util.RunesUtils;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@AllArgsConstructor
public final class RNormalClickHandler implements ClickHandler {
    private final Box box;
    private final RemoveRuneGUI gui;
    private final RemoveSession session;


    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == gui.getConfig().getClickToRemoveRune().getSlot()) {
            event.setCancelled(true);

            if (session.getSessionResult().isError()) return;

            gui.setGiveItem(false);

            player.sendMessage(StringUtils.color(box.files().messages().runeMessages.removedRune));

            player.openInventory(new RemoveRuneGUI(box, new RemoveSessionImpl(RunesUtils.removeRuneFromItem(session.getItemToRemove()), true)).getInventory());


        } else if (event.getSlot() == gui.getConfig().getToUpgradeSlot()) {
            event.setCancelled(true);

            final ItemStack copy = Optional.ofNullable(event.getCursor()).map(ItemStack::clone).orElse(null);
            final ItemStack current = Optional.ofNullable(event.getCurrentItem()).map(ItemStack::clone).orElse(null);

            gui.setGiveItem(false);

            player.setItemOnCursor(null);

            player.openInventory(new RemoveRuneGUI(box, new RemoveSessionImpl(copy, false)).getInventory());

            if (BukkitItemUtil.isNull(current)) return;

            //Set in cursor if there was smth in the clicked slot
            player.setItemOnCursor(current);

        }
    }

    @Override
    public void handleOutSide(InventoryClickEvent event) {

    }
}

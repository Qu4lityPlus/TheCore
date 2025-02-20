package com.qualityplus.enchanting.base.gui.enchantmain.handler;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.enchanting.api.box.Box;
import com.qualityplus.enchanting.base.gui.enchantmain.EnchantMainGUI;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@AllArgsConstructor
public final class NormalClickHandler implements ClickHandler{
    private final EnchantMainGUI gui;
    private final int bookShelf;
    private final int page;
    private final Box box;

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        final ItemStack copy = Optional.ofNullable(event.getCursor()).map(ItemStack::clone).orElse(null);
        final ItemStack current = Optional.ofNullable(event.getCurrentItem()).map(ItemStack::clone).orElse(null);

        if (BukkitItemUtil.isNull(copy)) {
            //PickUp
            gui.setGiveItem(false);
            event.setCurrentItem(null);
            player.openInventory(new EnchantMainGUI(box, page, bookShelf, null).getInventory());
            player.setItemOnCursor(current);
        } else {
            //Put
            gui.setGiveItem(false);

            player.setItemOnCursor(null);

            player.openInventory(new EnchantMainGUI(box, page, bookShelf, copy).getInventory());
        }
    }

    @Override
    public void handleOutSide(InventoryClickEvent event) {

    }
}

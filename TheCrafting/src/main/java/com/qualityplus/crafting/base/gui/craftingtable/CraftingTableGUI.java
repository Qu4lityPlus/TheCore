package com.qualityplus.crafting.base.gui.craftingtable;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.craftingtable.handler.result.TableClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CraftingTableGUI extends CraftingGUI {
    private final TableClickHandler tableClickHandler;
    private final CraftingTableGUIConfig config;

    private static Map<Integer, Integer> getSlots(Box box) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 1; i<=9; i++) {
            map.put(i, box.files().inventories().craftingGui.getRecipeSlots().get(i-1));
        }
        return map;
    }

    public CraftingTableGUI(Box box, Player player) {
        super(box.files().inventories().craftingGui, box);

        this.tableClickHandler = new TableClickHandler(inventory, TheAssistantPlugin.getAPI().getNms().createWorkBench(player), box, getSlots(box));
        this.config = box.files().inventories().craftingGui;

        this.tableClickHandler.handleClick(player);
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        setItem(config.getCloseGUI());

        Optional.ofNullable(config.getCustomGoBackItem()).ifPresent(this::setItem);

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (getTarget(event).equals(ClickTarget.INSIDE)) {
            if (isItem(slot, config.getCloseGUI())) {
                player.closeInventory();
            } else if (slot == config.getResultSlot()) {
                tableClickHandler.handleResultClick(event, false);
            } else if (config.getRecipeSlots().contains(slot)) {
                tableClickHandler.handleClick(player);
            } else if (config.getAutoRecipeSlots().contains(slot)) {
                tableClickHandler.handleResultClick(event, true);

            } else if (isItem(slot, config.getCustomGoBackItem())) {
                handleItemCommandClick(player, config.getCustomGoBackItem());
            } else
                event.setCancelled(true);
        } else
            tableClickHandler.handleClick(player);
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        tableClickHandler.handleClick(player);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        config.getRecipeSlots().stream()
                .map(slot -> inventory.getItem(slot))
                .filter(item -> !BukkitItemUtil.isNull(item))
                .forEach(item -> player.getInventory().addItem(item));
    }
}

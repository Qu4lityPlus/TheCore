package com.qualityplus.dragon.gui.dragons;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.mainmenu.MainMenuGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class DragonsGUI extends TheDragonGUI {
    private final DragonsGUIConfig config;

    public DragonsGUI(Box box) {
        super(box.files().inventories().dragonsGUIConfig, box);

        this.config = box.files().inventories().dragonsGUIConfig;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        int slot = 0;

        for (TheDragonEntity entity : box.files().dragons().dragonMap.values()) {

            inventory.setItem(slot, ItemStackUtils.makeItem(config.getDragonItem(), getPlaceholders(entity)));

            slot++;
        }

        inventory.setItem(config.getBackToMainMenu().getSlot(), ItemStackUtils.makeItem(config.getBackToMainMenu()));

        inventory.setItem(config.getCloseGUI().getSlot(), ItemStackUtils.makeItem(config.getCloseGUI()));

        return inventory;
    }

    private List<IPlaceholder> getPlaceholders(TheDragonEntity entity) {
        return Arrays.asList(
                new Placeholder("thedragon_dragon_id", entity.getId()),
                new Placeholder("thedragon_dragon_displayname", entity.getDisplayName()),
                new Placeholder("thedragon_dragon_maxhealth", entity.getMaxHealth()),
                new Placeholder("thedragon_dragon_chance", entity.getChance()),
                new Placeholder("thedragon_dragon_xp", entity.getXp())
                );
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI()))
            player.closeInventory();
        else if (isItem(slot, config.getBackToMainMenu()))
            player.openInventory(new MainMenuGUI(box).getInventory());
    }

}

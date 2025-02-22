package com.qualityplus.dragon.gui.confirm;

import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.guardians.DragonGuardiansGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class ConfirmGUI extends TheDragonGUI {
    private final ConfirmGUIConfig config;
    private final Guardian guardian;

    public ConfirmGUI(Box box, Guardian guardian) {
        super(box.files().inventories().confirmGUIConfig, box);

        this.config = box.files().inventories().confirmGUIConfig;
        this.guardian = guardian;
    }

    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        int slot = e.getSlot();
        e.setCancelled(true);
        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getConfirmItem())) {
            box.files().guardians().remove(guardian.getID());
            player.openInventory(new DragonGuardiansGUI(box, 1).getInventory());
        } else if (isItem(slot, config.getCancelItem()))
            player.openInventory(new DragonGuardiansGUI(box, 1).getInventory());
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        inventory.setItem(config.getConfirmItem().getSlot(), ItemStackUtils.makeItem(config.getConfirmItem()));

        inventory.setItem(config.getCancelItem().getSlot(), ItemStackUtils.makeItem(config.getCancelItem()));

        setItem(config.getCloseGUI());

        return inventory;
    }
}

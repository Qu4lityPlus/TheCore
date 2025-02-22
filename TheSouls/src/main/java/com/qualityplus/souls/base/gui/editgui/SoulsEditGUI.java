package com.qualityplus.souls.base.gui.editgui;

import com.qualityplus.assistant.inventory.GUI;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.api.edition.SoulEdition;
import com.qualityplus.souls.base.gui.SoulsGUI;
import com.qualityplus.souls.base.gui.addcommands.AddCommandsGUI;
import com.qualityplus.souls.base.gui.addmessages.AddMessagesGUI;
import com.qualityplus.souls.base.gui.allsouls.AllSoulsGUI;
import com.qualityplus.souls.base.gui.confirmdelete.ConfirmDeleteGUI;
import com.qualityplus.souls.base.soul.Soul;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class SoulsEditGUI extends SoulsGUI {
    private final SoulsEditGUIConfig config;
    private final SoulEdition edition;
    private final Soul soul;

    public SoulsEditGUI(Box box, Soul soul, SoulEdition edition) {
        super(box.files().inventories().soulsEditGUI, box);

        this.config = box.files().inventories().soulsEditGUI;
        this.edition = edition;
        this.soul = soul;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        setItem(config.getDeleteItem());
        setItem(config.getTeleportItem());
        setItem(config.getAddCommandItem());
        setItem(config.getAddMessageItem());

        setItem(config.getGoBackItem());
        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(GUI.ClickTarget.INSIDE)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem())) {
            player.openInventory(new AllSoulsGUI(box, 1, edition).getInventory());
        } else if (isItem(slot, config.getDeleteItem())) {
            player.openInventory(new ConfirmDeleteGUI(box, soul, edition).getInventory());
        } else if (isItem(slot, config.getTeleportItem())) {
            player.closeInventory();
            Optional.ofNullable(soul).ifPresent(soul1 -> player.teleport(soul1.getLocation().getLocation()));
        } else if (isItem(slot, config.getAddCommandItem())) {
            player.openInventory(new AddCommandsGUI(box, soul, 1, edition).getInventory());
        } else if (isItem(slot, config.getAddMessageItem())) {
            player.openInventory(new AddMessagesGUI(box, soul, 1, edition).getInventory());
        }
    }


}

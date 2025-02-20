package com.qualityplus.souls.base.gui.confirmdelete;

import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.api.edition.SoulEdition;
import com.qualityplus.souls.base.gui.SoulsGUI;
import com.qualityplus.souls.base.gui.editgui.SoulsEditGUI;
import com.qualityplus.souls.base.soul.Soul;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class ConfirmDeleteGUI extends SoulsGUI {
    private final ConfirmDeleteGUIConfig config;
    private final SoulEdition edition;
    private final Soul soul;

    public ConfirmDeleteGUI(Box box, Soul soul, SoulEdition edition) {
        super(box.files().inventories().confirmDeleteGUI, box);

        this.config = box.files().inventories().confirmDeleteGUI;
        this.edition = edition;
        this.soul = soul;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        int slot = event.getSlot();


        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBackItem()) || isItem(slot, config.getCancelItem())) {
            player.openInventory(new SoulsEditGUI(box, soul, edition).getInventory());
        } else if (isItem(slot, config.getConfirmItem())) {
            soul.disable();
            player.closeInventory();
            box.files().souls().soulList.remove(soul);
            player.sendMessage(StringUtils.color(box.files().messages().soulsMessages.soulRemoved.replace("%souls_total%", String.valueOf(box.files().souls().soulList.size()))));
        }


    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());


        setItem(config.getConfirmItem());
        setItem(config.getCancelItem());


        setItem(config.getGoBackItem());

        setItem(config.getCloseGUI());

        return inventory;
    }
}

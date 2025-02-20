package com.qualityplus.dragon.gui.guardians;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.guardian.Guardian;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.confirm.ConfirmGUI;
import com.qualityplus.dragon.gui.guardian.GuardianGUI;
import com.qualityplus.dragon.gui.mainmenu.MainMenuGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class DragonGuardiansGUI extends TheDragonGUI {
    private final Map<Integer, Guardian> guardiansMap = new HashMap<>();
    private final DragonGuardiansGUIConfig config;

    public DragonGuardiansGUI(Box box, int page) {
        super(box.files().inventories().guardiansGUIConfig, box);

        this.maxPerPage = 43;
        this.hasNext = box.files().guardians().guardianMap.size() > maxPerPage * page;
        this.config = box.files().inventories().guardiansGUIConfig;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<Guardian> guardians = new ArrayList<>(box.files().guardians().guardianMap.values());

        int slot = 0;

        int i = maxPerPage * (page - 1);
        if (guardians.size() > 0) {
            while (slot < maxPerPage) {
                if (guardians.size() > i && i >= 0) {
                    Guardian guardian = guardians.get(i);

                    List<IPlaceholder> placeholders = Collections.singletonList(new Placeholder("thedragon_guardian_id", guardian.getID()));

                    inventory.setItem(slot, ItemStackUtils.makeItem(config.getGuardianItem(), placeholders));

                    guardiansMap.put(slot, guardian);
                    slot++;
                    i++;
                    continue;
                }
                slot++;
            }
        }


        if (page > 1) setItem(config.getPreviousPageItem());

        if (hasNext) setItem(config.getNextPageItem());

        inventory.setItem(config.getCloseGUI().getSlot(), ItemStackUtils.makeItem(config.getCloseGUI()));

        inventory.setItem(config.getBackToMainMenu().getSlot(), ItemStackUtils.makeItem(config.getBackToMainMenu()));

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();

        int slot = e.getSlot();

        e.setCancelled(true);

        if (!getTarget(e).equals(ClickTarget.INSIDE)) return;

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getNextPageItem()) && hasNext) {
            player.openInventory(new DragonGuardiansGUI(box, page + 1).getInventory());
        } else if (isItem(slot, config.getPreviousPageItem()) && page > 1) {
            player.openInventory(new DragonGuardiansGUI(box, page - 1).getInventory());
        } else if (isItem(slot, config.getBackToMainMenu())) {
            player.openInventory(new MainMenuGUI(box).getInventory());
        } else {
            Guardian guardian = guardiansMap.getOrDefault(slot, null);

            if (guardian == null) return;

            TheDragonGUI toOpen = e.isLeftClick() ? new GuardianGUI(box, guardian) : new ConfirmGUI(box, guardian);

            player.openInventory(toOpen.getInventory());
        }

    }
}

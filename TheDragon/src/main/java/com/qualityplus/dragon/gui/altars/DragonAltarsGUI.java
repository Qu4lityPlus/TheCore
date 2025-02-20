package com.qualityplus.dragon.gui.altars;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.location.LocationUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.structure.type.DragonAltar;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.mainmenu.MainMenuGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class DragonAltarsGUI extends TheDragonGUI {
    private final Map<Integer, DragonAltar> altarsMap = new HashMap<>();
    private final DragonAltarsGUIConfig config;

    public DragonAltarsGUI(Box box, int page) {
        super(box.files().inventories().dragonAltarsGUIConfig, box);

        this.maxPerPage = 43;
        this.hasNext = box.files().structures().altars.size() > maxPerPage * page;
        this.config = box.files().inventories().dragonAltarsGUIConfig;
        this.page = page;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<DragonAltar> dragonAltars = new ArrayList<>(box.structures().getAltars());

        int slot = 0;

        int i = maxPerPage * (this.page - 1);

        if (dragonAltars.size() > 0) {
            while (slot < maxPerPage) {
                if (dragonAltars.size() > i && i >= 0) {
                    DragonAltar altar = dragonAltars.get(i);

                    List<IPlaceholder> placeholders = Arrays.asList(
                            new Placeholder("thedragon_altar_location", LocationUtils.toString(altar.getLocation())),
                            new Placeholder("thedragon_altar_id", String.valueOf(i))
                    );

                    inventory.setItem(slot, ItemStackUtils.makeItem(config.getAltarItem(), placeholders));

                    this.altarsMap.put(slot, altar);
                    slot++;
                    i++;
                    continue;
                }
                slot++;
            }
        }

        if (page > 1)
            inventory.setItem(config.getPreviousPage().getSlot(), ItemStackUtils.makeItem(config.getPreviousPage()));
        if (hasNext)
            inventory.setItem(config.getNextPage().getSlot(), ItemStackUtils.makeItem(config.getNextPage()));

        inventory.setItem(config.getBackToMainMenu().getSlot(), ItemStackUtils.makeItem(config.getBackToMainMenu()));

        inventory.setItem(config.getCloseGUI().getSlot(), ItemStackUtils.makeItem(config.getCloseGUI()));

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
        } else if (isItem(slot, config.getBackToMainMenu())) {
            player.openInventory(new MainMenuGUI(box).getInventory());
        } else if (isItem(slot, config.getNextPage()) && hasNext) {
            player.openInventory(new DragonAltarsGUI(box, page + 1).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new DragonAltarsGUI(box, page - 1).getInventory());
        } else {
            DragonAltar dragonAltar = altarsMap.getOrDefault(slot, null);

            if (dragonAltar == null) return;

            if (e.isLeftClick()) {
                player.closeInventory();
                player.teleport(dragonAltar.getLocation());
            } else {
                box.structures().removeStructure(dragonAltar);
                player.openInventory(new DragonAltarsGUI(box, page).getInventory());
            }
        }
    }

}

package com.qualityplus.dragon.gui.guardianspawns;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.location.LocationUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.gui.TheDragonGUI;
import com.qualityplus.dragon.gui.mainmenu.MainMenuGUI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GuardianSpawnsGUI extends TheDragonGUI {
    private final Map<Integer, Location> crystalMap = new HashMap<>();
    private final GuardianSpawnsGUIConfig config;

    public GuardianSpawnsGUI(Box box, int page) {
        super(box.files().inventories().guardianSpawnsGUIConfig, box);

        this.maxPerPage = 43;
        this.hasNext = box.files().guardians().guardianSpawns.size() > maxPerPage * page;
        this.config = box.files().inventories().guardianSpawnsGUIConfig;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<Location> crystals = box.files().guardians().guardianSpawns;
        int slot = 0;

        int i = maxPerPage * (page - 1);

        if (crystals.size() > 0) {
            while (slot < maxPerPage) {
                if (crystals.size() > i && i >= 0) {
                    Location location = crystals.get(i);

                    List<IPlaceholder> placeholders = PlaceholderBuilder.create(
                            new Placeholder("thedragon_guardian_spawn_location", LocationUtils.toString(location)),
                            new Placeholder("thedragon_guardian_spawn_id", String.valueOf(i))
                    ).get();

                    inventory.setItem(slot, ItemStackUtils.makeItem(config.getGuardianSpawnItem(), placeholders));

                    crystalMap.put(slot, location);
                    slot++;
                    i++;
                    continue;
                }
                slot++;
            }
        }

        if (hasNext)
            inventory.setItem(config.getNextPage().getSlot(), ItemStackUtils.makeItem(config.getNextPage()));
        if (page > 1)
            inventory.setItem(config.getPreviousPage().getSlot(), ItemStackUtils.makeItem(config.getPreviousPage()));

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
            player.openInventory(new GuardianSpawnsGUI(box, page + 1).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new GuardianSpawnsGUI(box, page - 1).getInventory());
        } else {

            Location toRemove = crystalMap.getOrDefault(slot, null);

            if (toRemove == null) return;

            if (e.isLeftClick()) {
                player.closeInventory();
                player.teleport(toRemove);
            } else {
                box.files().guardians().removeSpawn(toRemove);
                player.openInventory(new GuardianSpawnsGUI(box, page).getInventory());
            }
        }
    }

}

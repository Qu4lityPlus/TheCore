package com.qualityplus.runes.base.gui.options;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.runes.api.box.Box;
import com.qualityplus.runes.api.recipes.Runes;
import com.qualityplus.runes.base.gui.RuneGUI;
import com.qualityplus.runes.base.rune.Rune;
import com.qualityplus.runes.util.RunesPlaceholderUtils;
import com.qualityplus.runes.util.RunesUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AllRunesGUI extends RuneGUI {
    private final Map<Integer, Rune> slotsAndRune = new HashMap<>();
    private final AllRunesGUIConfig config;

    public AllRunesGUI(Box box, int page) {
        super(box.files().inventories().allRunesGUIConfig, box);

        this.config = box.files().inventories().allRunesGUIConfig;
        this.maxPerPage = config.getRuneSlots().size();
        this.hasNext = Runes.values().size() > maxPerPage * page;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<Rune> runes = new ArrayList<>(Runes.values());

        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (runes.size() > 0) {
                while (slot < maxPerPage) {
                    if (runes.size() > i && i >= 0) {
                        Rune rune = runes.get(i);
                        List<IPlaceholder> placeholders = RunesPlaceholderUtils.placeholderWithRequired(rune, 1).get();
                        inventory.setItem(slot, ItemStackUtils.makeItem(config.getRuneItem(), placeholders, rune.getRuneItem().get()));
                        slotsAndRune.put(slot, rune);
                        slot++;
                        i++;
                        continue;
                    }
                    slot++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (page > 1) setItem(config.getPreviousPage());

        if (hasNext) setItem(config.getNextPage());

        setItem(config.getCloseGUI());

        return inventory;
    }



    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getNextPage()) && hasNext) {
            player.openInventory(new AllRunesGUI(box, page + 1).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new AllRunesGUI(box, page - 1).getInventory());
        } else if (slotsAndRune.containsKey(slot)) {
            Rune rune = slotsAndRune.get(slot);

            if (rune == null) return;

            player.getInventory().addItem(RunesUtils.makeRune(box, rune, 1));
        }
    }
}

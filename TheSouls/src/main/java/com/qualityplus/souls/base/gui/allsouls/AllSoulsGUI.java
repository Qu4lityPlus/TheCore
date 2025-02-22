package com.qualityplus.souls.base.gui.allsouls;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.GUI;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.souls.api.box.Box;
import com.qualityplus.souls.api.edition.SoulEdition;
import com.qualityplus.souls.base.gui.SoulsGUI;
import com.qualityplus.souls.base.gui.editgui.SoulsEditGUI;
import com.qualityplus.souls.base.soul.Soul;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class AllSoulsGUI extends SoulsGUI {
    private final Map<Integer, Soul> soulMap = new HashMap<>();
    private final AllSoulsGUIConfig config;
    private final SoulEdition edition;

    public AllSoulsGUI(Box box, int page, SoulEdition edition) {
        super(box.files().inventories().allSoulsGUI, box);

        this.maxPerPage = box.files().inventories().allSoulsGUI.getSoulsPerPage();
        this.hasNext = box.files().souls().soulList.size() > maxPerPage * page;
        this.config = box.files().inventories().allSoulsGUI;
        this.edition = edition;
        this.page = page;
    }

    private List<IPlaceholder> getPlaceholders(Soul soul) {
        return Arrays.asList(
                new Placeholder("soul_location_x", NumberUtil.toInt(soul.getLocation().getX())),
                new Placeholder("soul_location_y", NumberUtil.toInt(soul.getLocation().getY())),
                new Placeholder("soul_location_z", NumberUtil.toInt(soul.getLocation().getZ())),
                new Placeholder("soul_location_world", soul.getLocation().getWorld())
        );
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (!getTarget(event).equals(GUI.ClickTarget.INSIDE)) return;

        int slot = event.getSlot();


        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (soulMap.containsKey(slot)) {
            Optional.ofNullable(soulMap.getOrDefault(slot, null))
                    .ifPresent(soul -> player.openInventory(new SoulsEditGUI(box, soul, edition).getInventory()));
        } else if (isItem(slot, config.getNextPageItem()) && hasNext) {
            player.openInventory(new AllSoulsGUI(box, page + 1, edition).getInventory());
        } else if (isItem(slot, config.getBackPageItem()) && page > 1) {
            player.openInventory(new AllSoulsGUI(box, page - 1, edition).getInventory());
        }


    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<Soul> soulList = box.files().souls().soulList;

        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (soulList.size() > 0) {
                while (slot < maxPerPage) {
                    if (soulList.size() > i && i >= 0) {
                        Soul soul = soulList.get(i);

                        inventory.setItem(slot, ItemStackUtils.makeItem(config.getSoulItem(), getPlaceholders(soul)));

                        soulMap.put(slot, soul);

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

        if (page > 1)
            setItem(config.getBackPageItem());

        if (hasNext)
            setItem(config.getNextPageItem());

        setItem(config.getCloseGUI());

        return inventory;
    }
}

package com.qualityplus.minions.base.gui.layout;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.MinionGUI;
import com.qualityplus.minions.base.gui.main.MainMinionGUI;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.layout.LayoutGUISettings;
import com.qualityplus.minions.base.minions.minion.layout.LayoutItem;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.minions.util.MinionPlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class LayoutGUI extends MinionGUI {
    private final LayoutGUIConfig config;
    private final MinionEntity minionEntity;

    public LayoutGUI(Box box, MinionEntity minionEntity) {
        super(box.files().inventories().layoutGUIConfig, box);

        this.config = box.files().inventories().layoutGUIConfig;
        this.minionEntity = minionEntity;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        Minion minion = getMinion();

        if (minion != null) {
            List<IPlaceholder> upgradePlaceholders = MinionPlaceholderUtil
                    .getMinionPlaceholders(minionEntity.getMinionUniqueId())
                    .with(MinionPlaceholderUtil.getMinionPlaceholders(minion))
                    .get();
            setMinionItem(minion, upgradePlaceholders);
            setItem(config.getGoBack(), upgradePlaceholders);
            setItem(config.getCloseGUI());
        }

        return inventory;
    }

    private void setMinionItem(Minion minion, List<IPlaceholder> placeholders) {
        LayoutGUISettings settings = minion.getLayoutGUISettings();

        if (settings == null || settings.getMinionSlot() < 0) return;

        Optional<ItemStack> itemStack = MinionEggUtil.createFromExistent(box.files().config().minionEggItem, minionEntity.getMinionUniqueId());

        itemStack.ifPresent(item -> inventory.setItem(settings.getMinionSlot(), ItemStackUtils.makeItem(config.getMinionItem(), placeholders, item)));

        Optional.ofNullable(settings.getLayoutItems())
                .orElse(new ArrayList<>())
                .forEach(this::setItem);
    }

    private void setItem(LayoutItem item) {
        item.getSlots().forEach(slot -> inventory.setItem(slot, ItemStackUtils.makeItem(item.getItem())));
    }

    private Minion getMinion() {
        String id = minionEntity.getData().map(MinionData::getMinionId).orElse(null);

        if (id == null) return null;

        return Minions.getByID(id);
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        event.setCancelled(true);

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new MainMinionGUI(box, minionEntity).getInventory());
        }
    }
}

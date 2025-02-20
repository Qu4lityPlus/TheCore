package com.qualityplus.minions.base.gui.changeitem;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.CommonGUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.background.Background;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.api.box.Box;
import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.gui.MinionGUI;
import com.qualityplus.minions.base.gui.main.MainMinionGUI;
import com.qualityplus.minions.base.minions.Minions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.layout.LayoutGUISettings;
import com.qualityplus.minions.base.minions.minion.layout.LayoutItem;
import com.qualityplus.minions.base.minions.minion.update.MinionSettings;
import com.qualityplus.minions.base.minions.minion.update.item.ItemSettings;
import com.qualityplus.minions.base.minions.minion.update.item.UpgradeSettings;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.util.MinionEggUtil;
import com.qualityplus.minions.util.MinionPlaceholderUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ChangeItemGUI extends MinionGUI {
    private final ChangeItemRequest changeItemRequest;
    private final ChangeItemGUIConfig config;
    private final ItemStack newItem;

    public ChangeItemGUI(Box box, ChangeItemRequest changeItemRequest, ItemStack newItem) {
        super(box.files().inventories().changeItemGUIConfig, box);

        this.config = box.files().inventories().changeItemGUIConfig;
        this.changeItemRequest = changeItemRequest;
        this.newItem = newItem;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<IPlaceholder> newItemPlaceholders = PlaceholderBuilder.create(
                new Placeholder("new_item_displayname", BukkitItemUtil.getName(newItem)),
                new Placeholder("new_item_lore", BukkitItemUtil.getItemLore(newItem))
        ).get();
        inventory.setItem(config.getNewItem().getSlot(), ItemStackUtils.makeItem(config.getNewItem(), newItemPlaceholders, newItem));


        ItemStack oldItem = getOldItem();

        if (oldItem != null) {
            List<IPlaceholder> oldItemPlaceholders = PlaceholderBuilder.create(
                    new Placeholder("old_item_displayname", BukkitItemUtil.getName(newItem)),
                    new Placeholder("old_item_lore", BukkitItemUtil.getItemLore(newItem))
            ).get();
            inventory.setItem(config.getOldItem().getSlot(), ItemStackUtils.makeItem(config.getOldItem(), oldItemPlaceholders, oldItem));
        }

        setItem(config.getConfirmItem());

        setItem(config.getRedItem());
        setItem(config.getYellowItem());
        setItem(config.getGreenItem());

        return inventory;
    }

    private ItemStack getOldItem() {
        if (changeItemRequest.is(ChangeItem.UPGRADE_ITEM_REQUIRED_ITEM) || changeItemRequest.is(ChangeItem.UPGRADE_ITEM_TO_GIVE)) {
            Map<String, UpgradeSettings> upgradeSettings = changeItemRequest.getMinion().getMinionUpdateSettings().getUpgradeSettings();
            MinionUpgrade upgrade = changeItemRequest.getMinionUpgrade();

            if (changeItemRequest.is(ChangeItem.UPGRADE_ITEM_TO_GIVE)) {
                return upgradeSettings.get(upgrade.getId())
                        .getItemSettings()
                        .getItemsToGive()
                        .stream()
                        .findFirst()
                        .orElse(null);
            }
            if (changeItemRequest.is(ChangeItem.UPGRADE_ITEM_REQUIRED_ITEM)) {
                return upgradeSettings.get(upgrade.getId())
                        .getItemSettings()
                        .getRequiredItemsToCreateSingle();
            }
        } else if (changeItemRequest.is(ChangeItem.DROP_ITEM)) {
            final MinionSettings minionSettings = changeItemRequest.getMinion().getMinionUpdateSettings();
            final ItemSettings baseItem = minionSettings.getBaseItem();
            if (baseItem == null) {
                return null;
            }

            final List<ItemStack> itemsToGive = baseItem.getItemsToGive();
            if (itemsToGive == null || itemsToGive.isEmpty()) {
                return null;
            }

            return itemsToGive.getFirst();
        }


        return null;
    }

    private void updateItem(Player player) {
        if (changeItemRequest.is(ChangeItem.UPGRADE_ITEM_REQUIRED_ITEM) || changeItemRequest.is(ChangeItem.UPGRADE_ITEM_TO_GIVE)) {
            Map<String, UpgradeSettings> upgradeSettings = changeItemRequest.getMinion().getMinionUpdateSettings().getUpgradeSettings();
            MinionUpgrade upgrade = changeItemRequest.getMinionUpgrade();
            Minion minion = changeItemRequest.getMinion();

            if (changeItemRequest.is(ChangeItem.UPGRADE_ITEM_TO_GIVE)) {
                upgradeSettings.get(upgrade.getId())
                        .getItemSettings()
                        .setItemsToGive(Collections.singletonList(newItem));


                Bukkit.getScheduler().runTaskAsynchronously(TheMinions.getInstance(), () -> minion.getMinionConfig().save());
                player.sendMessage(StringUtils.color("&aSuccessfully Changed item!"));
            }
            if (changeItemRequest.is(ChangeItem.UPGRADE_ITEM_REQUIRED_ITEM)) {
                upgradeSettings.get(upgrade.getId())
                        .getItemSettings()
                        .setRequiredItemsToCreate(FastMap.builder(Integer.class, ItemStack.class)
                                .put(newItem.getAmount(), newItem)
                                .build());

                Bukkit.getScheduler().runTaskAsynchronously(TheMinions.getInstance(), () -> minion.getMinionConfig().save());

                player.sendMessage(StringUtils.color("&aSuccessfully Changed item!"));

            }
        } else if (changeItemRequest.is(ChangeItem.DROP_ITEM)) {
            MinionSettings minionSettings = changeItemRequest.getMinion().getMinionUpdateSettings();
            Minion minion = changeItemRequest.getMinion();

            minionSettings.getBaseItem().setItemsToGive(Collections.singletonList(newItem));

            Bukkit.getScheduler().runTaskAsynchronously(TheMinions.getInstance(), () -> minion.getMinionConfig().save());

            player.sendMessage(StringUtils.color("&aSuccessfully Changed item!"));
        }
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        event.setCancelled(true);

        int slot = event.getSlot();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getConfirmItem())) {
            updateItem(player);

            player.closeInventory();
        }
    }
}

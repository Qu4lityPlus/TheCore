package com.qualityplus.minions.base.minions.entity.getter;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.gui.FakeInventory;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.minions.TheMinions;
import com.qualityplus.minions.base.gui.main.handler.click.UpgradeSlot;
import com.qualityplus.minions.base.minions.entity.MinionStorageState;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.update.item.ItemSettings;
import com.qualityplus.minions.base.minions.minion.update.item.UpgradeSettings;
import com.qualityplus.minions.base.minions.minion.upgrade.MinionUpgrade;
import com.qualityplus.minions.persistance.data.MinionData;
import com.qualityplus.minions.persistance.data.upgrade.UpgradeEntity;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface MinionItemsGetter {
    default MinionStorageState getMinionState(FakeInventory fakeInventory, UUID minionUniqueId, Minion minion) {
        ItemSettings itemSettings = getItemSettings(null, minionUniqueId, minion);

        MinionStorageState storageState = getStateFromSlot(fakeInventory, itemSettings, null);

        for (UpgradeSlot slot : UpgradeSlot.values()) {
            ItemSettings upgradeItemSettings = getItemSettings(slot, minionUniqueId, minion);
            MinionStorageState upgradeStorageState = getStateFromSlot(fakeInventory, upgradeItemSettings, slot);

            if (upgradeStorageState == null) continue;

            if (upgradeStorageState.isHasFullStorage()) continue;

            return upgradeStorageState;
        }
        return storageState;
    }

    default MinionStorageState getStateFromSlot(FakeInventory fakeInventory, ItemSettings itemSettings, UpgradeSlot slot) {

        if (itemSettings == null) return null;

        if (slot != null && !hasRequiredItems(fakeInventory.getItemsArray(), itemSettings))
            return null;

        FakeInventory copyOfFake = TheAssistantPlugin
                .getAPI()
                .getNms()
                .getFakeInventory(null, fakeInventory);

        itemSettings.getRequiredItemsToCreate().forEach((amount, item) -> copyOfFake.removeItems(item, amount));

        Inventory inventory = copyOfFake.getInventory();

        ItemStack[] toAdd = BukkitItemUtil.fromList(itemSettings.getItemsCloneToGive());

        ItemStack[] toAddCopy = BukkitItemUtil.fromList(itemSettings.getItemsCloneToGive());

        Map<Integer, ItemStack> remainingStuff = inventory.addItem(toAdd);

        int remainingAmount = getItemsAmount(new ArrayList<>(remainingStuff.values()));
        int toAddCopyAmount = getItemsAmount(Arrays.stream(toAddCopy).collect(Collectors.toList()));

        boolean hasFullStorage = remainingAmount >= toAddCopyAmount;


        if (copyOfFake.getEmptySlots() <= 0 && slot != null) {
            return null;
        }

        if (hasFullStorage && slot != null) {
            /*Bukkit.getConsoleSender().sendMessage("Remaining: " + remainingAmount + " | To Add Amount: " + toAddCopyAmount);

            Bukkit.getConsoleSender().sendMessage("Return -> " + slot.name());*/
            return null;
        }

        return new MinionStorageState(itemSettings.getRequiredItemsToCreate(), hasFullStorage, toAddCopy, slot);
    }


    default ItemSettings getItemSettings(UpgradeSlot slot, UUID minionUniqueId, Minion minion) {
        if (slot == null) {
            return minion.getMinionUpdateSettings().getBaseItem();
        }

        //Get id of upgrade
        String id = getUpgrade(slot, minionUniqueId);

        if (id == null) return null;

        //Get settings for upgrade with id
        UpgradeSettings settings = minion.getMinionUpdateSettings().getUpgradeSettings().getOrDefault(id,null);

        if (settings == null) return null;

        return settings.getItemSettings();
    }

    default boolean hasRequiredItems(ItemStack[] inventoryItems, ItemSettings itemSettings) {
        Map<Integer, ItemStack> requiredItems = itemSettings.getRequiredItemsToCreate();

        boolean hasRequiredItems = true;

        for (Map.Entry<Integer, ItemStack> entry : requiredItems.entrySet()) {
            int inInventory = InventoryUtils.getItemQuantity(inventoryItems, entry.getValue());

            int requiredAmount = entry.getKey();

            if (inInventory < requiredAmount) {
                hasRequiredItems = false;
                break;
            }
        }

        return hasRequiredItems;
    }

    default String getUpgrade(UpgradeSlot slot, UUID minionUniqueId) {
        Optional<MinionData> data = TheMinions.getApi().getMinionsService().getData(minionUniqueId);

        String id = data
                .map(d -> slot.equals(UpgradeSlot.FIRST_SLOT) ? d.getFirstUpgrade() : d.getSecondUpgrade())
                .map(UpgradeEntity::getId)
                .orElse(null);

        if (id == null) return null;

        MinionUpgrade upgrade = TheMinions.getApi().getConfigFiles().upgrades().normalUpgrades.getOrDefault(id, null);

        if (upgrade == null) return null;

        return upgrade.getId();
    }

    default int getItemsAmount(List<ItemStack> itemStacks) {
        return itemStacks
                .stream()
                .filter(BukkitItemUtil::isNotNull)
                .map(ItemStack::getAmount)
                .mapToInt(i -> i)
                .sum();
    }
}

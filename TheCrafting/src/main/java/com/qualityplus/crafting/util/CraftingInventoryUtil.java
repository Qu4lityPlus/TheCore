package com.qualityplus.crafting.util;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class CraftingInventoryUtil {
    public static Map<ItemStack, Integer> itemStackListToMap(final ItemStack[] items) {
        final Map<ItemStack, Integer> itemStackMap = new HashMap<>();


        for (final ItemStack value : items) {
            if (value == null) {
                continue;
            }
            final ItemStack itemStack = value;
            int amount = itemStack.getAmount();
            ItemStack key = itemStack;

            for (Map.Entry<ItemStack, Integer> requiredEntry : itemStackMap.entrySet()) {
                if (requiredEntry.getKey().isSimilar(key)) {
                    key = requiredEntry.getKey();
                    amount += requiredEntry.getValue();
                }
            }

            itemStackMap.put(key, amount);
        }

        return itemStackMap;
    }
}

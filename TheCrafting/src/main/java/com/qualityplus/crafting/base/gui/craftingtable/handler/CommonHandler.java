package com.qualityplus.crafting.base.gui.craftingtable.handler;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.assistant.lib.de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface CommonHandler {
    default void removeItemsOneByOne(Inventory inventory, Map<Integer, Integer> tableRelationSlots) {
        for (Integer value : tableRelationSlots.values()) {
            ItemStack itemStack = inventory.getItem(value);

            if (BukkitItemUtil.isNull(itemStack)) continue;

            inventory.setItem(value, BukkitItemUtil.getItemWithout(itemStack, 1));
        }
    }

    default void removeOneByOneRecipe(CustomRecipe recipe, Inventory inventory, Map<Integer, Integer> tableRelationSlots) {
        Map<Integer, ItemStack> ingredients = recipe.getIngredients();

        //Fake Slot | Special Slot
        for (Map.Entry<Integer, Integer> entry : tableRelationSlots.entrySet()) {
            ItemStack inRecipe = ingredients.getOrDefault(entry.getKey(), null);

            if (BukkitItemUtil.isNull(inRecipe)) continue;

            ItemStack inTable = inventory.getItem(entry.getValue());

            inventory.setItem(entry.getValue(), BukkitItemUtil.getItemWithout(inTable, inRecipe.getAmount()));
        }
    }

    default boolean isEmptyCrafting(ItemStack itemStack) {

        NBTItem nbtItem = new NBTItem(itemStack);

        return nbtItem.hasKey("craftingEmpty");
    }
}

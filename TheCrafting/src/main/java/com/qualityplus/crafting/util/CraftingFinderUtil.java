package com.qualityplus.crafting.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.crafting.api.recipes.IRecipe;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.crafting.base.recipes.VanillaRecipe;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class CraftingFinderUtil {
    //Normal Slot | Special Slot
    public IRecipe getCraftingRecipe(Inventory inventory, InventoryView fakeTable, Map<Integer, Integer> tableRelationSlots) {
        //Trying to get Vanilla -
        final ItemStack result = fakeTable.getItem(0);

        if (BukkitItemUtil.isNull(result)) {
            for (CustomRecipe recipe : Recipes.values()) {
                final Map<Integer, ItemStack> ingredients = recipe.getIngredients();

                int count = 0;

                //Fake Slot | Special Slot
                for (Map.Entry<Integer, Integer> entry : tableRelationSlots.entrySet()) {
                    final int recipeSlot = entry.getKey();

                    final ItemStack inRecipe = ingredients.getOrDefault(recipeSlot, null);
                    final ItemStack inTable = inventory.getItem(entry.getValue());


                    final boolean inRecipeIsNull = BukkitItemUtil.isNull(inRecipe);
                    final boolean inTableIsNull = BukkitItemUtil.isNull(inTable);

                    if ((inRecipeIsNull && !inTableIsNull)) {
                        count++;
                    }

                    if (inRecipeIsNull || inTableIsNull) {
                        continue;
                    }

                    if (!inTable.isSimilar(inRecipe)) {
                        continue;
                    }

                    if (inTable.getAmount() < inRecipe.getAmount()) {
                        continue;
                    }

                    count++;
                }

                if (count == ingredients.size()) {
                    return recipe;
                }
            }

        } else {
            return new VanillaRecipe(result);
        }
        return null;
    }

    public List<CustomRecipe> getCraftingRecipes(Player player, int amount) {
        List<CustomRecipe> recipes = new ArrayList<>();

        for (CustomRecipe recipe : Recipes.values()) {
            Map<Integer, ItemStack> ingredients = recipe.getIngredients();

            int count = 0;

            //Fake Slot | Special Slot
            for (ItemStack inRecipe : ingredients.values()) {
                if (BukkitItemUtil.isNull(inRecipe)) continue;

                if (!recipe.hasPermission(player)) continue;

                if (InventoryUtils.getItemQuantity(player.getInventory().getContents(), inRecipe) < inRecipe.getAmount()) continue;

                count++;
            }

            if (count == ingredients.size()) recipes.add(recipe);

            if (recipes.size() >= amount) break;
        }

        return recipes;
    }
}

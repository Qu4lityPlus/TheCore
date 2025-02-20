package com.qualityplus.crafting.base.gui.craftingtable.handler.result;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.recipes.IRecipe;
import com.qualityplus.crafting.base.gui.craftingtable.handler.CommonHandler;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.crafting.base.recipes.VanillaRecipe;
import com.qualityplus.crafting.util.CraftingFinderUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class ShiftResultHandler implements CommonHandler {
    //Fake Slot | Special Slot
    private final Map<Integer, Integer> tableRelationSlots;
    private final TableClickHandler clickHandler;
    private final Inventory inventory;
    private final InventoryView view;
    private final Box box;

    public void handle(Player player, ItemStack itemStack, IRecipe automatic, boolean isAuto) {

        if (isEmptyCrafting(itemStack)) return;

        if (isAuto && automatic == null) return;

        IRecipe recipe = isAuto ? automatic : CraftingFinderUtil.getCraftingRecipe(inventory, view, tableRelationSlots);

        if (recipe == null) return;

        if (box.files().blockedCraftings().isBlocked(recipe)) return;

        ItemStack result = recipe.getResult();

        if (recipe instanceof VanillaRecipe) {
            int minAmount = getMinAmount();

            giveItem(player, result, result.getAmount() * minAmount);

            for (int i = 0; i<minAmount; i++) {
                removeItemsOneByOne(inventory, tableRelationSlots);
            }
        } else {
            int craftingTimes = recipe.getCraftAmountTimes(player);

            giveItem(player, result, craftingTimes / result.getAmount());

            for (int i = 0; i < craftingTimes; i++) {
                if (isAuto) {
                    removeItemsFromInventory(player, (CustomRecipe) recipe);
                } else {
                    removeOneByOneRecipe((CustomRecipe) recipe, inventory, tableRelationSlots);
                }
            }

        }

        clickHandler.handleClick(player);

    }

    private void giveItem(Player player, ItemStack itemStack, int amount) {
        if (amount > itemStack.getMaxStackSize())
            for (int i = 0; i<amount; i++)
                player.getInventory().addItem(BukkitItemUtil.getItemWith(itemStack.clone(), 1));
        else
            player.getInventory().addItem(BukkitItemUtil.getItemWith(itemStack.clone(), amount));

    }

    private int getMinRecipeAmount(CustomRecipe recipe) {
        int min = getMaxAmount();

        Map<Integer, ItemStack> ingredients = recipe.getIngredients();

        //Fake Slot | Special Slot
        for (Map.Entry<Integer, Integer> entry : tableRelationSlots.entrySet()) {
            ItemStack inRecipe = ingredients.getOrDefault(entry.getKey(), null);

            if (BukkitItemUtil.isNull(inRecipe)) continue;

            ItemStack inTable = inventory.getItem(entry.getValue());

            if (BukkitItemUtil.isNull(inTable)) continue;

            min = Math.min(min, inTable.getAmount() / inRecipe.getAmount());
        }
        return min;
    }

    private int getMinAutoRecipeAmount(Player player, CustomRecipe recipe) {
        int min = getMaxAmountInAuto(player, recipe);

        Map<Integer, ItemStack> ingredients = recipe.getIngredients();

        //Fake Slot | Special Slot

        for (ItemStack inInventory : player.getInventory().getContents()) {
            if (BukkitItemUtil.isNull(inInventory)) continue;

            for (ItemStack inRecipe : ingredients.values().stream()
                    .filter(item -> !BukkitItemUtil.isNull(item))
                    .collect(Collectors.toList())) {

                if (!inInventory.isSimilar(inRecipe)) continue;

                if (inInventory.getAmount() < inRecipe.getAmount()) continue;

                min = Math.min(min, inInventory.getAmount() / inRecipe.getAmount());

            }
        }

        return min;
    }

    private int getMinAmount() {
        int min = getMaxAmount();
        for (final Integer slot : tableRelationSlots.values()) {
            final ItemStack itemStack = inventory.getItem(slot);

            if (BukkitItemUtil.isNull(itemStack)) {
                continue;
            }

            min = Math.min(itemStack.getAmount(), min);
        }

        return min;
    }

    private int getMaxAmount() {
        int min = 1;
        for (Integer value : tableRelationSlots.values()) {
            ItemStack itemStack = inventory.getItem(value);

            if (BukkitItemUtil.isNull(itemStack)) continue;

            min = Math.max(itemStack.getAmount(), min);
        }

        return min;
    }

    private int getMaxAmountInAuto(Player player, CustomRecipe recipe) {
        int min = 1;
        Map<Integer, ItemStack> ingredients = recipe.getIngredients();

        //Fake Slot | Special Slot

        for (ItemStack inInventory : player.getInventory().getContents()) {
            if (BukkitItemUtil.isNull(inInventory)) continue;

            for (ItemStack inRecipe : ingredients.values().stream()
                    .filter(item -> !BukkitItemUtil.isNull(item))
                    .collect(Collectors.toList())) {

                if (!inInventory.isSimilar(inRecipe)) continue;

                if (inInventory.getAmount() < inRecipe.getAmount()) continue;

                min = Math.max(min, inInventory.getAmount() / min);

            }
        }

        return min;
    }

    private void removeItemsFromInventory(Player player, CustomRecipe recipe) {
        recipe.getIngredients().values()
                .stream()
                .filter(item -> !BukkitItemUtil.isNull(item))
                .forEach(item -> InventoryUtils.removeItems(player.getInventory(), item, item.getAmount()));
    }

}

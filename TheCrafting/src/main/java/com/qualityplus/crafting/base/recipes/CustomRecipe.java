package com.qualityplus.crafting.base.recipes;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Exclude;
import com.qualityplus.assistant.util.map.MapUtils;
import com.qualityplus.crafting.api.recipes.IRecipe;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.crafting.util.CraftingInventoryUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Setter
@Builder
public final class CustomRecipe extends OkaeriConfig implements IRecipe {
    private String id;
    private String displayName;
    private String resultSerialized;
    private String recipePermission;
    private String category;
    private Map<Integer, String> ingredientsSerialized;
    private int slot;
    private int page;

    @Exclude
    private Map<Integer, ItemStack> ingredients;
    @Exclude
    private ItemStack result;

    @Builder
    public CustomRecipe(String id, String displayName, String resultSerialized, Map<Integer, String> ingredientsSerialized, String recipePermission, String category) {
        this.id = id;
        this.category = category;
        this.displayName = displayName;
        this.resultSerialized = resultSerialized;
        this.recipePermission = recipePermission;
        this.ingredientsSerialized = ingredientsSerialized;
    }

    public ItemStack getResult() {
        if (result == null) registerResult();
        return getCopy(result);
    }

    @Override
    public int getCraftAmountTimes(final Player player) {
        int craftAmountTimes = -1;

        final ItemStack[] ingredientsArray = this.ingredients.values().toArray(new ItemStack[0]);
        final ItemStack[] playerInvArray = player.getInventory().getContents();
        final Map<ItemStack, Integer> itemStackMap = CraftingInventoryUtil.itemStackListToMap(ingredientsArray);
        final Map<ItemStack, Integer> playerItemStackMap = CraftingInventoryUtil.itemStackListToMap(playerInvArray);

        for (Map.Entry<ItemStack, Integer> entry : itemStackMap.entrySet()) {
            for (Map.Entry<ItemStack, Integer> playerItem : playerItemStackMap.entrySet()) {
                if (playerItem.getKey().isSimilar(entry.getKey())) {

                    final int amountTimes = playerItem.getValue() / entry.getValue();

                    if (craftAmountTimes == -1) {
                        craftAmountTimes = amountTimes;
                    } else {
                        craftAmountTimes = Math.min(craftAmountTimes,  amountTimes);
                    }
                }
            }
        }
        return craftAmountTimes;
    }

    public Map<Integer, ItemStack> getIngredients() {
        if (ingredients == null) {
            registerIngredients();
        }
        return ingredients;
    }

    public void setIngredientsSerialized(final Map<Integer, String> ingredientsSerialized) {
        this.ingredientsSerialized = ingredientsSerialized;
        registerIngredients();
    }

    public void setResultSerialized(String resultSerialized) {
        this.resultSerialized = resultSerialized;
        registerResult();
    }

    private void registerResult() {
        this.result = Optional.ofNullable(resultSerialized).map(BukkitItemUtil::deserialize).orElse(null);
    }

    private void registerIngredients() {
        this.ingredients = new HashMap<>();

        for (Map.Entry<Integer, String> entry : MapUtils.check(ingredientsSerialized).entrySet()) {
            ingredients.put(entry.getKey(), Optional.ofNullable(entry.getValue())
                    .map(BukkitItemUtil::deserialize)
                    .orElse(null));
        }
    }

    private ItemStack getCopy(ItemStack itemStack) {
        return Optional.ofNullable(itemStack).map(ItemStack::clone).orElse(null);
    }

    public void register() {
        Recipes.registerNewRecipe(this);
    }

    public boolean hasPermission(Player player) {
        return recipePermission == null || player.hasPermission(recipePermission);
    }


    public boolean resultIsEquals(XMaterial material) {
        ItemStack itemStack = getResult();

        if (result == null) return false;

        return result.isSimilar(material.parseItem());
    }

    @Override
    public boolean canCraft(final Player player) {
        boolean canCraft = true;
        final ItemStack[] ingredientsArray = this.ingredients.values().toArray(new ItemStack[0]);
        final ItemStack[] playerInvArray = player.getInventory().getContents();
        final Map<ItemStack, Integer> itemStackMap = CraftingInventoryUtil.itemStackListToMap(ingredientsArray);
        final Map<ItemStack, Integer> playerItemStackMap = CraftingInventoryUtil.itemStackListToMap(playerInvArray);

        for (Map.Entry<ItemStack, Integer> entry : itemStackMap.entrySet()) {
            for (Map.Entry<ItemStack, Integer> playerItem : playerItemStackMap.entrySet()) {
                if (playerItem.getKey().isSimilar(entry.getKey())) {
                    if (playerItem.getValue() < entry.getValue()) {
                        canCraft = false;
                    }
                }
            }
        }
        return canCraft;
    }
}

package com.qualityplus.crafting.util;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class CraftingPlaceholderUtils {
    public List<IPlaceholder> getRecipePlaceholders(CustomRecipe recipe) {
        return Arrays.asList(
                new Placeholder("crafting_recipe_displayname", recipe.getDisplayName()),
                new Placeholder("crafting_recipe_id", recipe.getId()),
                new Placeholder("crafting_recipe_permission", emptyIfNull(recipe.getRecipePermission())),
                new Placeholder("crafting_recipe_category", emptyIfNull(recipe.getCategory())),
                new Placeholder("crafting_recipe_page", recipe.getPage()),
                new Placeholder("crafting_recipe_slot", recipe.getSlot()),

                new Placeholder("crafting_recipe_result_item_displayname", emptyIfNull(BukkitItemUtil.getName(recipe.getResult()))),
                new Placeholder("crafting_recipe_result_item_lore", BukkitItemUtil.getItemLore(recipe.getResult()))
        );
    }

    private String emptyIfNull(String input) {
        return input == null || input.equals("") ? "&cEmpty" : input;
    }
}

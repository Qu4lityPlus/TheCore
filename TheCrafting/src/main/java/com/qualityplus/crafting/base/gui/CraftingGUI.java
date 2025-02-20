package com.qualityplus.crafting.base.gui;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.inventory.GUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.SimpleGUI;
import com.qualityplus.assistant.util.actionbar.ActionBarUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.base.category.Category;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class CraftingGUI extends GUI {
    protected final Box box;
    protected UUID uuid;

    public CraftingGUI(int size, String title, Box box) {
        super(size, title);

        this.box = box;
    }

    public CraftingGUI(SimpleGUI simpleGUI, Box box) {
        super(simpleGUI);

        this.box = box;
    }

    public void setItem(Item item) {
        setItem(item, box.files().config().loreWrapper);
    }

    public void setItem(Item item, List<IPlaceholder> placeholderList) {
        setItem(item, placeholderList, box.files().config().loreWrapper);
    }

    protected void setRecipeItems(CustomRecipe recipe, List<Integer> recipeSlots, int resultSlot) {
        recipe.getIngredients().entrySet().stream()
                .filter(entry -> !BukkitItemUtil.isNull(entry.getValue()))
                .forEach(entry -> inventory.setItem(recipeSlots.get(entry.getKey() - 1), entry.getValue()));

        inventory.setItem(resultSlot, recipe.getResult());
    }

    protected List<IPlaceholder> getCategoryPlaceholders(Category category) {
        return PlaceholderBuilder.create(new Placeholder("category_recipe_displayname", category.getDisplayName()))
                .with(getCategoryPlaceholders(category.getRecipes()))
                .get();
    }

    protected List<IPlaceholder> getCategoryPlaceholders(List<CustomRecipe> recipeList) {
        double xp = getUnlocked(recipeList);
        double maxXp = recipeList.size();
        double percentage = ActionBarUtils.getPercentageFromTotal(xp, maxXp);

        return Arrays.asList(
                new Placeholder("recipes_percentage_progress", percentage),
                new Placeholder("recipes_progress_actionbar", ActionBarUtils.getReplacedBar(percentage)),
                new Placeholder("unlocked_recipes", xp),
                new Placeholder("recipes_total", maxXp)
        );
    }

    private int getUnlocked(List<CustomRecipe> recipeList) {
        return (int) recipeList.stream()
                .filter(recipe -> recipe.hasPermission(Bukkit.getPlayer(uuid)))
                .count();
    }
}

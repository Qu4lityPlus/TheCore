package com.qualityplus.crafting.base.gui.recipes;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.individual.RecipeIndividualGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.crafting.util.CraftingPlaceholderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RecipesGUI extends CraftingGUI {
    private final Map<Integer, CustomRecipe> recipeMap = new HashMap<>();
    private final RecipesGUIConfig config;
    private final RecipeEdition edition;

    public RecipesGUI(Box box, int page, RecipeEdition edition) {
        super(box.files().inventories().recipesGUIConfig, box);

        this.maxPerPage = 43;
        this.hasNext = Recipes.values().size() > maxPerPage * page;
        this.config = box.files().inventories().recipesGUIConfig;
        this.edition = edition;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<CustomRecipe> recipes = new ArrayList<>(Recipes.values());

        try {
            int slot = 0;
            int i = maxPerPage * (page - 1);
            if (recipes.size() > 0) {
                while (slot < maxPerPage) {
                    if (recipes.size() > i && i >= 0) {
                        CustomRecipe recipe = recipes.get(i);
                        List<IPlaceholder> placeholders = CraftingPlaceholderUtils.getRecipePlaceholders(recipe);
                        inventory.setItem(slot, ItemStackUtils.makeItem(config.getRecipeItem(), placeholders, recipe.getResult()));
                        recipeMap.put(slot, recipe);
                        slot++;
                        i++;
                        continue;
                    }
                    slot++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (page > 1) setItem(config.getPreviousPage());

        if (hasNext) setItem(config.getNextPage());

        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) return;

        int slot = event.getSlot();

        Player player = (Player) event.getWhoClicked();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getNextPage()) && hasNext) {
            player.openInventory(new RecipesGUI(box, page + 1, edition).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new RecipesGUI(box, page - 1, edition).getInventory());
        } else if (recipeMap.containsKey(slot)) {
            CustomRecipe recipe = recipeMap.get(slot);

            if (recipe == null) return;

            player.openInventory(new RecipeIndividualGUI(box, recipe, edition).getInventory());
        }
    }
}

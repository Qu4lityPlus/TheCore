package com.qualityplus.crafting.base.gui.preview;

import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.base.category.Category;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.book.sub.RecipeBookSubGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;


public final class RecipePreviewGUI extends CraftingGUI {
    private final RecipePreviewGUIConfig config;
    private final RecipeEdition edition;
    private final CustomRecipe recipe;

    public RecipePreviewGUI(Box box, CustomRecipe recipe, RecipeEdition edition) {
        super(box.files().inventories().recipePreviewGUI.getSize(),
              box.files().inventories().recipePreviewGUI.getTitle().replace("%crafting_recipe_displayname%", recipe.getDisplayName()),
              box);

        this.config = box.files().inventories().recipePreviewGUI;
        this.edition = edition;
        this.recipe = recipe;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        setRecipeItems(recipe, config.getRecipeSlots(), config.getResultSlot());

        setItem(config.getGoBackBook());

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
        } else if (isItem(slot, config.getGoBackBook())) {

            Category category = box.files().categories().getCategory(recipe.getId());

            if (category == null) {
                player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.recipeDontBelongAnyCategory));
                player.closeInventory();
                return;
            }

            player.openInventory(new RecipeBookSubGUI(box, player.getUniqueId(), category, 1, edition).getInventory());
        }
    }

}

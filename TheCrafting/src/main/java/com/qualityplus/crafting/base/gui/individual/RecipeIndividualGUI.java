package com.qualityplus.crafting.base.gui.individual;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.EditionObject;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.modify.ModifyRecipeGUI;
import com.qualityplus.crafting.base.gui.recipes.RecipesGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.crafting.util.CraftingPlaceholderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public final class RecipeIndividualGUI extends CraftingGUI {
    private final RecipeIndividualGUIConfig config;
    private final RecipeEdition edition;
    private final CustomRecipe recipe;

    public RecipeIndividualGUI(Box box, CustomRecipe recipe, RecipeEdition edition) {
        super(box.files().inventories().recipeIndividualGUI, box);

        this.config = box.files().inventories().recipeIndividualGUI;
        this.edition = edition;
        this.recipe = recipe;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        List<IPlaceholder> placeholder = CraftingPlaceholderUtils.getRecipePlaceholders(recipe);

        setItem(config.getModifyPermissionItem(), placeholder);
        setItem(config.getModifyRecipeItem(), placeholder);
        setItem(config.getModifyDisplayNameItem(), placeholder);
        setItem(config.getModifyCategoryItem(), placeholder);
        setItem(config.getModifyPageItem(), placeholder);
        setItem(config.getModifySlotItem(), placeholder);
        setItem(config.getDeleteItem(), placeholder);

        setItem(config.getGoBack());

        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!getTarget(event).equals(ClickTarget.INSIDE)) {
            return;
        }

        final int slot = event.getSlot();

        final Player player = (Player) event.getWhoClicked();

        if (isItem(slot, config.getCloseGUI())) {
            player.closeInventory();
        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new RecipesGUI(box, 1, edition).getInventory());
        } else if (isItem(slot, config.getModifyRecipeItem())) {
            player.openInventory(new ModifyRecipeGUI(box, recipe, edition).getInventory());
        } else if (isItem(slot, config.getModifyDisplayNameItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.typeDisplayName));
            edition.setEditMode(player.getUniqueId(), new EditionObject(recipe, RecipeEdition.EditionType.DISPLAY_NAME));
        } else if (isItem(slot, config.getModifyPermissionItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.typePermission));
            edition.setEditMode(player.getUniqueId(), new EditionObject(recipe, RecipeEdition.EditionType.PERMISSION));
        } else if (isItem(slot, config.getModifyCategoryItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.typeCategory));
            edition.setEditMode(player.getUniqueId(), new EditionObject(recipe, RecipeEdition.EditionType.CATEGORY));
        } else if (isItem(slot, config.getModifySlotItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.typeSlot));
            edition.setEditMode(player.getUniqueId(), new EditionObject(recipe, RecipeEdition.EditionType.SLOT));
        } else if (isItem(slot, config.getModifyPageItem())) {
            player.closeInventory();
            player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.typePage));
            edition.setEditMode(player.getUniqueId(), new EditionObject(recipe, RecipeEdition.EditionType.PAGE));
        } else if (isItem(slot, config.getDeleteItem())) {
            player.closeInventory();

            final CustomRecipe exist = Recipes.getByID(this.recipe.getId());

            if (exist == null) {
                player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.recipeDoesntExist));
                return;
            }

            this.box.files().recipes().craftingRecipes.remove(this.recipe);
            Recipes.removeByID(this.recipe.getId());

            player.sendMessage(StringUtils.color(this.box.files().messages().recipeMessages.successfullyDeletedRecipe));
        }
    }

}

package com.qualityplus.crafting.base.gui.modify;

import com.qualityplus.assistant.api.util.BukkitItemUtil;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.individual.RecipeIndividualGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ModifyRecipeGUI extends CraftingGUI {
    private final ModifyRecipeGUIConfig config;
    private final RecipeEdition edition;
    private final CustomRecipe recipe;

    public ModifyRecipeGUI(Box box, CustomRecipe recipe, RecipeEdition recipeEdition) {
        super(box.files().inventories().modifyRecipeGUI, box);

        this.config = box.files().inventories().modifyRecipeGUI;
        this.edition = recipeEdition;
        this.recipe = recipe;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        setRecipeItems(recipe, config.getRecipeSlots(), config.getResultSlot());

        if (Recipes.getByID(recipe.getId()) != null)
            setItem(config.getGoBack());

        setItem(config.getSaveRecipe());

        setItem(config.getCloseGUI());

        return inventory;
    }

    @Override
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Inventory inventory1 = event.getInventory();
        final Player player = (Player) event.getPlayer();

        for (int slot : this.config.getRecipeSlots()) {
            final ItemStack itemStack = inventory1.getItem(slot);
            if (BukkitItemUtil.isNull(itemStack)) {
                continue;
            }
            player.getInventory().addItem(itemStack);
        }

        final ItemStack resultItem = inventory1.getItem(this.config.getResultSlot());
        if (BukkitItemUtil.isNull(resultItem)) {
            return;
        }
        player.getInventory().addItem(resultItem);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

        if (getTarget(event).equals(ClickTarget.INSIDE)) {

            int slot = event.getSlot();

            Player player = (Player) event.getWhoClicked();

            if (slot == config.getResultSlot() || config.getRecipeSlots().contains(slot))
                return;

            event.setCancelled(true);

            if (isItem(slot, config.getCloseGUI())) {
                player.closeInventory();
            } else if (isItem(slot, config.getSaveRecipe())) {
                final Map<Integer, String> ingredients = getIngredients();

                if (ingredients.isEmpty()) {
                    player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.recipeIngredientsCantBeEmpty));
                    return;
                }

                final ItemStack result = inventory.getItem(config.getResultSlot());

                if (BukkitItemUtil.isNull(result)) {
                    player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.recipeResultCantBeEmpty));
                    return;
                }

                recipe.setIngredientsSerialized(ingredients);
                recipe.setResultSerialized(BukkitItemUtil.serialize(result.clone()));

                player.closeInventory();

                player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.recipeSuccessfullyCreated.replace("%crafting_recipe_id%", recipe.getId())));

                recipe.register();
            } else if (isItem(slot, config.getGoBack()) && Recipes.getByID(recipe.getId()) != null) {
                player.openInventory(new RecipeIndividualGUI(box, recipe, edition).getInventory());
            }
        }


    }

    private Map<Integer, String> getIngredients() {
        Map<Integer, String> ingredients = new HashMap<>();

        for (int i = 1; i<=config.getRecipeSlots().size(); i++) {
            final ItemStack itemStack = inventory.getItem(config.getRecipeSlots().get(i - 1));

            if (BukkitItemUtil.isNull(itemStack)) {
                continue;
            }

            ingredients.put(i, BukkitItemUtil.serialize(itemStack.clone()));
        }
        return ingredients;
    }
}

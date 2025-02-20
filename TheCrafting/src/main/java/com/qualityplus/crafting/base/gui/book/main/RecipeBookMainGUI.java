package com.qualityplus.crafting.base.gui.book.main;

import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.api.recipes.Recipes;
import com.qualityplus.crafting.base.category.Category;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.book.sub.RecipeBookSubGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.crafting.util.CraftingItemStackUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public final class RecipeBookMainGUI extends CraftingGUI {
    private final Map<Integer, Category> recipeMap = new HashMap<>();
    private final RecipeBookMainGUIConfig config;
    private final RecipeEdition edition;

    public RecipeBookMainGUI(Box box, UUID uuid, RecipeEdition edition) {
        super(box.files().inventories().recipeBookMainGUI, box);

        this.config = box.files().inventories().recipeBookMainGUI;
        this.edition = edition;
        this.uuid = uuid;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        inventory.setItem(config.getGeneralProgressItem().getSlot(), ItemStackUtils.makeItem(
                config.getGeneralProgressItem(),
                getCategoryPlaceholders(new ArrayList<>(Recipes.values()))));

        for (Category category : box.files().categories().categoryList) {
            inventory.setItem(category.getSlot(), CraftingItemStackUtil.makeCategoryItem(
                    config.getCategoryItem(),
                    getCategoryPlaceholders(category),
                    category));

            recipeMap.put(category.getSlot(), category);
        }


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
        } else if (recipeMap.containsKey(slot)) {
            Category category = recipeMap.getOrDefault(slot, null);

            if (category == null) return;

            player.openInventory(new RecipeBookSubGUI(box, player.getUniqueId(), category, 1, edition).getInventory());

        }
    }

}

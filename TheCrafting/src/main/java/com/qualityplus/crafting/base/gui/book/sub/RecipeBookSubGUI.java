package com.qualityplus.crafting.base.gui.book.sub;

import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.inventory.InventoryUtils;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.itemstack.ItemStackUtils;
import com.qualityplus.crafting.api.box.Box;
import com.qualityplus.crafting.api.edition.RecipeEdition;
import com.qualityplus.crafting.base.category.Category;
import com.qualityplus.crafting.base.gui.CraftingGUI;
import com.qualityplus.crafting.base.gui.book.main.RecipeBookMainGUI;
import com.qualityplus.crafting.base.gui.preview.RecipePreviewGUI;
import com.qualityplus.crafting.base.recipes.CustomRecipe;
import com.qualityplus.crafting.util.CraftingItemStackUtil;
import com.qualityplus.crafting.util.CraftingPlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public final class RecipeBookSubGUI extends CraftingGUI {
    private final Map<Integer, CustomRecipe> recipeMap = new HashMap<>();
    private final RecipeBookSubGUIConfig config;
    private final RecipeEdition edition;
    private final Category category;

    private final int page;

    public RecipeBookSubGUI(Box box, UUID uuid, Category category, int page, RecipeEdition edition) {
        super(box.files().inventories().recipeBookSubGUI, box);

        this.config = box.files().inventories().recipeBookSubGUI;
        this.category = category;
        this.edition = edition;
        this.uuid = uuid;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.getBackground());

        inventory.setItem(config.getCategoryItem().getSlot(), CraftingItemStackUtil.makeCategoryItem(
                config.getCategoryItem(),
                getCategoryPlaceholders(category),
                category));

        final List<CustomRecipe> recipes = getRecipesForPage(page);

        if (hasNextPage()) {
            setItem(getNextPage());
        }

        if (this.page > 1) {
            setItem(getPreviousPage());
        }

        for (CustomRecipe recipe : recipes) {
            if (recipe.hasPermission(Bukkit.getPlayer(uuid)))
                inventory.setItem(recipe.getSlot(), ItemStackUtils.makeItem(config.getUnlockedItem(), CraftingPlaceholderUtils.getRecipePlaceholders(recipe), recipe.getResult()));
            else
                inventory.setItem(recipe.getSlot(), ItemStackUtils.makeItem(config.getLockedItem()));

            recipeMap.put(recipe.getSlot(), recipe);
        }


        setItem(config.getGoBack());

        setItem(config.getCloseGUI());

        return inventory;
    }

    private boolean hasNextPage() {
        return getRecipesForPage(page + 1).stream()
                .findAny()
                .isPresent();
    }

    private Item getNextPage() {
        return Optional.ofNullable(config.getNextPage())
                .orElse(ItemBuilder.of(XMaterial.BOOK,  53,1, "&dNext Page", Arrays.asList("", "&e» Click to go next Page!")).build());
    }

    private Item getPreviousPage() {
        return Optional.ofNullable(config.getPreviousPage())
                .orElse(ItemBuilder.of(XMaterial.BOOK, 46, 1, "&dPrevious Page", Arrays.asList("", "&e» Click to go to previous Page!")).build());
    }

    private List<CustomRecipe> getRecipesForPage(final int page) {
        return category.getRecipes().stream()
                .filter(recipe -> recipe.getPage() == page)
                .collect(Collectors.toList());
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
        } else if (recipeMap.containsKey(slot)) {
            CustomRecipe recipe = recipeMap.getOrDefault(slot, null);

            if (recipe == null) {
                return;
            }

            if (!recipe.hasPermission(Bukkit.getPlayer(uuid))) {
                player.sendMessage(StringUtils.color(box.files().messages().recipeMessages.notUnlockedYet));
                return;
            }

            player.openInventory(new RecipePreviewGUI(box, recipe, edition).getInventory());

        } else if (isItem(slot, config.getGoBack())) {
            player.openInventory(new RecipeBookMainGUI(box, uuid, edition).getInventory());
        } else if (isItem(slot, config.getNextPage()) && hasNextPage()) {
            player.openInventory(new RecipeBookSubGUI(box, uuid, category, page + 1, edition).getInventory());
        } else if (isItem(slot, config.getPreviousPage()) && page > 1) {
            player.openInventory(new RecipeBookSubGUI(box, uuid, category, page - 1, edition).getInventory());
        }
    }

}

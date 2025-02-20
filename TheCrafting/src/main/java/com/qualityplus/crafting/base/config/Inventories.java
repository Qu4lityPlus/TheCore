package com.qualityplus.crafting.base.config;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.inventory.CommonGUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.background.Background;
import com.qualityplus.assistant.inventory.background.DefaultBackgrounds;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.crafting.base.gui.book.main.RecipeBookMainGUIConfig;
import com.qualityplus.crafting.base.gui.book.sub.RecipeBookSubGUIConfig;
import com.qualityplus.crafting.base.gui.craftingtable.CraftingTableGUIConfig;
import com.qualityplus.crafting.base.gui.individual.RecipeIndividualGUIConfig;
import com.qualityplus.crafting.base.gui.modify.ModifyRecipeGUIConfig;
import com.qualityplus.crafting.base.gui.preview.RecipePreviewGUIConfig;
import com.qualityplus.crafting.base.gui.recipes.RecipesGUIConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;

@Getter
@Setter
@Configuration(path = "inventories.yml")
@Header("================================")
@Header("       Inventories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Inventories extends OkaeriConfig implements DefaultBackgrounds {
    @CustomKey("recipesGUIConfig")
    public RecipesGUIConfig recipesGUIConfig = RecipesGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Recipes",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .recipeItem(ItemBuilder.of(XMaterial.STONE, 1, "&e#%crafting_recipe_id%", Arrays.asList("", "&e» &7Click to edit this recipe")).build())
            .nextPage(ItemBuilder.of(XMaterial.BOOK, 52, 1, "&7Next Page", Collections.emptyList()).enabled(true).build())
            .previousPage(ItemBuilder.of(XMaterial.BOOK, 46, 1, "&7Back Page", Collections.emptyList()).enabled(true).build())
            .build();


    @CustomKey("recipeIndividualGUI")
    public RecipeIndividualGUIConfig recipeIndividualGUI = RecipeIndividualGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Recipe Edit",
                    36,
                    getIndividualBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  31, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))

            .goBack(ItemBuilder.of(XMaterial.ARROW,  30, 1, "&aGo Back", Arrays.asList("", "&e» &7Click to go back")).build())

            .modifyDisplayNameItem(ItemBuilder.of(XMaterial.FILLED_MAP,  10, 1, "&eModify DisplayName", Arrays.asList("", "&7DisplayName: &e%crafting_recipe_displayname%", "", "&e» &7Click to edit display name")).build())
            .modifyRecipeItem(ItemBuilder.of(XMaterial.CRAFTING_TABLE,  11, 1, "&eModify Recipe", Arrays.asList("", "&e» &7Click to edit recipe")).build())
            .modifyPermissionItem(ItemBuilder.of(XMaterial.PAPER,  12, 1, "&eModify Permission", Arrays.asList("", "&7Permission: &e%crafting_recipe_permission%", "", "&e» &7Click to edit permission")).build())
            .modifyCategoryItem(ItemBuilder.of(XMaterial.BOOK,  13, 1, "&eModify Category", Arrays.asList("", "&7Category: &e%crafting_recipe_category%", "", "&e» &7Click to edit category")).build())
            .modifySlotItem(ItemBuilder.of(XMaterial.GOLD_NUGGET,  14, 1, "&eModify Slot", Arrays.asList("", "&7Slot: &e%crafting_recipe_slot%", "", "&e» &7Click to edit slot")).build())
            .modifyPageItem(ItemBuilder.of(XMaterial.DIAMOND,  15, 1, "&eModify Page", Arrays.asList("", "&7Page: &e%crafting_recipe_page%", "", "&e» &7Click to edit page")).build())
            .deleteItem(ItemBuilder.of(XMaterial.REDSTONE,  16, 1, "&eDelete recipe", Arrays.asList("", "&e» &7Click to delete recipe")).build())

            .build();

    @CustomKey("craftingGui")
    public CraftingTableGUIConfig craftingGui = CraftingTableGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Crafting GUI",
                    54,
                    getTableBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .autoRecipeSlots(Arrays.asList(17,26,34))
            .recipeSlots(Arrays.asList(10,11,12,19,20,21,28,29,30))
            .resultSlot(23)
            .resultItemFilled(ItemBuilder.of(XMaterial.BARRIER, 1, "%crafting_recipe_result_item_displayname%", Arrays.asList("&7%crafting_recipe_result_item_lore%",
                    "&8————————————————————",
                    "&7This is the item you are",
                    "&7crafting.",
                    "",
                    "&e» &7Click to craft this item!")).build())
            .resultItemEmpty(ItemBuilder.of(XMaterial.BARRIER, 1, "%crafting_recipe_status_placeholder%", Collections.emptyList()).build())
            .customGoBackItem(ItemBuilder.of(XMaterial.ARROW,  47, 1, "&eGo Back", Arrays.asList("", "&7Click to go back!"))
                            .command("your go back command %player%")
                            .enabled(false)
                            .build())
            .build();

    @CustomKey("modifyRecipeGUI")
    public ModifyRecipeGUIConfig modifyRecipeGUI = ModifyRecipeGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Recipe Edition",
                    54,
                    getRecipeCreationBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .recipeSlots(Arrays.asList(10,11,12,19,20,21,28,29,30))
            .resultSlot(25)
            .goBack(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Arrays.asList("", "&e» &7Click to go back")).build())

            .saveRecipe(ItemBuilder.of(XMaterial.LIME_DYE,  49, 1, "&aSave Recipe", Arrays.asList("", "&e» &7Click to save recipe")).build())
            .build();

    @CustomKey("recipePreviewGUI")
    public RecipePreviewGUIConfig recipePreviewGUI = RecipePreviewGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "%crafting_recipe_displayname% Recipe",
                    54,
                    getRecipeCreationBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .recipeSlots(Arrays.asList(10,11,12,19,20,21,28,29,30))
            .resultSlot(25)

            .goBackBook(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Arrays.asList("", "&e» &7Click to go book menu")).build())

            .build();

    @CustomKey("recipeBookMainGUI")
    public RecipeBookMainGUIConfig recipeBookMainGUI = RecipeBookMainGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Recipe Book",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .generalProgressItem(ItemBuilder.of(XMaterial.BOOK,  4, 1, "&aRecipe Book", Arrays.asList("&7Through your adventure, you will",
                    "&7unlock recipes for all kinds of",
                    "&7special items! You can view how",
                    "&7to craft these items here.",
                    "",
                    "&7Recipe Book Unlocked: &e%recipes_percentage_progress%&6%",
                    "%recipes_progress_actionbar% &e%unlocked_recipes%&6/&e%recipes_total%")).build())
            .categoryItem(ItemBuilder.of(XMaterial.STONE, 1, "&a%category_recipe_displayname% Recipes", Arrays.asList("&7View all of the &a%category_recipe_displayname% Recipes",
                    "&7that you have unlocked!",
                    "",
                    "&7Recipe Unlocked: &e%recipes_percentage_progress%&6%",
                    "%recipes_progress_actionbar% &e%unlocked_recipes%&6/&e%recipes_total%")).build())
            .build();

    @CustomKey("recipeBookSubGUI")
    public RecipeBookSubGUIConfig recipeBookSubGUI = RecipeBookSubGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Recipe Book",
                    54,
                    getBookSubBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .lockedItem(ItemBuilder.of(XMaterial.GRAY_DYE,  4, 1, "&c???", Collections.singletonList("&7Locked Recipe")).build())
            .unlockedItem(ItemBuilder.of(XMaterial.GRAY_DYE,  4, 1, "%crafting_recipe_result_item_displayname%", Arrays.asList("%crafting_recipe_result_item_lore%", "", "&eClick to view recipe")).build())
            .categoryItem(ItemBuilder.of(XMaterial.STONE, 4, 1, "&a%category_recipe_displayname% Recipes", Arrays.asList("&7View all of the &a%category_recipe_displayname% Recipes",
                    "&7that you have unlocked!",
                    "",
                    "&7Recipe Unlocked: &e%recipes_percentage_progress%&6%",
                    "%recipes_progress_actionbar% &e%unlocked_recipes%&6/&e%recipes_total%")).build())
            .goBack(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Arrays.asList("", "&e» &7Click to go back")).build())
            .nextPage(ItemBuilder.of(XMaterial.BOOK,  53,1, "&dNext Page", Arrays.asList("", "&e» Click to go next Page!")).build())
            .previousPage(ItemBuilder.of(XMaterial.BOOK, 46, 1, "&dPrevious Page", Arrays.asList("", "&e» Click to go to previous Page!")).build())
            .build();

    private Background getTableBackground() {
        return new Background(commonMap()
                .put(25, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .build());
    }

    private Background getRecipeCreationBackground() {
        return new Background(commonMap()
                .put(23, ItemBuilder.of(XMaterial.CRAFTING_TABLE, 1, "&aResult Item ➝", Arrays.asList("", "&aItem at the right will be", "&athe result of the craft.")).build())
                .build());
    }

    private  ImmutableMap.Builder<Integer, Item> commonMap() {
        return ImmutableMap.<Integer, Item>builder()
                .put(0, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(3, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(4, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(5, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(8, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(9, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(13, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(14, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(15, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(16, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(22, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(24, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(31, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(32, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(33, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(34, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(35, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(36, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(37, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(38, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(39, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(40, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(41, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(42, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(43, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(44, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(45, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(46, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(47, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(48, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(50, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(51, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(52, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(53, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build());
    }

    private Background getIndividualBackground() {
        return new Background(ImmutableMap.<Integer, Item>builder()
                .put(0, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(3, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(4, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(5, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(8, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(9, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(28, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(29, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(31, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(32, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(33, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(34, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(35, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .build());
    }

    private Background getBookSubBackground() {
        return new Background(ImmutableMap.<Integer, Item>builder()
                .put(0, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(3, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(4, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(5, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(8, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(9, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(35, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(36, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(44, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(45, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(46, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(47, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(48, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(50, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(51, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(52, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(53, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .build());
    }
}

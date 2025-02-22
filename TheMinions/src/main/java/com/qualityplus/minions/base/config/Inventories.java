package com.qualityplus.minions.base.config;

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
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.minions.base.gui.changeitem.ChangeItemGUIConfig;
import com.qualityplus.minions.base.gui.layout.LayoutGUIConfig;
import com.qualityplus.minions.base.gui.main.MainMinionGUIConfig;
import com.qualityplus.minions.base.gui.preview.MinionRecipePreviewGUIConfig;
import com.qualityplus.minions.base.gui.recipes.MinionRecipesGUIConfig;
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
    @CustomKey("minionGUIConfig")
    public MainMinionGUIConfig minionGUIConfig = MainMinionGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "&8%minion_egg_displayname% Minion %minion_level_roman%",
                    54,
                    getMinionGUIBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))
            .minionSkinEmptyItem(ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE,  10, 1, "&aMinion Skin Slot", Arrays.asList("&7You can insert a Minion Skin", "&7here to change the appearrance of", "&7your minion.")).build())

            .minionFuelItem(ItemBuilder.of(XMaterial.ORANGE_STAINED_GLASS_PANE, 19, 1, "&aFuel", Arrays.asList("&7Increase the speed of your", "&7minion by adding minion fuel", "&7items here.", "", "&cNote: &7You can't taje fuel", "&7back out after you place it", "&7here!")).build())
            .minionAutomatedShipping(ItemBuilder.of(XMaterial.BLUE_STAINED_GLASS_PANE,  28, 1, "&aAutomated Shipping", Arrays.asList("&7Add a &aBudget Hopper &7or", "&9Enchanted Hopper &7here to make", "&7your minion automatically sell", "&7generated items after its", "&7inventory is full.")).build())
            .firstUpgradeEmptyItem(ItemBuilder.of(XMaterial.YELLOW_STAINED_GLASS_PANE,  37, 1, "&aUpgrade Slot", Arrays.asList("&7You can improve your minion by", "&7adding a minion upgrade item", "&7here.")).build())
            .secondUpgradeEmptyItem(ItemBuilder.of(XMaterial.YELLOW_STAINED_GLASS_PANE,  46, 1, "&aUpgrade Slot", Arrays.asList("&7You can improve your minion by", "&7adding a minion upgrade item", "&7here.")).build())
            .idealLayoutItem(ItemBuilder.of(XMaterial.REDSTONE_TORCH,  3, 1, "&aIdeal Layout", Arrays.asList("&7View the most efficient spot for", "&7this minion to be placed in.", "", "&eClick to view!")).build())
            .minionItem(ItemBuilder.of(XMaterial.PLAYER_HEAD,  4, 1, "&9%minion_egg_displayname% Minion %minion_level_roman%", Arrays.asList("&7%minion_description%",
                    "",
                    "&7Time Between Actions: &a%minion_time_between_actions%s", "&7Max Storage: &a%minion_max_storage%", "&7Resources Generated: &b%minion_resources_generated%")).build())
            .minionTierItem(ItemBuilder.of(XMaterial.GOLD_INGOT,  5, 1, "&aNext Tier", Arrays.asList("&7View the items required to", "&7upgrade this minion to the next", "&7tier.", "", "%minion_upgrade_status%", "", "&eClick to view!")).build())
            .collectAllItem(ItemBuilder.of(XMaterial.CHEST,  48, 1, "&aCollect All", Collections.singletonList("&eClick to collect all items!")).build())
            .quickUpgradeItem(ItemBuilder.of(XMaterial.DIAMOND,  50, 1, "&aQuick-Upgrade Minion", Arrays.asList("&7Click here to upgrade your", "&7minion to the next tier.", "", "%minion_upgrade_status%", "", "%minion_can_upgrade%")).build())
            .pickUpMinion(ItemBuilder.of(XMaterial.BEDROCK,  53, 1, "&aPickup Minion", Collections.singletonList("&eClick to pickup!")).build())
            .storageSlotLocked(ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&cLocked Storage Slot", Collections.singletonList("&7Level up to unlock this slot!")).build())
            .automatedShippingPlacedItem(ItemBuilder.of(XMaterial.STONE, 28, 1, "%minion_upgrade_item_display_name%", Arrays.asList(
                    "%minion_upgrade_item_lore%",
                    "",
                    "&7Items Sold: &b%minion_upgrade_sold_items%",
                    "&7Held Coins: &b%minion_upgrade_held_coins%",
                    "",
                    "&eClick to take coins!")).build())
            .minionSkinPlacedItem(ItemBuilder.of(XMaterial.PLAYER_HEAD,  10, 1, "&a%minion_skin_item_display_name%", Collections.singletonList("&7Your minion looks fabulous!")).build())

            .fuelPlacedItem(ItemBuilder.of(XMaterial.STONE, 19, 1, "%minion_upgrade_item_display_name%", Arrays.asList(
                    "%minion_upgrade_item_lore%",
                    "",
                    "&7Remaining Time: &b%minion_upgrade_remaining_time%",
                    "",
                    "&eClick to take out!")).build())
            .firstUpgradePlacedItem(ItemBuilder.of(XMaterial.STONE, 37, 1, "%minion_upgrade_item_display_name%", Arrays.asList(
                    "%minion_upgrade_item_lore%",
                    "",
                    "&eClick to take out!")).build())
            .secondUpgradePlacedItem(ItemBuilder.of(XMaterial.STONE, 46, 1, "%minion_upgrade_item_display_name%", Arrays.asList(
                    "%minion_upgrade_item_lore%",
                    "",
                    "&eClick to take out!")).build())
            .storageSlots(Arrays.asList(21,22,23,24,25,30,31,32,33,34,39,40,41,42,43))
            .build();

    @CustomKey("layoutGUIConfig")
    public LayoutGUIConfig layoutGUIConfig = LayoutGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "&8Ideal Layout (Top-down view)",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(true).build()
            ))
            .minionItem(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&9%minion_egg_displayname% Minion %minion_level_roman%", Arrays.asList("&7%minion_description%",
                    "",
                    "&7Time Between Actions: &a%minion_time_between_actions%s", "&7Max Storage: &a%minion_max_storage%", "&7Resources Generated: &b%minion_resources_generated%")).build())

            .goBack(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Collections.singletonList("&7To %minion_egg_displayname% %minion_level_roman%")).build())
            .build();

    @CustomKey("changeItemGUIConfig")
    public ChangeItemGUIConfig changeItemGUIConfig = ChangeItemGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "&8Confirm Item change",
                    36,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  36, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(false).build()
            ))

            .oldItem(ItemBuilder.of(XMaterial.STONE,  11, 1, "&a%old_item_displayname%", Arrays.asList("%old_item_lore%", "", "&e————————————————————", "&6This is the old item")).build())
            .newItem(ItemBuilder.of(XMaterial.STONE,  15, 1, "&a%new_item_displayname%", Arrays.asList("%new_item_lore%", "", "&e————————————————————", "&6This is the new item")).build())
            .redItem(ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE,  12, 1, "&c»", Collections.emptyList()).build())
            .yellowItem(ItemBuilder.of(XMaterial.YELLOW_STAINED_GLASS_PANE,  13, 1, "&e»", Collections.emptyList()).build())
            .greenItem(ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE,  14, 1, "&a»", Collections.emptyList()).build())
            .confirmItem(ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE,  31, 1, "&aConfirm", Arrays.asList("", "&e» &7Click to confirm")).build())

            .build();


    @CustomKey("recipePreviewGUIConfig")
    public MinionRecipePreviewGUIConfig recipePreviewGUIConfig = MinionRecipePreviewGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "&8%minion_egg_displayname% Minion %minion_level_roman% Recipe",
                    54,
                    getRecipeCreationBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(true).build()
            ))
            .recipeSlots(Arrays.asList(10,11,12,19,20,21,28,29,30))
            .resultSlot(25)
            .goBack(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Collections.singletonList("&7To &a%minion_egg_displayname% %minion_level_roman%")).build())
            .build();

    @CustomKey("minionRecipesGUIConfig")
    public MinionRecipesGUIConfig minionRecipesGUIConfig = MinionRecipesGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "&8%minion_egg_displayname% Minion Recipes",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).enabled(true).build()
            ))
            .levelSlotsMap(FastMap.builder(Integer.class, Integer.class)
                    .put(11, 1)
                    .put(12, 2)
                    .put(13, 3)
                    .put(14, 4)
                    .put(15, 5)
                    .put(21, 6)
                    .put(22, 7)
                    .put(23, 8)
                    .put(30, 9)
                    .put(31, 10)
                    .put(32, 11)
                    .build())
            .minionItem(ItemBuilder.of(XMaterial.PLAYER_HEAD, 1, "&9%minion_egg_displayname% Minion %minion_level_roman%", Arrays.asList(
                    "&7%minion_description%",
                    "",
                    "&7Time Between Actions: &a%minion_time_between_actions%s",
                    "&7Max Storage: &a%minion_max_storage%",
                    "&7Resources Generated: &b%minion_resources_generated%",
                    "",
                    "%minion_recipe_status%")).build())

            .goBack(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Collections.singletonList("&7To &a%minion_egg_displayname% %minion_level_roman%")).build())
            .build();

    private Background getMinionGUIBackground() {
        return new Background(ImmutableMap.<Integer, Item>builder()
                .put(0, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(8, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(9, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(11, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(12, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(13, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(14, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(15, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(16, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(20, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(29, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(35, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(36, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(38, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(44, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(45, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(47, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(49, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(51, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(52, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
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
}

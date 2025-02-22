package com.qualityplus.enchanting.base.config;

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
import com.qualityplus.enchanting.base.gui.enchantmain.EnchantMainGUIConfig;
import com.qualityplus.enchanting.base.gui.enchantsub.EnchantSubGUIConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Configuration(path = "inventories.yml")
@Header("================================")
@Header("       Inventories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Inventories extends OkaeriConfig implements DefaultBackgrounds {
    @CustomKey("enchantMainGUI")
    public EnchantMainGUIConfig enchantMainGUI = EnchantMainGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Enchant Item",
                    54,
                    getMainBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .enchantItemNoBookShelfPower(ItemBuilder.of(XMaterial.ENCHANTED_BOOK, 1, "&c%enchanting_enchant_displayname%", Arrays.asList("&7%enchanting_enchant_description%", "", "&cRequires %enchanting_enchant_required_bookshelf% bookshelf power!")).build())
            .enchantItem(ItemBuilder.of(XMaterial.ENCHANTED_BOOK, 1, "&a%enchanting_enchant_displayname%", Arrays.asList("&7%enchanting_enchant_description%", "", "&eClick to view")).build())
            .backPageItem(ItemBuilder.of(XMaterial.PLAYER_HEAD,  17, 1, "&aPrevious Page", Collections.singletonList("&8Page %previous_page%"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmFiYWQ5MjRiMjIzNzJiYzk2NmE2ZDUxN2QyZjFiOGI1N2ZkZDI2MmI0ZTA0ZjQ4MzUyZTY4M2ZmZjkyIn19fQ==").build())
            .nextPageItem(ItemBuilder.of(XMaterial.PLAYER_HEAD,  35, 1, "&aNext Page", Collections.singletonList("&8Page %next_page%"))
                    .headData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=").build())
            .bookShelfItem(ItemBuilder.of(XMaterial.BOOKSHELF,  48, 1, "&dBookshelf Power", Arrays.asList("&7Stronger enchantments require", "&7more bookshelf power which can",
                    "&7be increased by placing", "&7bookshelves nearby.", "", "&7Current Bookshelf Power: &d%enchanting_table_bookshelf%")).build())
            .enchantmentSlots(Arrays.asList(12,13,14,15,16,21,22,23,24,25,30,31,32,33,34))
            .emptyItem(ItemBuilder.of(XMaterial.GRAY_DYE, 23, 1, "&cEnchant Item", Arrays.asList("&7Place an item in the open slot", "&7enchant it!")).build())
            .cannotEnchantItem(ItemBuilder.of(XMaterial.RED_DYE, 23, 1, "&cCannot Enchant Item!", Collections.singletonList("&7This item cannot be enchanted!")).build())
            .customGoBackItem(ItemBuilder.of(XMaterial.ARROW,  47, 1, "&eGo Back", Arrays.asList("", "&7Click to go back!"))
                    .command("your go back command %player%")
                    .enabled(false)
                    .build())
            .build();

    @CustomKey("levelEnchantGUI")
    public EnchantSubGUIConfig levelEnchantGUI = EnchantSubGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Enchant Item",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .enchantItem(ItemBuilder.of(XMaterial.ENCHANTED_BOOK, 1, "&fEnchanted Book", Arrays.asList("&9%enchanting_enchant_displayname% %enchanting_enchant_level_to_enchant_roman%", "&7%enchanting_enchant_description%", "", "&7Cost: %enchanting_enchant_exp_cost%", "&7Cost: %enchanting_enchant_money_cost%", "", "%enchanting_enchant_warning%", "%enchanting_enchant_click_placeholder%")).build())
            .goBackItem(ItemBuilder.of(XMaterial.ARROW,  48, 1, "&aGo Back", Collections.singletonList("&eClick to go back.")).build())
            .enchantmentSlotsMap(ImmutableMap.<Integer, List<Integer>>builder()
                    .put(1, Collections.singletonList(22))
                    .put(2, Arrays.asList(21, 23))
                    .put(3, Arrays.asList(20, 22, 24))
                    .put(4, Arrays.asList(19, 21, 23, 25))
                    .put(5, Arrays.asList(20,21,22,23,24))
                    .build())
            .build();

    private Background getMainBackground() {
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
                .put(10, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(11, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(12, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(13, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(14, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(15, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(16, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(20, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(21, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(22, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(23, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(24, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(25, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(28, ItemBuilder.of(XMaterial.ENCHANTING_TABLE, 1, "&aEnchant Item", Arrays.asList("&7Add an item to the slot above to", "&7view enchantment options!")).build())
                .put(29, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(30, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
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
                .put(53, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .build());
    }
}

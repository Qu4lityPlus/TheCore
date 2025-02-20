package com.qualityplus.anvil.base.config;

import com.google.common.collect.ImmutableMap;
import com.qualityplus.anvil.base.gui.anvilmain.AnvilMainGUIConfig;
import com.qualityplus.assistant.inventory.CommonGUI;
import com.qualityplus.assistant.inventory.Item;
import com.qualityplus.assistant.inventory.background.Background;
import com.qualityplus.assistant.inventory.background.DefaultBackgrounds;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.CustomKey;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
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
    @CustomKey("enchantMainGUI")
    public AnvilMainGUIConfig enchantMainGUI = AnvilMainGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Anvil",
                    54,
                    getMainBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .itemToUpgradeFilledSlots(Arrays.asList(20,11,12))
            .itemToUpgradeFilledItem(ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The item you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
            .itemToUpgradeSlot(29)

            .itemToSacrificeFilledSlots(Arrays.asList(24,15,14))
            .itemToSacrificeFilledItem(ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The item you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
            .itemToSacrificeSlot(33)

            .readyToCombineSlots(Arrays.asList(45,46,47,48,50,51,52,53))
            .readyToCombineItem(ItemBuilder.of(XMaterial.LIME_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            //Info
            .combineFilledItem(ItemBuilder.of(XMaterial.ANVIL, 22, 1, "&aCombine Items", Arrays.asList("&7Combine the items in the slots", "&7to the left and right below.", "",
                    "&7Cost: %anvil_enchant_exp_cost%", "&7Cost: %anvil_enchant_money_cost%", "", "&eClick to combine!"))
                    .enchanted(true).build())

            .combinedErrorItem(ItemBuilder.of(XMaterial.BARRIER, 13, 1, "&cError", Collections.singletonList("%anvil_error%")).build())
            //Result
            .combinedItemFilled(ItemBuilder.of(XMaterial.STONE, 13, 1, "&f%anvil_result_item_displayname%", Arrays.asList("%anvil_result_item_lore%", "&8————————————————————", "&aThis is the item you will get.", "&aClick the &cANVIL BELOW &ato", "&acombine."))
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
                .put(11, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The item you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
                .put(12, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The item you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
                .put(13, ItemBuilder.of(XMaterial.BARRIER, 1, "&cAnvil", Arrays.asList("&7Place a target item in the left", "&7slot and a sacrifice item in the", "&7right slot to combine", "&7Enchantments!")).build())
                .put(14, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The item you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
                .put(15, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The item you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
                .put(16, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(19, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(20, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The item you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
                .put(21, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(22, ItemBuilder.of(XMaterial.ANVIL, 1, "&aCombine Items", Arrays.asList("&7Combine the items in the slots", "&7to the left and right below.")).build())
                .put(23, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(24, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The item you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
                .put(25, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(28, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(30, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(31, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(32, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
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
                .put(45, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(46, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(47, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(48, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(50, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(51, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(52, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(53, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .build());
    }
}

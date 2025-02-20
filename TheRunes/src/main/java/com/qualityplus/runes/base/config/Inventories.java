package com.qualityplus.runes.base.config;

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
import com.qualityplus.runes.base.gui.derune.RemoveRuneGUIConfig;
import com.qualityplus.runes.base.gui.options.AllRunesGUIConfig;
import com.qualityplus.runes.base.gui.runetable.RuneTableGUIConfig;
import com.qualityplus.runes.base.table.effects.RuneTableEffect;
import com.qualityplus.runes.base.table.effects.RuneTableEffects;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Configuration(path = "inventories.yml")
@Header("================================")
@Header("       Inventories      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class Inventories extends OkaeriConfig implements DefaultBackgrounds {

    @CustomKey("runeTableGUIConfig")
    public RuneTableGUIConfig runeTableGUIConfig = RuneTableGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Rune Table",
                    54,
                    getMainBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .itemToUpgradeFilledSlots(Arrays.asList(10,11,12,22))
            .itemToUpgradeFilledItem(ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The item you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
            .itemToUpgradeSlot(19)

            .itemToSacrificeFilledSlots(Arrays.asList(16,15,14))
            .itemToSacrificeFilledItem(ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, "&6Rune To Sacrifice", Arrays.asList("&7The rune you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
            .itemToSacrificeSlot(25)

            .readyToCombineSlots(Arrays.asList(45,46,47,48,50,51,52,53))
            .readyToCombineItem(ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .removalItem(ItemBuilder.of(XMaterial.CAULDRON, 44, 1, "&eRune Removal", Arrays.asList("&7Sometimes, simplicity is most", "&7beatiful.")).build())
            .clickToCombineRunesItem(ItemBuilder.of(XMaterial.END_PORTAL_FRAME, 13, 1, "&aAttempt to Fuse Runes",
                            Arrays.asList("&7By Providing two runes of the", "&7same type you can attempt to",
                                    "&7fuse them into a single rune and", "&7upgrade the tier.", "",
                                    "&cWARNING: if you fail to fuse", "&ctwo runes of your runes will", "&cdisolve!", "",
                                    "&7Succeed Chance: &a%rune_succeed_chance%&7%",
                                    "&7Fail Chance: &c%rune_fail_chance%&7%",
                                    "",
                                    "&eAttempt to Fuse Runes!"))
                    .enchanted(true).build())
            .clickToCombineItemAndRuneItem(ItemBuilder.of(XMaterial.END_PORTAL_FRAME, 13, 1, "&aCombine Items",
                            Arrays.asList("&7Combine the provided rune and", "&7item.",
                                    "",
                                    "&eClick to combine!"))
                    .enchanted(true).build())

            .combinedErrorItem(ItemBuilder.of(XMaterial.BARRIER, 31, 1, "&cError", Collections.singletonList("%rune_error%")).build())
            .combinedItemFilled(ItemBuilder.of(XMaterial.STONE, 31, 1, "&f%rune_result_item_displayname%", Arrays.asList("%rune_result_item_lore%", "&8————————————————————", "&aThis is the item you will get.", "&aClick the &cPORTAL FRAME", "&cABOVE &ato combine.")).build())
            .runeTableEffects(new RuneTableEffects(ImmutableMap.<Integer, List<RuneTableEffect>>builder()
                    .put(0, Arrays.asList(
                            new RuneTableEffect(10, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(16, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(45, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(53, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                    ))
                    .put(1, Arrays.asList(
                            new RuneTableEffect(11, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(15, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(46, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(52, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                    ))
                    .put(2, Arrays.asList(
                            new RuneTableEffect(12, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(14, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(47, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(51, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                            ))
                    .put(3, Arrays.asList(
                            new RuneTableEffect(22, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(48, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(50, ItemBuilder.of(XMaterial.PINK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                    ))

                    .put(4, Arrays.asList(
                            new RuneTableEffect(10, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(16, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(45, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(53, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                    ))
                    .put(5, Arrays.asList(
                            new RuneTableEffect(11, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(15, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(46, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(52, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                    ))
                    .put(6, Arrays.asList(
                            new RuneTableEffect(12, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(14, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(47, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(51, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                    ))
                    .put(7, Arrays.asList(
                            new RuneTableEffect(22, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(48, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build()),
                            new RuneTableEffect(50, ItemBuilder.of(XMaterial.MAGENTA_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                            ))
                    .build()

            ))
            .build();

    @CustomKey("removeRuneGUIConfig")
    public RemoveRuneGUIConfig removeRuneGUIConfig = RemoveRuneGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "Rune Removal",
                    54,
                    getRemovalBackground(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .itemToUpgradeFilledSlots(Arrays.asList(0,9,18,27,36,8,17,26,35,44))
            .itemToUpgradeFilledItem(ItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
            .itemToUpgradeSlot(13)
            .goBack(ItemBuilder.of(XMaterial.ARROW, 48, 1, "&aGo back", Arrays.asList("", "&e» Click to go to back!")).build())

            .clickToRemoveRune(ItemBuilder.of(XMaterial.CAULDRON, 22, 1, "&aClick to Remove Rune!",
                            Arrays.asList("&cWARNING: The rune will be lost", "&cforever!"))
                    .build())

            .successItem(ItemBuilder.of(XMaterial.GREEN_WOOL, 22, 1, "&aSuccess!", Collections.singletonList("&7Your item had its rune removed.")).build())
            .toRemoveItem(ItemBuilder.of(XMaterial.STONE, 1, "&7%to_remove_rune_item_displayname%", Collections.singletonList("%to_remove_rune_item_lore%")).build())

            .combinedErrorItem(ItemBuilder.of(XMaterial.BARRIER, 22, 1, "&cError", Arrays.asList("&7Place an item with a rune", "&7attached to it in the above", "&7slot.")).build())
            .build();

    @CustomKey("allRunesGUIConfig")
    public AllRunesGUIConfig allRunesGUIConfig = AllRunesGUIConfig.builder()
            .commonGUI(new CommonGUI(
                    "All Runes",
                    54,
                    getBackGroundFiller(),
                    ItemBuilder.of(XMaterial.BARRIER,  49, 1, "&cClose", Arrays.asList("", "&e» &7Click to close")).build()
            ))
            .runeItem(ItemBuilder.of(XMaterial.STONE,  49, 1, "%rune_displayname%", Arrays.asList("%rune_description%", "", "&e» &7Click to get")).build())
            .runeSlots(IntStream.range(0, 44).boxed().collect(Collectors.toList()))
            .nextPage(ItemBuilder.of(XMaterial.BOOK, 52, 1, "&7Next Page", Collections.emptyList()).enabled(true).build())
            .previousPage(ItemBuilder.of(XMaterial.BOOK, 46, 1, "&7Back Page", Collections.emptyList()).enabled(true).build())
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
                .put(10, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The you you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
                .put(11, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The you you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
                .put(12, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&6Item To Upgrade", Arrays.asList("&7The you you want to upgrade", "&7should be placed in the slot on", "&7this side.")).build())
                .put(13, ItemBuilder.of(XMaterial.END_PORTAL_FRAME, 1, "&aApply a Rune or Fuse Two Runes", Arrays.asList("&7Add the rune to your provided", "&7item or provide two runes to", "&7attempt to fuse them.")).build())
                .put(14, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The you you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
                .put(15, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The you you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())
                .put(16, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&6Item To Sacrifice", Arrays.asList("&7The you you are sacrificing in", "&7order to upgrade the item on the", "&7left should be placed in the", "&7slot on this side.")).build())

                .put(17, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(20, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(21, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(22, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(23, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(24, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(26, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(28, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(29, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(30, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(31, ItemBuilder.of(XMaterial.BARRIER, 1, "&cRunic Pedestal", Arrays.asList("&7Place a target item in the left", "&7slot and sacrifice rune in the", "&7right slot to add the rune's", "&7effects to the item or add two", "&7runes to attempt to fuse them.")).build())

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
                .put(45, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(46, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(47, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(48, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(50, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(51, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(52, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(53, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .build());
    }

    private Background getRemovalBackground() {
        return new Background(ImmutableMap.<Integer, Item>builder()
                .put(1, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(2, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(3, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(4, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(5, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(6, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(7, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(10, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(11, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(12, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(14, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(15, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(16, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(19, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(20, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(21, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(23, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(24, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(25, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(28, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(29, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(30, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(31, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(32, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(33, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(34, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(37, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(38, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(39, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(40, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(41, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(42, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(43, ItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(45, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(46, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(47, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(50, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(51, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(52, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(53, ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())

                .put(0, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(9, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(18, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(27, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(36, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(8, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(17, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(26, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(35, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())
                .put(44, ItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()).build())


                .put(22, ItemBuilder.of(XMaterial.BARRIER, 1, "&aRune Removal", Arrays.asList("&7Place an item with a rune", "&7attached to it in the above", "&7slot.")).build())

                .build());
    }
}

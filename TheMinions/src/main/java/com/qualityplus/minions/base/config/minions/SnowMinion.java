package com.qualityplus.minions.base.config.minions;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.util.faster.FastStack;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.assistant.util.time.HumanTime;
import com.qualityplus.minions.base.minions.entity.MinionEntityOptions;
import com.qualityplus.minions.base.minions.minion.Minion;
import com.qualityplus.minions.base.minions.minion.MinionType;
import com.qualityplus.minions.base.minions.minion.egg.MinionEgg;
import com.qualityplus.minions.base.minions.minion.layout.LayoutGUISettings;
import com.qualityplus.minions.base.minions.minion.layout.LayoutItem;
import com.qualityplus.minions.base.minions.minion.layout.LayoutType;
import com.qualityplus.minions.base.minions.minion.layout.MinionLayout;
import com.qualityplus.minions.base.minions.minion.level.MatRequirement;
import com.qualityplus.minions.base.minions.minion.level.MinionLevel;
import com.qualityplus.minions.base.minions.minion.update.MinionSettings;
import com.qualityplus.minions.base.minions.minion.update.item.ItemSettings;
import com.qualityplus.minions.base.minions.minion.update.item.UpgradeSettings;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration(path = "minions/snow_minion.yml")
@Header("================================")
@Header("       Snow Minion      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class SnowMinion extends OkaeriConfig {
    public String id = "snow_minion";
    public String displayName = "Snow Minion";
    public MinionType type = MinionType.BLOCK_BREAK;
    public MinionEntityOptions minionEntityOptions = MinionEntityOptions.builder()
            .particle("")
            .displayName("&bSnow Minion")
            .build();
    public MinionLayout minionLayout = MinionLayout.builder()
            .type(LayoutType.TWO_X_TWO)
            .toReplaceBlock(XMaterial.SNOW_BLOCK)
            .materialThatCauseException(Collections.emptyList())
            .allMaterialsCauseExceptionExcept(Collections.singletonList(XMaterial.SNOW_BLOCK))
            .build();
    public MinionEgg minionEgg = MinionEgg.builder()
            .displayName("Snow")
            .eggDisplayName("%minion_egg_displayname% Minion %minion_level_roman%")
            .build();
    public Map<Integer, MinionLevel> minionLevels = getMinionLevels();
    public List<String> description = Arrays.asList("&7Place this minion and it will", "&7start generating and breaking", "&7snow blocks! Requires an open",
            "&7arena to place snow blocks.", "&7Minions also work when you are", "&7offline!");

    public LayoutGUISettings layoutGUISettings = LayoutGUISettings.builder()
            .minionSlot(22)
            .layoutItems(Collections.singletonList(LayoutItem.builder()
                    .slots(Arrays.asList(2,3,4,5,6,11,12,13,14,15,20,21,23,24,29,30,31,32,33,38,39,40,41,42))
                    .item(ItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "&fAir", Arrays.asList("&7Air should be 1 layer underneath", "&7where the minion is standing.")).build())
                    .build()))
            .build();
    public MinionSettings minionSettings = MinionSettings.builder()
            .baseItem(ItemSettings.builder()
                    .itemsToGive(Collections.singletonList(FastStack.fast(XMaterial.SNOWBALL, 1)))
                    .requiredItemsToCreate(Collections.emptyMap())
                    .build())
            .upgradeSettings(FastMap.builder(String.class, UpgradeSettings.class)
                    .put("super_compactor_3000", UpgradeSettings.builder()
                            .sellPrice(20)
                            .itemSettings(ItemSettings.builder()
                                    .itemsToGive(Collections.singletonList(FastStack.fast(XMaterial.SNOW_BLOCK, 1)))
                                    .requiredItemsToCreate(FastMap.builder(Integer.class, ItemStack.class)
                                            .put(2, FastStack.fast(XMaterial.SNOWBALL, 1))
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();


    public Minion getMinion() {
        return Minion.builder()
                .id(id)
                .displayName(displayName)
                .type(type)
                .minionEntityOptions(minionEntityOptions)
                .minionLayout(minionLayout)
                .minionEgg(minionEgg)
                .minionLevels(minionLevels)
                .minionUpdateSettings(minionSettings)
                .description(description)
                .layoutGUISettings(layoutGUISettings)
                .minionConfig(this)
                .build();
    }

    private Map<Integer, MinionLevel> getMinionLevels() {
        return FastMap.builder(Integer.class, MinionLevel.class)
                .put(1, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(64)
                                .build())
                        .executeActionsTime(new HumanTime(125, HumanTime.TimeType.SECONDS))
                        .maxStorage(1)
                        .minionSkin("snow_minion_1")
                        .build())
                .put(2, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(120, HumanTime.TimeType.SECONDS))
                        .maxStorage(2)
                        .minionSkin("snow_minion_2")
                        .build())
                .put(3, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(115, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_3")
                        .build())
                .put(4, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(110, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_4")
                        .build())
                .put(5, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(105, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_5")
                        .build())
                .put(6, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(100, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_6")
                        .build())
                .put(7, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(90, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_7")
                        .build())
                .put(8, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(85, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_8")
                        .build())
                .put(9, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(80, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_9")
                        .build())
                .put(10, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(75, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_10")
                        .build())
                .put(11, MinionLevel.builder()
                        .matRequirement(MatRequirement.builder()
                                .requiredMaterial(XMaterial.SNOWBALL)
                                .requiredMaterialAmount(128)
                                .build())
                        .executeActionsTime(new HumanTime(65, HumanTime.TimeType.SECONDS))
                        .maxStorage(3)
                        .minionSkin("snow_minion_11")
                        .build())
                .build();
    }
}

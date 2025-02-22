package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XPotion;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.BrewChancePerk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Configuration(path = "perks/brew_chance_perk.yml")
@Header("================================")
@Header("       Brew Chance Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class BrewerChanceConfig extends OkaeriConfig implements PerkFile {

    public String id = "brew_chance";
    public boolean enabled = true;
    public String displayName = "Brew Chance";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to get extra potion", "  &7effect when consume a potion", "  &7for &a%duration% &7seconds.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(10)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ5MmNhOTQwNzkxMzZkMjUyNTcwM2QzNzVjMjU1N2VhYzIwMWVlN2RkMzljZTExYzY0YTljMzgxNDdlY2M0ZCJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_brew_chance_description%"))
            .build();
    public Perk getPerk() {
        return BrewChancePerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(1)
                .secondsDurationPerLevel(1)
                .baseSecondsDuration(1)
                .potionAndChances(ImmutableMap.<XPotion, Double>builder()
                        .put(XPotion.ABSORPTION, 2D)
                        .put(XPotion.CONFUSION, 4D)
                        //.put(XPotion.NAUSEA, 4D)
                        .put(XPotion.CONDUIT_POWER, 5D)
                        .build())
                .baseAmount(0)
                .build();
    }
}

package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.EagleEyesPerk;
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
@Configuration(path = "perks/eagle_eyes_perk.yml")
@Header("================================")
@Header("       Eagle Eyes Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class EagleEyesConfig extends OkaeriConfig implements PerkFile {
    public String id = "eagle_eyes";
    public boolean enabled = true;
    public String displayName = "Eagle Eyes";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to get Night Vision %potion_level_roman%", "  &7for &a%duration% &7seconds when receive", "  &7damage.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(12)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRjNTc1YzhiM2ExOTVmM2Q5YmU3YWE0ZTg4NDllNGMyNWZjZGE0YTM0N2I2YmVmZDA2ZGQ4NmY5ODhjYjY5NiJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_eagle_eyes_description%"))
            .build();
    public Perk getPerk() {
        return EagleEyesPerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(0.1)
                .baseSecondsDuration(0)
                .secondsDurationPerLevel(1)
                .level(1)
                .initialAmount(0)
                .build();
    }
}

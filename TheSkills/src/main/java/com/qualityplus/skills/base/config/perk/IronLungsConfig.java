package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.IronLungsPerk;
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
@Configuration(path = "perks/iron_lungs_perk.yml")
@Header("================================")
@Header("       Iron Lungs Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class IronLungsConfig extends OkaeriConfig implements PerkFile {
    public String id = "iron_lungs";
    public boolean enabled = true;
    public String displayName = "Iron Lungs";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to get Water Breathing %potion_level_roman%", "  &7for &a%duration% &7seconds while be in the water.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(15)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTczMTFiMWZjNzdmM2I3ZWQxYmNlNjVlM2ZiMjRkNzI2YTgzY2EyMTIzZGI0MzNiZWMxYzNmMzBhZTdmMThjNiJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_iron_lungs_description%"))
            .build();
    public Perk getPerk() {
        return IronLungsPerk.builder()
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

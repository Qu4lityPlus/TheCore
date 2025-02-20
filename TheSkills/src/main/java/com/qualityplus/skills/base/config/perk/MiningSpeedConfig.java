package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.MiningSpeedPerk;
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
@Configuration(path = "perks/mining_speed_perk.yml")
@Header("================================")
@Header("       Mining Speed Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class MiningSpeedConfig extends OkaeriConfig implements PerkFile {
    public String id = "mining_speed";
    public boolean enabled = true;
    public String displayName = "Mining Speed";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to get Haste for", "  &7&a%duration% &7seconds when mining ores.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(22)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE4YzY4NTE0ZDhjODE5ZTY4ZWI3NWI1OTY0NDVkODhiMzY3YWUzNTFiYWE5OTgzYmM2OWNmNmI1MjBmMzk4NSJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_mining_speed_description%"))
            .build();
    public Perk getPerk() {
        return MiningSpeedPerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(0.1)
                .baseSecondsDuration(0)
                .secondsDurationPerLevel(1)
                .initialAmount(0)
                .build();
    }
}

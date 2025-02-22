package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.SeaFortunePerk;
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
@Configuration(path = "perks/sea_fortune_perk.yml")
@Header("================================")
@Header("       Sea Fortune Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class SeaFortuneConfig extends OkaeriConfig implements PerkFile {
    public String id = "sea_fortune";
    public boolean enabled = true;
    public String displayName = "Sea Lucky Chance";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to get &a%multiplier%x &7drops", "  &7when fishing.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(30)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxNTU3YjcwMGRmNTI0ZTA3ODYwYzc0NjNlYWNhOTQ1MTViYWI3ZTRiMWQzM2UzOWJkMjg5NmFkY2IwZWQ5MCJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_sea_fortune_description%"))
            .build();
    public Perk getPerk() {
        return SeaFortunePerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .initialAmount(0)
                .build();
    }
}

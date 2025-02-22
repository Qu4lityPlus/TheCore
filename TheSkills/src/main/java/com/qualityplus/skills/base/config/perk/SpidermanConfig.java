package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.SpidermanPerk;
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
@Configuration(path = "perks/spiderman_perk.yml")
@Header("================================")
@Header("       Spiderman Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class SpidermanConfig extends OkaeriConfig implements PerkFile {
    public String id = "spiderman";
    public boolean enabled = true;
    public String displayName = "Spiderman";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to trap mobs in", "  &7a web for &a%duration% &7seconds while", "  &7in combat.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(31)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU5NzZmZTMyZmM1ZDEwNTg5ZGZhODhkMDM2M2FkYmM5ODJlYjM1ZjhjZmNkMGUwYzg5M2E2MDk5NjY1YTg3NyJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_spiderman_description%"))
            .build();
    public Perk getPerk() {
        return SpidermanPerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(0.1)
                .secondsDuration(2)
                .canBeUsedWithPlayers(false)
                .enabledWorlds(Arrays.asList("world", "world_nether", "world_the_end"))
                .build();
    }


}

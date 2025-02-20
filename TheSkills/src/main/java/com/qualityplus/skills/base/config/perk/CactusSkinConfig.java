package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.CactusSkinPerk;
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
@Configuration(path = "perks/cactus_skin_perk.yml")
@Header("================================")
@Header("       Cactus Skin Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class CactusSkinConfig extends OkaeriConfig implements PerkFile {
    public String id = "cactus_skin";
    public boolean enabled = true;
    public String displayName = "Cactus Skin";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance deal &a+%damage% &7to enemies", "  &7when they punch you.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(11)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjEyZGUzYzE5NTQyNDczYjdiMzQ0MWRmZTdkZDM2MGQyNDJlZDMwZGE3NmI0ODUzZDhhMjQxN2I4NTUyM2ZmYyJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_eagle_eyes_description%"))
            .build();
    public Perk getPerk() {
        return CactusSkinPerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(0.1)
                .damageBase(0)
                .damagePerLevel(0.2)
                .initialAmount(0)
                .build();
    }
}

package com.qualityplus.skills.base.config.perk;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.perk.perks.RefurbishedPerk;
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
@Configuration(path = "perks/refurbished_perk.yml")
@Header("================================")
@Header("       Refurbished Perk      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class RefurbishedConfig extends OkaeriConfig implements PerkFile {
    public String id = "refurbished";
    public boolean enabled = true;
    public String displayName = "Refurbished";
    public List<String> description = Arrays.asList("  &a%percent%% &7chance to refurbish your", "  &7pickaxes restoring their max", "  &7durability while breaking blocks.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(29)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNiZDlmNWVjMWVkMDA3MjU5OTk2NDkxZTY5ZmY2NDlhMzEwNmNmOTIwMjI3YjFiYjNhNzFlZTdhODk4NjNmIn19fQ==")
            .mainMenuLore(Collections.singletonList("%skill_refurbished_description%"))
            .build();
    public Perk getPerk() {
        return RefurbishedPerk.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(0.1)
                .toolList(Arrays.asList(
                        XMaterial.DIAMOND_PICKAXE,
                        XMaterial.IRON_PICKAXE,
                        XMaterial.GOLDEN_PICKAXE
                ))
                .build();
    }
}

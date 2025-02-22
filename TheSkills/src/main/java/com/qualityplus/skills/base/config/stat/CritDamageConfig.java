package com.qualityplus.skills.base.config.stat;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.stats.CritDamageStat;
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
@Configuration(path = "stats/critic_damage_stat.yml")
@Header("================================")
@Header("       Critic Damage Stat      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class CritDamageConfig extends OkaeriConfig implements StatFile {
    public String id = "critic_damage";
    public boolean enabled = true;
    public String displayName = "&9☠ Critic Damage";
    public List<String> description = Arrays.asList("&a%chance%% &7of extra damage you", "&7deal when landing critical hit...");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(11)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGRhZmIyM2VmYzU3ZjI1MTg3OGU1MzI4ZDExY2IwZWVmODdiNzljODdiMjU0YTdlYzcyMjk2ZjkzNjNlZjdjIn19fQ==")
            .mainMenuLore(Collections.singletonList("%skill_critic_damage_description%"))
            .build();
    public Stat getStat() {
        return CritDamageStat.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .damagePercentagePerLevel(2)
                .baseAmount(0)
                .build();
    }
}

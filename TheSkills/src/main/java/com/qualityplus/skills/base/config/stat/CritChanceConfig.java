package com.qualityplus.skills.base.config.stat;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.stats.CritChanceStat;
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
@Configuration(path = "stats/critic_chance_stat.yml")
@Header("================================")
@Header("       Critic Chance Stat      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class CritChanceConfig extends OkaeriConfig implements StatFile {
    public String id = "critic_chance";
    public boolean enabled = true;
    public String displayName = "&9☣ Critic Chance";
    public List<String> description = Arrays.asList("&a%chance%% &7chance to deal extra damage", "&7on enemies.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(10)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U0ZjQ5NTM1YTI3NmFhY2M0ZGM4NDEzM2JmZTgxYmU1ZjJhNDc5OWE0YzA0ZDlhNGRkYjcyZDgxOWVjMmIyYiJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_critic_chance_description%"))
            .build();
    public Stat getStat() {
        return CritChanceStat.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(2)
                .baseAmount(0)
                .build();
    }
}

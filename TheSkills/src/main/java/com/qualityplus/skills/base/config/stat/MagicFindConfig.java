package com.qualityplus.skills.base.config.stat;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.base.stat.stats.MagicFindStat;
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
@Configuration(path = "stats/magic_find_stat.yml")
@Header("================================")
@Header("       Magic Find Stat      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class MagicFindConfig extends OkaeriConfig implements StatFile {
    public String id = "magic_find";
    public boolean enabled = true;
    public String displayName = "&b✬ Magic Find";
    public List<String> description = Arrays.asList("&7With &a%level_number% &7Magic Find you have &a%chance%% &7chance", "&7to find magic objects when killing mobs.");
    public int maxLevel = 50;
    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(15)
            .page(1)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2VkNTE5OGRkY2RkODliZTkxYzY5ZmU5YWJmZTJjYTRjMTk0N2M3ZTJlYWMxMWYxODQ2YmQzMTIyY2E1YjhjNiJ9fX0=")
            .mainMenuLore(Collections.singletonList("%skill_intelligence_description%"))
            .build();
    public Stat getStat() {
        return MagicFindStat.builder()
                .id(id)
                .displayName(displayName)
                .description(description)
                .enabled(enabled)
                .skillGUIOptions(guiOptions)
                .chancePerLevel(0.1)
                .itemAndChances(ImmutableMap.<XMaterial, Double>builder()
                        .put(XMaterial.DIAMOND_BLOCK, 2D)
                        .build())
                .baseAmount(0)
                .build();
    }
}

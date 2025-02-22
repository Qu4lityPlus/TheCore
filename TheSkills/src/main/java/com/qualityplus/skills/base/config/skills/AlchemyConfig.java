package com.qualityplus.skills.base.config.skills;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.skills.AlchemySkill;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Configuration(path = "skills/alchemy_skill.yml")
@Header("================================")
@Header("       Alchemy      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class AlchemyConfig extends OkaeriConfig {
    public String id = "alchemy";
    public boolean enabled = true;
    public String displayName = "Alchemy";
    public List<String> description = Collections.singletonList("&7Brew Potions to earn alchemy xp!");
    public int maxLevel = 50;
    private Map<Integer, Double> xpRequirements = getLevelsMap();
    private Map<Integer, List<String>> skillInfoInGUI = getInfo();
    private Map<Integer, List<StatReward>> statRewards = getInternalRewards();
    private Map<Integer, List<String>> skillInfoInMessage = getInfo();
    private Map<Integer, List<CommandReward>> commandRewards = new HashMap<>();
    private Map<XMaterial, Double> rewards = ImmutableMap.<XMaterial, Double>builder()
            .put(XMaterial.GHAST_TEAR, 5D)
            .put(XMaterial.GLOWSTONE_DUST, 5D)
            .put(XMaterial.FERMENTED_SPIDER_EYE, 5D)
            .put(XMaterial.GUNPOWDER, 5D)
            .put(XMaterial.REDSTONE, 5D)
            .put(XMaterial.NETHER_WART, 5D)
            .put(XMaterial.PUFFERFISH, 5D)
            .put(XMaterial.DRAGON_BREATH, 5D)
            .put(XMaterial.SUGAR, 5D)
            .put(XMaterial.RABBIT_FOOT, 5D)
            .put(XMaterial.MAGMA_BLOCK, 5D)
            .put(XMaterial.SPIDER_EYE, 5D)
            .put(XMaterial.PHANTOM_MEMBRANE, 5D)
            .build();

    public GUIOptions guiOptions = GUIOptions.builder()
            .slot(19)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ5MmNhOTQwNzkxMzZkMjUyNTcwM2QzNzVjMjU1N2VhYzIwMWVlN2RkMzljZTExYzY0YTljMzgxNDdlY2M0ZCJ9fX0=")
            .mainMenuLore(Arrays.asList("&7Abilities To Upgrade:",
                    "&8» %skill_defense_displayname%",
                    "&8» %skill_ferocity_displayname%",
                    "",
                    "&7Your Stats:",
                    "&8» &r&6%skill_brew_chance_displayname%",
                    "   &7%skill_brew_chance_description%",
                    "&8» &r&6%skill_potion_master_displayname%",
                    "   &7%skill_potion_master_description%"
            ))
            .build();

    public Skill getSkill() {
        return AlchemySkill.builder()
                .id(id)
                .enabled(enabled)
                .displayName(displayName)
                .description(description)
                .xpRequirements(xpRequirements)
                .skillInfoInGUI(skillInfoInGUI)
                .statRewards(statRewards)
                .skillInfoInMessage(skillInfoInMessage)
                .skillInfoInGUI(skillInfoInGUI)
                .commandRewards(commandRewards)
                .maxLevel(maxLevel)
                .skillGUIOptions(guiOptions)
                .rewards(rewards)
                .build();
    }

    private Map<Integer, List<String>> getInfo() {
        return FastMap.listBuilder(Integer.class, String.class)
                .put(1, Arrays.asList("&7Abilities To Upgrade:",
                                "&8» &f+1 %skill_defense_displayname%",
                                "&8» &f+1 %skill_ferocity_displayname%",
                                "",
                                "&7Your Stats:",
                                "&8» &r&6%skill_brew_chance_displayname%",
                                "   &7%skill_brew_chance_description%",
                                "&8» &r&6%skill_potion_master_displayname%",
                                "   &7%skill_potion_master_description%"))
                .put(10, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+2 %skill_defense_displayname%",
                        "&8» &f+2 %skill_ferocity_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_brew_chance_displayname%",
                        "   &7%skill_brew_chance_description%",
                        "&8» &r&6%skill_potion_master_displayname%",
                        "   &7%skill_potion_master_description%"))
                .build();
    }

    private Map<Integer, List<StatReward>> getInternalRewards() {
        return FastMap.listBuilder(Integer.class, StatReward.class)
                .put(1, Arrays.asList(new StatReward("defense", 1), new StatReward("ferocity", 1), new StatReward("brew_chance", 1), new StatReward("potion_master", 1)))
                .put(10, Arrays.asList(new StatReward("defense", 2), new StatReward("ferocity", 2), new StatReward("brew_chance", 1), new StatReward("potion_master", 1)))
                .build();
    }

    private Map<Integer, Double> getLevelsMap() {
        Map<Integer, Double> levels = new HashMap<>();

        NumberUtil.intStream(0, maxLevel).forEach(n -> levels.put(n, n*15d));

        return levels;
    }
}

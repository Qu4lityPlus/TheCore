package com.qualityplus.skills.base.config.skills;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.skills.ForagingSkill;
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
@Configuration(path = "skills/foraging_skill.yml")
@Header("================================")
@Header("       Foraging      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class ForagingConfig extends OkaeriConfig {
    public String id = "foraging";
    public boolean enabled = true;
    public String displayName = "Foraging";
    public List<String> description = Collections.singletonList("&7Cut wood to earn foraging xp!");
    public int maxLevel = 50;
    private Map<Integer, Double> xpRequirements = getLevelsMap();
    private Map<Integer, List<String>> skillInfoInGUI = getInfo();
    private Map<Integer, List<StatReward>> statRewards = getInternalRewards();
    private Map<Integer, List<String>> skillInfoInMessage = getInfo();
    private Map<Integer, List<CommandReward>> commandRewards = new HashMap<>();
    private Map<XMaterial, Double> rewards = ImmutableMap.<XMaterial, Double>builder()
                        .put(XMaterial.OAK_LOG, 2D)
                        .put(XMaterial.ACACIA_LOG, 2D)
                        .put(XMaterial.BIRCH_LOG, 2D)
                        .put(XMaterial.DARK_OAK_LOG, 2D)
                        .put(XMaterial.JUNGLE_LOG, 2D)
                        .put(XMaterial.SPRUCE_LOG, 2D)
                        .build();
    private Map<XMaterial, Double> minionXpRewards = ImmutableMap.<XMaterial, Double>builder()
            .put(XMaterial.OAK_LOG, 2D)
            .put(XMaterial.ACACIA_LOG, 2D)
            .put(XMaterial.BIRCH_LOG, 2D)
            .put(XMaterial.DARK_OAK_LOG, 2D)
            .put(XMaterial.JUNGLE_LOG, 2D)
            .put(XMaterial.SPRUCE_LOG, 2D)
            .build();

    private GUIOptions guiOptions = GUIOptions.builder()
            .slot(29)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0OTc3ODZkN2JhMTkyMzY5OTMyOTY2Njg2YWE5ZmI3MTQ3ZmE1ODg0ZjlhNjIwOWM0YTczZTJkNjBmMzlmNSJ9fX0=")
            .mainMenuLore(Arrays.asList("&7Abilities To Upgrade:",
                    "&8» %skill_strength_displayname%",
                    "&8» %skill_defense_displayname%",
                    "",
                    "&7Your Stats:",
                    "&8» &r&6%skill_foraging_fortune_displayname%",
                    "   &7%skill_foraging_fortune_description%",
                    "&8» &r&6%skill_medicine_man_displayname%",
                    "   &7%skill_medicine_man_description%"))
            .build();

    public Skill getSkill() {
        return ForagingSkill.builder()
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
                .minionXpRewards(minionXpRewards)
                .build();
    }

    private Map<Integer, List<String>> getInfo() {
        return FastMap.listBuilder(Integer.class, String.class)
                .put(1, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+1 %skill_strength_displayname%",
                        "&8» &f+1 %skill_defense_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_foraging_fortune_displayname%",
                        "   &7%skill_foraging_fortune_description%",
                        "&8» &r&6%skill_medicine_man_displayname%",
                        "   &7%skill_medicine_man_description%"))
                .put(10, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+2 %skill_strength_displayname%",
                        "&8» &f+2 %skill_defense_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_foraging_fortune_displayname%",
                        "   &7%skill_foraging_fortune_description%",
                        "&8» &r&6%skill_medicine_man_displayname%",
                        "   &7%skill_medicine_man_description%"))
                .build();
    }

    private Map<Integer, List<StatReward>> getInternalRewards() {
        return FastMap.listBuilder(Integer.class, StatReward.class)
                .put(1, Arrays.asList(new StatReward("strength", 1), new StatReward("defense", 1), new StatReward("foraging_fortune", 1), new StatReward("medicine_man", 1)))
                .put(10, Arrays.asList(new StatReward("strength", 2), new StatReward("defense", 2), new StatReward("foraging_fortune", 1), new StatReward("medicine_man", 1)))
                .build();
    }

    private Map<Integer, Double> getLevelsMap() {
        Map<Integer, Double> levels = new HashMap<>();

        NumberUtil.intStream(0, maxLevel).forEach(n -> levels.put(n, n*15d));

        return levels;
    }

}

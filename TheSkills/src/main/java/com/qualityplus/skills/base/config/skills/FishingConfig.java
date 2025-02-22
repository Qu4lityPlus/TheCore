package com.qualityplus.skills.base.config.skills;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.skills.FishingSkill;
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
@Configuration(path = "skills/fishing_skill.yml")
@Header("================================")
@Header("       Fishing      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class FishingConfig extends OkaeriConfig {

    public String id = "fishing";
    public boolean enabled = true;
    public String displayName = "Fishing";
    public List<String> description = Collections.singletonList("&7Earn xp by fishing!");
    public int maxLevel = 50;
    private Map<Integer, Double> xpRequirements = getLevelsMap();
    private Map<Integer, List<String>> skillInfoInGUI = getInfo();
    private Map<Integer, List<StatReward>> statRewards = getInternalRewards();
    private Map<Integer, List<String>> skillInfoInMessage = getInfo();
    private Map<Integer, List<CommandReward>> commandRewards = new HashMap<>();

    private GUIOptions guiOptions = GUIOptions.builder()
            .slot(25)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJhZDNjYzZkMzYzMWNhYTg4N2E5MWViYzVlNmE2NWNmMjU3ODAzYzdjN2FjZDU3ZDE5YTBhYzIyZmFlODQwMyJ9fX0=")
            .mainMenuLore(Arrays.asList("&7Abilities To Upgrade:",
                    "&8» %skill_speed_displayname%",
                    "&8» %skill_magic_find_displayname%",
                    "",
                    "&7Your Stats:",
                    "&8» &r&6%skill_sea_fortune_displayname%",
                    "   &7%skill_sea_fortune_description%",
                    "&8» &r&6%skill_iron_lungs_displayname%",
                    "   &7%skill_iron_lungs_description%"))
            .build();

    public Skill getSkill() {
        return FishingSkill.builder()
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
                .rewards(ImmutableMap.<String, Double>builder()
                        .put("COD", 2D)
                        .build())
                .rewardsForAllCaught(2D)
                .build();
    }


    private Map<Integer, List<String>> getInfo() {
        return FastMap.listBuilder(Integer.class, String.class)
                .put(1, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+1 %skill_speed_displayname%",
                        "&8» &f+1 %skill_magic_find_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_sea_fortune_displayname%",
                        "   &7%skill_sea_fortune_description%",
                        "&8» &r&6%skill_iron_lungs_displayname%",
                        "   &7%skill_iron_lungs_description%"))
                .put(10, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+2 %skill_speed_displayname%",
                        "&8» &f+2 %skill_magic_find_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_sea_fortune_displayname%",
                        "   &7%skill_sea_fortune_description%",
                        "&8» &r&6%skill_iron_lungs_displayname%",
                        "   &7%skill_iron_lungs_description%"))
                .build();
    }

    private Map<Integer, List<StatReward>> getInternalRewards() {
        return FastMap.listBuilder(Integer.class, StatReward.class)
                .put(1, Arrays.asList(new StatReward("speed", 1), new StatReward("magic_find", 1), new StatReward("fishing_fortune", 1), new StatReward("iron_lungs", 1)))
                .put(10, Arrays.asList(new StatReward("speed", 2), new StatReward("magic_find", 2), new StatReward("fishing_fortune", 1), new StatReward("iron_lungs", 1)))
                .build();
    }

    private Map<Integer, Double> getLevelsMap() {
        Map<Integer, Double> levels = new HashMap<>();

        NumberUtil.intStream(0, maxLevel).forEach(n -> levels.put(n, n*15d));

        return levels;
    }
}

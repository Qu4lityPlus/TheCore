package com.qualityplus.skills.base.config.skills;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.skills.CombatSkill;
import com.qualityplus.assistant.lib.eu.okaeri.configs.OkaeriConfig;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Header;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameModifier;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.NameStrategy;
import com.qualityplus.assistant.lib.eu.okaeri.configs.annotation.Names;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;

import java.util.*;

@Getter
@Setter
@Configuration(path = "skills/combat_skill.yml")
@Header("================================")
@Header("       Combat      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class CombatConfig extends OkaeriConfig {
    public String id = "combat";
    public boolean enabled = true;
    public String displayName = "Combat";
    public List<String> description = Collections.singletonList("&7Earn xp by killing mobs!");
    public int maxLevel = 50;
    private Map<Integer, Double> xpRequirements = getLevelsMap();
    private Map<Integer, List<String>> skillInfoInGUI = getInfo();
    private Map<Integer, List<StatReward>> statRewards = getInternalRewards();
    private Map<Integer, List<String>> skillInfoInMessage = getInfo();
    private Map<Integer, List<CommandReward>> commandRewards = new HashMap<>();
    private Map<EntityType, Double> rewards = ImmutableMap.<EntityType, Double>builder()
            .put(EntityType.ZOMBIE, 2D)
            .put(EntityType.CREEPER, 3D)
            .put(EntityType.ENDERMAN, 4D)
            .build();
    private GUIOptions guiOptions = GUIOptions.builder()
            .slot(21)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ1MjhiMzIyOTY2MGYzZGZhYjQyNDE0ZjU5ZWU4ZmQwMWU4MDA4MWRkM2RmMzA4Njk1MzZiYTliNDE0ZTA4OSJ9fX0=")
            .mainMenuLore(Arrays.asList("&7Abilities To Upgrade:",
                    "&8» %skill_critic_damage_displayname%",
                    "&8» %skill_critic_chance_displayname%",
                    "",
                    "&7Your Stats:",
                    "&8» &r&6%skill_one_punch_man_displayname%",
                    "   &7%skill_one_punch_man_description%",
                    "&8» &r&6%skill_spiderman_displayname%",
                    "   &7%skill_spiderman_description%"))
            .build();

    public Skill getSkill() {
        return CombatSkill.builder()
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
                        "&8» &f+1 %skill_critic_damage_displayname%",
                        "&8» &f+1 %skill_critic_chance_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_one_punch_man_displayname%",
                        "   &7%skill_one_punch_man_description%",
                        "&8» &r&6%skill_spiderman_displayname%",
                        "   &7%skill_spiderman_description%"))
                .put(10, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+2 %skill_critic_damage_displayname%",
                        "&8» &f+2 %skill_critic_chance_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_one_punch_man_displayname%",
                        "   &7%skill_one_punch_man_description%",
                        "&8» &r&6%skill_spiderman_displayname%",
                        "   &7%skill_spiderman_description%"))
                .build();
    }

    private Map<Integer, List<StatReward>> getInternalRewards() {
        return FastMap.listBuilder(Integer.class, StatReward.class)
                .put(1, Arrays.asList(new StatReward("critic_damage", 1), new StatReward("critic_chance", 1), new StatReward("one_punch_man", 1), new StatReward("spiderman", 1)))
                .put(10, Arrays.asList(new StatReward("critic_damage", 2), new StatReward("critic_chance", 2), new StatReward("one_punch_man", 1), new StatReward("spiderman", 1)))
                .build();
    }

    private Map<Integer, Double> getLevelsMap() {
        Map<Integer, Double> levels = new HashMap<>();

        NumberUtil.intStream(0, maxLevel).forEach(n -> levels.put(n, n*15d));

        return levels;
    }
}

package com.qualityplus.skills.base.config.skills;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.util.faster.FastMap;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.skills.DungeoneeringSkill;
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
@Configuration(path = "skills/dungeoneering_skill.yml")
@Header("================================")
@Header("       Dungeoneering      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class DungeoneeringConfig extends OkaeriConfig {

    public String id = "dungeoneering";
    public boolean enabled = true;
    public String displayName = "Dungeoneering";
    public List<String> description = Collections.singletonList("&7Earn xp by killing bosses!");
    public int maxLevel = 50;
    private Map<Integer, Double> xpRequirements = getLevelsMap();
    private Map<Integer, List<String>> skillInfoInGUI = getInfo();
    private Map<Integer, List<StatReward>> statRewards = getInternalRewards();
    private Map<Integer, List<String>> skillInfoInMessage = getInfo();
    private Map<Integer, List<CommandReward>> commandRewards = new HashMap<>();

    private GUIOptions guiOptions = GUIOptions.builder()
            .slot(32)
            .item(XMaterial.PLAYER_HEAD)
            .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA1MGRlNTcyY2Y4YmYxNDk2YjUyNzg1YWQzNDlkMDJhY2RkYTY0NDc5YjFiZTc1MDkzZTlhMWY3OTI4ZGQyIn19fQ=")
            .mainMenuLore(Arrays.asList("&7Abilities To Upgrade:",
                    "&8» %skill_speed_displayname%",
                    "&8» %skill_ferocity_displayname%",
                    "",
                    "&7Your Stats:",
                    "&8» &r&6%skill_steel_skin_displayname%",
                    "   &7%skill_steel_skin_description%",
                    "&8» &r&6%skill_wizard_displayname%",
                    "   &7%skill_wizard_description%"
            ))
            .build();

    public Skill getSkill() {
        return DungeoneeringSkill.builder()
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
                .rewardsByName(new HashMap<>())
                .mythicMobRewards(new HashMap<String, Double>() {{
                    put("mythicMobIdExample", 10D);
                    put("mythicMobIdExample2", 20D);

                }})
                .build();
    }

    private Map<Integer, List<String>> getInfo() {
        return FastMap.listBuilder(Integer.class, String.class)
                .put(1, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+1 %skill_speed_displayname%",
                        "&8» &f+1 %skill_ferocity_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_steel_skin_displayname%",
                        "   &7%skill_steel_skin_description%",
                        "&8» &r&6%skill_wizard_displayname%",
                        "   &7%skill_wizard_description%"))
                .put(10, Arrays.asList("&7Abilities To Upgrade:",
                        "&8» &f+2 %skill_speed_displayname%",
                        "&8» &f+2 %skill_ferocity_displayname%",
                        "",
                        "&7Your Stats:",
                        "&8» &r&6%skill_steel_skin_displayname%",
                        "   &7%skill_steel_skin_description%",
                        "&8» &r&6%skill_wizard_displayname%",
                        "   &7%skill_wizard_description%"))
                .build();
    }

    private Map<Integer, List<StatReward>> getInternalRewards() {
        return new HashMap<>();
    }

    private Map<Integer, Double> getLevelsMap() {
        Map<Integer, Double> levels = new HashMap<>();

        NumberUtil.intStream(0, maxLevel).forEach(n -> levels.put(n, n*15d));

        return levels;
    }

}

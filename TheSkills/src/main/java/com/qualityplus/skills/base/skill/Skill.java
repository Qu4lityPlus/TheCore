package com.qualityplus.skills.base.skill;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.skills.api.effect.CommonObject;
import com.qualityplus.skills.api.listener.ExtraListener;
import com.qualityplus.skills.api.registry.ListenerRegistrable;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.skill.registry.Skills;
import com.qualityplus.skills.base.skill.skills.blockbreak.BlockBreakResponse;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import lombok.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class Skill extends CommonObject implements ListenerRegistrable {
    private int maxLevel;
    private Map<Integer, Double> xpRequirements;
    private Map<Integer, List<String>> skillInfoInGUI;
    private Map<Integer, List<StatReward>> statRewards;
    private Map<Integer, List<String>> skillInfoInMessage;
    private Map<Integer, List<CommandReward>> commandRewards;

    public Skill(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, int maxLevel,
                 Map<Integer, Double> xpRequirements, Map<Integer, List<String>> skillInfoInGUI, Map<Integer, List<StatReward>> statRewards,
                 Map<Integer, List<String>> skillInfoInMessage, Map<Integer, List<CommandReward>> commandRewards) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount);
        this.maxLevel = maxLevel;
        this.xpRequirements = xpRequirements;
        this.skillInfoInGUI = skillInfoInGUI;
        this.statRewards = statRewards;
        this.skillInfoInMessage = skillInfoInMessage;
        this.commandRewards = commandRewards;
    }

    @Override
    public void register() {
        Skills.registerNewSkill(this);
    }

    @Override
    public void addExtraListener(Class<? extends ExtraListener> listener) {
        extraListeners.add(listener);
    }

    private <T> T getMap(Map<Integer, T> map, int level) {
        if (map.containsKey(level)) {
            return map.get(level);
        } else {
            int highestLevel = 1;

            for (Integer startLevel : map.keySet()) {
                if (startLevel > level)
                    break;

                if (startLevel > highestLevel)
                    highestLevel = startLevel;
            }

            return map.getOrDefault(highestLevel, null);
        }
    }

    public List<String> getCachedMessage(int level) {
        return getMap(skillInfoInMessage, level);
    }

    public List<String> getCachedGUI(int level) {
        return getMap(skillInfoInGUI, level);

    }

    public List<CommandReward> getCommandRewards(int level) {
        return Optional.ofNullable(getMap(commandRewards, level)).orElse(Collections.emptyList());
    }

    public List<StatReward> getStatRewards(int level) {
        return Optional.ofNullable(getMap(statRewards, level)).orElse(Collections.emptyList());
    }

    public double getLevelRequirement(int level) {
        return getMap(xpRequirements, level);
    }

    public Optional<BlockBreakResponse> getBlockBreakEventXp(final XMaterial material, final Map<XMaterial, Double> rewards) {
        double xp = rewards.getOrDefault(material, 0D);

        if (xp <= 0) return Optional.empty();

        return Optional.of(BlockBreakResponse.builder()
                        .xp(xp)
                        .build());
    }


}

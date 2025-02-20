package com.qualityplus.skills.base.skill.skills;

import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.base.event.PlayerKillEvent;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public final class CombatSkill extends Skill {
    private Map<EntityType, Double> rewards;

    @Builder
    public CombatSkill(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, int maxLevel, Map<Integer, Double> xpRequirements, Map<Integer, List<String>> skillInfoInGUI, Map<Integer, List<StatReward>> statRewards, Map<Integer, List<String>> skillInfoInMessage, Map<Integer, List<CommandReward>> commandRewards, Map<EntityType, Double> rewards) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, maxLevel, xpRequirements, skillInfoInGUI, statRewards, skillInfoInMessage, commandRewards);
        this.rewards = rewards;
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKillMob(PlayerKillEvent e) {
        Player player = e.getPlayer();

        if (!SkillsPlayerUtil.isInSurvival(player)) return;

        Entity entity = e.getKilled();

        double xp = rewards.getOrDefault(entity.getType(), 0D);

        if (xp <= 0) return;

        TheSkills.getApi().getSkillsService().addXp(player, true, true, this, xp);
    }
}

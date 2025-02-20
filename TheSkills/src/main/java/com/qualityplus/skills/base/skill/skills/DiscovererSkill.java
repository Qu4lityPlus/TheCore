package com.qualityplus.skills.base.skill.skills;

import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public final class DiscovererSkill extends Skill {
    private static final Map<UUID, Double> blocksWalked = new HashMap<>();
    private Map<Integer, Double> rewardsPerBlocksWalked;

    @Builder
    public DiscovererSkill(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, int maxLevel, Map<Integer, Double> xpRequirements, Map<Integer, List<String>> skillInfoInGUI, Map<Integer, List<StatReward>> statRewards, Map<Integer, List<String>> skillInfoInMessage, Map<Integer, List<CommandReward>> commandRewards, Map<Integer, Double> rewardsPerBlocksWalked) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, maxLevel, xpRequirements, skillInfoInGUI, statRewards, skillInfoInMessage, commandRewards);
        this.rewardsPerBlocksWalked = rewardsPerBlocksWalked;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        UUID uuid = player.getUniqueId();

        if (!SkillsPlayerUtil.isInSurvival(player)) return;

        Location from = e.getFrom();
        Location to = e.getTo();

        if (to == null) return;

        if (from.getWorld() != to.getWorld())
            return;

        double walked = blocksWalked.getOrDefault(uuid, 0D) + from.distance(to);

        blocksWalked.put(uuid, walked);

        Optional<Integer> optReward = rewardsPerBlocksWalked.keySet().stream().filter(reward -> reward <= walked).findFirst();

        if (!optReward.isPresent()) return;

        double xp = rewardsPerBlocksWalked.getOrDefault(optReward.get(), 0D);

        if (xp <= 0) return;

        blocksWalked.put(uuid, walked - optReward.get());

        TheSkills.getApi().getSkillsService().addXp(player, true, true, this, xp);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(PlayerQuitEvent e) {
        blocksWalked.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(PlayerKickEvent e) {
        blocksWalked.remove(e.getPlayer().getUniqueId());
    }
}

package com.qualityplus.skills.base.skill.skills.blockbreak;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.api.common.rewards.commands.CommandReward;
import com.qualityplus.assistant.util.block.BlockUtils;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.api.service.SkillsService;
import com.qualityplus.skills.base.reward.StatReward;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Map;

@Getter
public abstract class BlockBreakSkill extends Skill {
    private final Map<XMaterial, Double> minionXpRewards;
    private final Map<XMaterial, Double> rewards;

    public BlockBreakSkill(final String id, final boolean enabled, final String displayName,
                           final List<String> description, final GUIOptions skillGUIOptions,
                           final double initialAmount, final int maxLevel, final Map<Integer, Double> xpRequirements,
                           final Map<Integer, List<String>> skillInfoInGUI, final Map<Integer, List<StatReward>> statRewards,
                           final Map<Integer, List<String>> skillInfoInMessage, final Map<Integer, List<CommandReward>> commandRewards,
                           final Map<XMaterial, Double> rewards, final Map<XMaterial, Double> minionXpRewards) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, maxLevel, xpRequirements, skillInfoInGUI, statRewards, skillInfoInMessage, commandRewards);
        this.minionXpRewards = minionXpRewards;
        this.rewards = rewards;
    }

    protected void onBreak(final BlockBreakEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getBlock();

        if (!SkillsPlayerUtil.isInSurvival(player)) {
            return;
        }

        if (BlockUtils.isPlacedByPlayer(block)) {
            return;
        }

        final boolean isAgeable = block.getBlockData() instanceof Ageable;
        boolean isMaxLevel = false;
        if (isAgeable) {
            final Ageable ageable = (Ageable) block.getBlockData();
            isMaxLevel = ageable.getAge() == ageable.getMaximumAge();
        }

        if (isAgeable && !isMaxLevel) {
            return;
        }

        final XMaterial xmaterial = XMaterial.matchXMaterial(block.getType());
        final SkillsService service = TheSkills.getApi().getSkillsService();

        getBlockBreakEventXp(xmaterial, rewards)
                .map(BlockBreakResponse::getXp)
                .ifPresent(xp -> service.addXp(player, true, true, this, xp));
    }
}

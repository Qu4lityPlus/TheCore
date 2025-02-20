package com.qualityplus.skills.listener.minion;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.minions.base.event.PlayerPickUpMinionItemsEvent;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.api.listener.ExtraListener;
import com.qualityplus.skills.api.service.SkillsService;
import com.qualityplus.skills.base.skill.Skill;
import com.qualityplus.skills.base.skill.skills.blockbreak.BlockBreakResponse;
import com.qualityplus.skills.base.skill.skills.blockbreak.BlockBreakSkill;
import com.qualityplus.skills.util.SkillsPlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public final class TheMinionsListener implements ExtraListener {
    private BlockBreakSkill skill;

    @EventHandler
    public void onPickUp(PlayerPickUpMinionItemsEvent e) {
        final Player player = e.getPlayer();

        if (!SkillsPlayerUtil.isInSurvival(player)) return;

        for (ItemStack itemStack : e.getItems()) {
            XMaterial xmaterial = XMaterial.matchXMaterial(itemStack.getType());

            final SkillsService service = TheSkills.getApi().getSkillsService();

            skill.getBlockBreakEventXp(xmaterial, skill.getMinionXpRewards())
                    .map(BlockBreakResponse::getXp)
                    .ifPresent(xp -> service.addXp(player, true, true, skill, xp * itemStack.getAmount()));
        }


    }

    @Override
    public void applySkill(Skill skill) {
        this.skill = (BlockBreakSkill) skill;
    }
}

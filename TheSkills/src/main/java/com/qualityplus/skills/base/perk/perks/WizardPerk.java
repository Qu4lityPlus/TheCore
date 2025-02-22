package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.base.event.EntityDamagedByPlayerEvent;
import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XPotion;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.perk.perks.common.AbstractPotionPerk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public final class WizardPerk extends AbstractPotionPerk {
    @Builder
    public WizardPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, int secondsDurationPerLevel,
                      int baseSecondsDuration, int level) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel, secondsDurationPerLevel, baseSecondsDuration, level);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(EntityDamagedByPlayerEvent e) {
        Player p = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p)) {
            return;
        }

        Optional.of(XPotion.CONFUSION)
        //Optional.of(XPotion.NAUSEA)
                .map(potion -> potion.buildPotionEffect(getDurationTicks(getStat(p)), getLevel()))
                .ifPresent(p::addPotionEffect);
    }
}

package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.base.event.PlayerKillEvent;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class OrbMasterPerk extends Perk {
    @Builder
    public OrbMasterPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChanceExpEvent(PlayerKillEvent e) {
        if (!(e.getKilled() instanceof ExperienceOrb)) return;

        if (e.getKilled().hasMetadata("fortuneXpOrb")) return;

        ExperienceOrb experienceOrb = (ExperienceOrb) e.getKilled();

        Player player = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= chancePerLevel * getStat(player))
            return;

        ExperienceOrb orb = player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);

        orb.setMetadata("fortuneXpOrb", new FixedMetadataValue(TheSkills.getApi().getPlugin(), "fortuneXpOrb"));

        orb.setExperience(experienceOrb.getExperience());

    }
}

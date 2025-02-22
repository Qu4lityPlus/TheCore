package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

@NoArgsConstructor
public final class SteelSkinPerk extends Perk {
    @Builder
    public SteelSkinPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (event.getCause() != EntityDamageEvent.DamageCause.HOT_FLOOR && event.getCause() != EntityDamageEvent.DamageCause.LAVA && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && event.getCause() != EntityDamageEvent.DamageCause.FIRE)
            return;

        double chance = chancePerLevel * getStat(player);

        if (RandomUtil.randomBetween(0.0, 100.0) < chance)
            event.setCancelled(true);
    }
}

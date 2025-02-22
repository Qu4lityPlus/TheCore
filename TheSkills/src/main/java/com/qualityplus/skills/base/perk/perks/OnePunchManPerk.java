package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.base.event.EntityDamagedByPlayerEvent;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class OnePunchManPerk extends Perk {
    private boolean canBeUsedWithPlayers;

    @Builder
    public OnePunchManPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, boolean canBeUsedWithPlayers) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.canBeUsedWithPlayers = canBeUsedWithPlayers;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(EntityDamagedByPlayerEvent e) {
        Player p = e.getPlayer();

        if (!canBeUsedWithPlayers && e.getEntity() instanceof Player) return;

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        if (!(e.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        entity.setHealth(0);
    }
}

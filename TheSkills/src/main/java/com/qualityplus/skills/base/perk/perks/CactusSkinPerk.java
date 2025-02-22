package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.base.event.PlayerDamagedByEntityEvent;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
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
public final class CactusSkinPerk extends Perk {
    private double damagePerLevel;
    private double damageBase;

    @Builder
    public CactusSkinPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, double damagePerLevel, double damageBase) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.damagePerLevel = damagePerLevel;
        this.damageBase = damageBase;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(PlayerDamagedByEntityEvent e) {
        Player p = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        double toDamage = damage(getStat(p));

        if (!(e.getEntity() instanceof LivingEntity)) return;

        ((LivingEntity)e.getEntity()).damage(toDamage);
    }

    private double damage(double level) {
        return (damagePerLevel * level) + damageBase;
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(new Placeholder("damage", damage(level))).get());
    }
}

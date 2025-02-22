package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.base.event.EntityDamagedByPlayerEvent;
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
public final class LightningPunchPerk extends Perk {
    private double damage;

    @Builder
    public LightningPunchPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, double damage) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.damage = damage;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(EntityDamagedByPlayerEvent e) {
        Player p = e.getPlayer();

        if (!(e.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        entity.getWorld().strikeLightningEffect(entity.getLocation());

        entity.damage(damage);
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(new Placeholder("damage", damage)).get());
    }
}

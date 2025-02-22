package com.qualityplus.skills.base.perk.perks;

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
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class ProjectileMasterPerk extends Perk {
    private double extraDamagePercentageBasePerLevel;
    private double extraDamagePercentageBase;

    @Builder
    public ProjectileMasterPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, double extraDamagePercentageBase,
                                double extraDamagePercentageBasePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.extraDamagePercentageBasePerLevel = extraDamagePercentageBasePerLevel;
        this.extraDamagePercentageBase = extraDamagePercentageBase;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(EntityShootBowEvent e) {
        Projectile pr = (Projectile) e.getProjectile();

        if (!(pr.getShooter() instanceof Player)) return;

        Player p = (Player) pr.getShooter();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        double damage = e.getEntity().getLastDamage();

        double toDamage = (getPercentage() * damage) / 100;

        e.getEntity().damage(toDamage);
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(new Placeholder("projectile_percent", getPercentage())).get());
    }

    private double getPercentage() {
        return extraDamagePercentageBasePerLevel + extraDamagePercentageBase;
    }
}

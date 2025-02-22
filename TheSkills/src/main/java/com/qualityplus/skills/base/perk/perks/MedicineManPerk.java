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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class MedicineManPerk extends Perk {
    private double healPercentageToRegeneratePerLevel;
    private double healPercentageToRegenerateBase;

    @Builder
    public MedicineManPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel,
                           double healPercentageToRegenerateBase, double healPercentageToRegeneratePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.healPercentageToRegeneratePerLevel = healPercentageToRegeneratePerLevel;
        this.healPercentageToRegenerateBase = healPercentageToRegenerateBase;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(PlayerDamagedByEntityEvent e) {
        Player p = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        double toGive = (getPercentage() * p.getHealth()) * 100;

        p.setHealth(Math.min(p.getHealth() + toGive, p.getMaxHealth()));
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(new Placeholder("regen_percent", getPercentage())).get());
    }

    private double getPercentage() {
        return healPercentageToRegenerateBase + healPercentageToRegeneratePerLevel;
    }
}

package com.qualityplus.skills.base.stat.stats;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.Stat;
import com.qualityplus.skills.persistance.data.UserData;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

/**
 * Extra Defense for players
 */
@Data @EqualsAndHashCode(callSuper = true) @NoArgsConstructor
public final class DefenseStat extends Stat {
    private double damageReductionPercentagePerLevel;

    @Builder
    public DefenseStat(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double damageReductionPercentagePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount);

        this.damageReductionPercentagePerLevel = damageReductionPercentagePerLevel;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        double multiplier = damageReductionPercentagePerLevel * TheSkills.getApi().getSkillsService().getData(player.getUniqueId())
                .map(UserData::getSkills)
                .map(userPerks -> userPerks.getLevel(id))
                .orElse(1D);

        multiplier /= 100;
        multiplier += 1;

        event.setDamage(event.getDamage() / multiplier);
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        List<IPlaceholder> placeholders = PlaceholderBuilder.create()
                .with(new Placeholder("level_number", level),
                      new Placeholder("level_roman", NumberUtil.toRoman((int)level)),
                      new Placeholder("percentage", MathUtil.round(damageReductionPercentagePerLevel * (double) level))
                ).get();
        return StringUtils.processMulti(description, placeholders);
    }
}

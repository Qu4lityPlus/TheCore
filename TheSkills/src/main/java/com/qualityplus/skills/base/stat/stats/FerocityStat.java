package com.qualityplus.skills.base.stat.stats;

import com.qualityplus.assistant.api.util.IPlaceholder;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.api.util.MathUtil;
import com.qualityplus.assistant.api.util.NumberUtil;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.event.FerocityDamageEvent;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import com.qualityplus.skills.base.stat.Stat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

/**
 * Chance to make damage twice
 */
@Data @EqualsAndHashCode(callSuper = true) @NoArgsConstructor
public final class FerocityStat extends Stat {
    private double chancePerLevel;

    @Builder
    public FerocityStat(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount);

        this.chancePerLevel = chancePerLevel;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        if (!(event.getEntity() instanceof LivingEntity)) return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("ferocity"))
            return;

        double level = getStat(player);

        if (RandomUtil.randomBetween(0.0, 100.0) >= chancePerLevel * level) {
            return;
        }

        Bukkit.getScheduler().runTask(TheSkills.getApi().getPlugin(), () -> {
            FerocityDamageEvent ferocityEvent = new FerocityDamageEvent(player, this, event.getEntity(), event.getDamage());

            Bukkit.getPluginManager().callEvent(ferocityEvent);

            if (ferocityEvent.isCancelled()) return;

            victim.setMetadata("ferocity", new FixedMetadataValue(TheSkills.getApi().getPlugin(), this));
            victim.setNoDamageTicks(0);
            victim.damage(ferocityEvent.getDamage(), player);
        });
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        List<IPlaceholder> placeholders = PlaceholderBuilder.create()
                .with(new Placeholder("level_number", level),
                      new Placeholder("level_roman", NumberUtil.toRoman((int)level)),
                      new Placeholder("chance", chancePerLevel * level)
                ).get();
        return StringUtils.processMulti(description, placeholders);
    }
}

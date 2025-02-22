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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public final class MiningSpeedPerk extends Perk {
    private int secondsDurationPerLevel;
    private int baseSecondsDuration;

    @Builder
    public MiningSpeedPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, int secondsDurationPerLevel, int baseSecondsDuration,
                           double chancePerLevel) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel);

        this.secondsDurationPerLevel = secondsDurationPerLevel;
        this.baseSecondsDuration = baseSecondsDuration;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        double level = getStat(player);

        if (RandomUtil.randomBetween(0.0, 100.0) >= chancePerLevel * level)
            return;

        int duration = getDuration(level) * 20;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, 1));
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(new Placeholder("duration", getDuration(level))).get());
    }

    private int getDuration(double level) {
        return (int) ((secondsDurationPerLevel * level) + baseSecondsDuration);
    }
}

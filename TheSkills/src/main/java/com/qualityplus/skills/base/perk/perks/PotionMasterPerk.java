package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.base.perk.perks.common.AbstractPotionPerk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public final class PotionMasterPerk extends AbstractPotionPerk {
    @Builder
    public PotionMasterPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double initialAmount, double chancePerLevel, int secondsDurationPerLevel, int baseSecondsDuration,
                            int level) {
        super(id, enabled, displayName, description, skillGUIOptions, initialAmount, chancePerLevel, secondsDurationPerLevel, baseSecondsDuration, level);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(PlayerItemConsumeEvent e) {
        if (!e.getItem().getType().equals(Material.POTION)) return;

        final Player player = e.getPlayer();

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(player)) return;

        PotionEffect effect = getPotion(getStat(player), e.getItem());

        if (effect == null) {
            return;
        }

        player.addPotionEffect(effect);
    }

    private PotionEffect getPotion(final double level, final ItemStack item) {
        if (!(item.getItemMeta() instanceof PotionMeta)) {
            return null;
        }

        final PotionEffectType type = Optional.ofNullable((PotionMeta) item.getItemMeta())
                .map(meta -> meta.getBasePotionData().getType().getEffectType()).
                orElse(null);

        return Optional.ofNullable(type).map(t -> new PotionEffect(type, getDurationTicks(level), getLevel())).orElse(null);
    }
}

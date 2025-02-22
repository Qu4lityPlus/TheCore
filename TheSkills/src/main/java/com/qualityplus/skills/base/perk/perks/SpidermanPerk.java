package com.qualityplus.skills.base.perk.perks;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XMaterial;
import com.qualityplus.assistant.base.event.EntityDamagedByPlayerEvent;
import com.qualityplus.assistant.util.StringUtils;
import com.qualityplus.assistant.util.placeholder.Placeholder;
import com.qualityplus.assistant.util.placeholder.PlaceholderBuilder;
import com.qualityplus.assistant.util.random.RandomUtil;
import com.qualityplus.skills.TheSkills;
import com.qualityplus.skills.base.perk.Perk;
import com.qualityplus.skills.base.skill.gui.GUIOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public final class SpidermanPerk extends Perk {
    private final Map<Location, Material> oldBlocks = new HashMap<>();
    private boolean canBeUsedWithPlayers;
    private List<String> enabledWorlds;
    private int secondsDuration;

    @Builder
    public SpidermanPerk(String id, boolean enabled, String displayName, List<String> description, GUIOptions skillGUIOptions, double baseAmount, double chancePerLevel, boolean canBeUsedWithPlayers,
                         List<String> enabledWorlds, int secondsDuration) {
        super(id, enabled, displayName, description, skillGUIOptions, baseAmount, chancePerLevel);

        this.canBeUsedWithPlayers = canBeUsedWithPlayers;
        this.chancePerLevel = chancePerLevel;
        this.enabledWorlds = enabledWorlds;
        this.secondsDuration = secondsDuration;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePerk(EntityDamagedByPlayerEvent e) {
        Player p = e.getPlayer();

        if (!enabledWorlds.contains(p.getWorld().getName())) return;

        if (!canBeUsedWithPlayers && e.getEntity() instanceof Player) return;

        if (RandomUtil.randomBetween(0.0, 100.0) >= getChancePerLevel() * getStat(p))
            return;

        Block block = e.getEntity().getLocation().getBlock();

        final Location location = block.getLocation();

        final Material before = Optional.of(block.getType()).orElse(Material.AIR);

        oldBlocks.put(location, before);

        block.setType(XMaterial.COBWEB.parseMaterial());

        Bukkit.getScheduler().runTaskLater(TheSkills.getApi().getPlugin(), () ->{
            Optional.ofNullable(oldBlocks.getOrDefault(location, null)).ifPresent(material -> location.getBlock().setType(material));
            oldBlocks.remove(location);
        }, secondsDuration * 20L);
    }

    @Override
    public List<String> getFormattedDescription(double level) {
        return StringUtils.processMulti(super.getFormattedDescription(level), PlaceholderBuilder.create(new Placeholder("duration", secondsDuration)).get());
    }
}

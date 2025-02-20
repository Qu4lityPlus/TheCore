package com.qualityplus.pets.base.skills;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.stats.Stat;
import com.qualityplus.pets.api.skills.SkillDependency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class AureliumSkillsPlugin implements SkillDependency {
    private static final String TEMPORAL_STAT_PLACEHOLDER = "ThePets_%s";
    private final AureliumSkills plugin = getPlugin();

    @Override
    public void addStat(Player player, String stat, double amount) {
        AureliumAPI.addStatModifier(player, getFormatted(stat), getAureliumStat(stat), amount);
    }

    @Override
    public void removeStat(Player player, String stat, double amount) {
        AureliumAPI.removeStatModifier(player, getFormatted(stat));
    }

    private Stat getAureliumStat(String key) {
        return plugin.getStatRegistry().getStat(key);
    }

    private String getFormatted(String stat) {
        return String.format(TEMPORAL_STAT_PLACEHOLDER, stat);
    }

    @NotNull
    @SuppressWarnings("all")
    private AureliumSkills getPlugin() {
        return (AureliumSkills) Bukkit.getPluginManager().getPlugin("AureliumSkills");
    }
}

package com.qualityplus.runes.api.provider;

import org.bukkit.entity.Player;

public interface LevelProvider {
    double getLevel(Player player);

    void addXp(Player player, double xp);
}

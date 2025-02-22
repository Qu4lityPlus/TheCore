package com.qualityplus.runes.base.provider;

import com.qualityplus.runes.api.provider.LevelProvider;
import org.bukkit.entity.Player;

public final class XPLevelProvider implements LevelProvider {
    @Override
    public double getLevel(Player player) {
        return player.getLevel();
    }

    @Override
    public void addXp(Player player, double xp) {
        player.setTotalExperience((int) (player.getTotalExperience() + xp));
    }
}

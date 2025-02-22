package com.qualityplus.skills.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@UtilityClass
public final class SkillsPlayerUtil {
    public static boolean isInSurvival(Player player) {
        return player.getGameMode().equals(GameMode.SURVIVAL);
    }

    public static double getXpMultiplier(Player player) {
        double multiplier = 1;

        for (double i = 1.5D; i<10D; i+=0.5D)
            if (player.hasPermission("theskills.xp.multiplier." + i)) multiplier = Math.max(multiplier, i);



        return multiplier;
    }
}

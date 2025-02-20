package com.qualityplus.minions.util;

import com.qualityplus.minions.TheMinions;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class MinionPlayerUtil {

    public static final String PLAYER_MINIONS_PLACE_AMOUNT = "theminions.place_amount.";

    public static int getMinionsAmount(Player player) {
        int minions = 1;

        for (int i = 1; i< TheMinions.getApi().getConfigFiles().config().maxMinionsPerPlayer; i+=1) {
            if (player.hasPermission(PLAYER_MINIONS_PLACE_AMOUNT + i)) {
                minions = Math.max(minions, i);
            }
        }
        return minions;
    }
}

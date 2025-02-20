package com.qualityplus.dragon.base.handler;

import com.qualityplus.dragon.api.handler.ReplyHandler;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public final class BossBarHandler implements ReplyHandler<Player, String> {
    private BossBar bossBar;

    @Override
    public void handle(Player player, String message) {

        if (player == null || message == null || message.equals("")) {
            bossBar.removeAll();
            return;
        }
        if (bossBar != null) bossBar.removeAll();
        this.bossBar = Bukkit.createBossBar(message, BarColor.PURPLE, BarStyle.SEGMENTED_10, BarFlag.DARKEN_SKY);
        this.bossBar.addPlayer(player);
    }
}

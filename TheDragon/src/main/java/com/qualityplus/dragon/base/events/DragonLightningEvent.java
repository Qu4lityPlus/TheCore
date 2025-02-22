package com.qualityplus.dragon.base.events;

import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.event.DragonGameEvent;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.base.configs.DragonEventsFile;
import com.qualityplus.dragon.base.game.player.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public final class DragonLightningEvent extends DragonGameEvent {
    private final double lightningDamage;

    public DragonLightningEvent(DragonEventsFile.SerializableEvent event) {
        super(event.generalSettings.secondsDuration, event.generalSettings.repeatEventAfterSeconds, event.generalSettings.dragonSpeedAmplifier, event.generalSettings.keepDragonAFK);
        this.lightningDamage = event.lightningSettings.lightningDamage;
    }


    @Override
    public void start(DragonGame dragonGame) {
        this.time = 0;

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(TheDragon.getApi().getPlugin(), () -> {

            if (this.time >= this.duration) {
                //Cancelling Event
                finish();
            } else if (this.time % this.repeat == 0) {
                //Check Event
                this.manageDragon(dragonGame);
            }

            this.time+=1;
        }, 0, 20);
    }

    private void manageDragon(final DragonGame dragonGame) {
        dragonGame.getPlayers(EventPlayer::isActive).forEach(player -> makeLightning(player, dragonGame));
    }


    private void makeLightning(final Player player, final DragonGame dragonGame) {
        Bukkit.getScheduler().runTask(TheDragon.getApi().getPlugin(), () -> this.strikePlayer(player, dragonGame));
    }

    private void strikePlayer(final Player player, final DragonGame dragonGame) {
        if (!dragonGame.isActive()) {
            return;
        }

        if (!isValidPlayer(player)) {
            return;
        }

        LightningStrike ls = player.getLocation().getWorld().strikeLightning(player.getLocation());

        ls.setMetadata("LightningStrike", new FixedMetadataValue(TheDragon.getApi().getPlugin(), this.lightningDamage));
    }

    private boolean isValidPlayer(final Player player) {
        return player != null && player.isOnline();
    }
}

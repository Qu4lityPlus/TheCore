package com.qualityplus.dragon.base.events;

import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.event.DragonGameEvent;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.handler.ParticleHandler;
import com.qualityplus.dragon.api.handler.ProjectileHandler;
import com.qualityplus.dragon.base.configs.DragonEventsFile;
import com.qualityplus.dragon.base.handler.ParticleHandlerImpl;
import com.qualityplus.dragon.base.handler.ProjectileHandlerImpl;
import org.bukkit.Bukkit;
import com.qualityplus.dragon.api.handler.ProjectileHandler.ProjectileType;
import org.bukkit.Particle;

public final class DragonFireBallEvent extends DragonGameEvent {
    private final ProjectileHandler projectileEvent;
    private final ParticleHandler particleEvent;
    private final double amount;
    private final boolean particle = false;
    private final double damage = 0;

    public DragonFireBallEvent(DragonEventsFile.SerializableEvent event) {
        super(event.generalSettings.secondsDuration, event.generalSettings.repeatEventAfterSeconds, event.generalSettings.dragonSpeedAmplifier, event.generalSettings.keepDragonAFK);
        this.amount = event.dragonFireballSettings.dragonFireballsPerSecond;
        this.projectileEvent = new ProjectileHandlerImpl();
        this.particleEvent = new ParticleHandlerImpl();
    }

    @Override
    public void start(final DragonGame dragonGame) {
        this.time = 0;

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(TheDragon.getApi().getPlugin(), () -> {

            this.move(dragonGame);

            if (this.time >= this.duration) {
                //Cancelling Event
                this.finish();
            } else if (this.time % this.repeat == 0) {
                //Check Event
                this.projectileEvent.shoot(ProjectileType.DRAGONBALL, this.damage, this.amount, dragonGame);
            }

            if (this.particle) {
                this.particleEvent.spell(dragonGame, Particle.SPELL_WITCH);
            }

            this.time+=1;
        }, 0, 20);
    }
}

package com.qualityplus.dragon.base.events;

import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.event.DragonGameEvent;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.handler.ParticleHandler;
import com.qualityplus.dragon.api.handler.ProjectileHandler;
import com.qualityplus.dragon.api.handler.ProjectileHandler.ProjectileType;
import com.qualityplus.dragon.base.configs.DragonEventsFile;
import com.qualityplus.dragon.base.handler.ParticleHandlerImpl;
import com.qualityplus.dragon.base.handler.ProjectileHandlerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;

public final class DragonNormalFireBallEvent extends DragonGameEvent {
    private final ProjectileHandler projectileEvent;
    private final ParticleHandler particleEvent;
    private final double fireballsPerSecond;
    private final boolean showParticle;
    private final double fireballDamage;

    public DragonNormalFireBallEvent(DragonEventsFile.SerializableEvent event) {
        super(event.generalSettings.secondsDuration, event.generalSettings.repeatEventAfterSeconds, event.generalSettings.dragonSpeedAmplifier, event.generalSettings.keepDragonAFK);
        this.fireballsPerSecond = event.fireBallsSettings.fireballsPerSecond;
        this.fireballDamage = event.fireBallsSettings.fireballDamage;
        this.showParticle = event.fireBallsSettings.showParticle;
        this.projectileEvent = new ProjectileHandlerImpl();
        this.particleEvent = new ParticleHandlerImpl();
    }

    @Override
    public void start(DragonGame dragonGame) {
        final Plugin plugin = TheDragon.getApi().getPlugin();

        this.time = 0;

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            this.move(dragonGame);

            //Cancelling Event
            if (this.time >= this.duration) {
                this.finish();
            } else if (this.time % this.repeat == 0) {
                this.projectileEvent.shoot(ProjectileType.FIREBALL, fireballDamage, fireballsPerSecond, dragonGame);
            }

            if (this.showParticle) {
                /**
                 * TODO
                 */
                this.particleEvent.spell(dragonGame, Particle.FLAME);
            }

            this.time += 1;
        }, 0, 20);
    }
}

package com.qualityplus.dragon.base.handler;

import com.destroystokyo.paper.ParticleBuilder;
import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.nms.NMS;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.controller.DragonController;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.handler.ParticleHandler;
import com.qualityplus.dragon.base.game.player.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

public final class ParticleHandlerImpl implements ParticleHandler {
    @Override
    public void spell(final DragonGame dragonGame, final Particle particle) {
        int points = 30;
        double initialRadius = 1;
        Bukkit.getScheduler().runTaskLater(TheDragon.getApi().getPlugin(), () -> {
            final DragonController controller = TheDragon.getApi().getDragonService().getDragonController();

            if (!dragonGame.isActive() || controller == null || controller.dragon() == null) return;

            final Location origin = TheAssistantPlugin
                    .getAPI()
                    .getNms()
                    .getDragonPart(controller.dragon(), NMS.DragonPart.BODY)
                    .clone()
                    .subtract(0,3,0);

            for (double radius = initialRadius; radius>0; radius-=0.1) {
                for (int i = 0; i < points; i++) {
                    double angle = 2 * Math.PI * i / points;

                    Location point = origin.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));

                    Location toDisplay = new Location(point.getWorld(), point.getX(), point.getY(), point.getZ());

                    new ParticleBuilder(particle)
                            .location(toDisplay)
                            .receivers(dragonGame.getPlayers(EventPlayer::isActive))
                            .spawn();
                }
            }
        }, 1);
    }
}

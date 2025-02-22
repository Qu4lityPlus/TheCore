package com.qualityplus.dragon.base.handler;

import com.qualityplus.assistant.TheAssistantPlugin;
import com.qualityplus.assistant.api.nms.NMS;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.game.DragonGame;
import com.qualityplus.dragon.api.handler.ProjectileHandler;
import com.qualityplus.dragon.base.game.player.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.List;

public final class ProjectileHandlerImpl implements ProjectileHandler {
    @Override
    public void shoot(ProjectileType projectile, double damage, double amount, final DragonGame dragonGame) {
        final List<Player> players = dragonGame.getPlayers(EventPlayer::isActive);

        if (players.isEmpty() || amount < 1) {
            return;
        }

        final int perPlayer = (int) (amount / players.size());

        Bukkit.getScheduler().runTask(TheDragon.getApi().getPlugin(),
                () -> players.forEach(player -> this.shootPlayer(player, projectile, perPlayer, damage, dragonGame))
        );
    }

    private void shootPlayer(final Player player, final ProjectileType projectile,
                             final int perPlayer, final double damage, final DragonGame game) {

        for (int i = 0; i<perPlayer; i++) {
            shoot(player, projectile, damage, i, game);
        }
    }


    private void shoot(Player player, ProjectileType projectile, double damage, int i, DragonGame dragonGame) {
        Bukkit.getScheduler().runTaskLater(TheDragon.getApi().getPlugin(), () -> {
            if (dragonGame.isActive() && player != null && player.isOnline()) {
                final EnderDragon enderDragon = TheDragon.getApi().getDragonService().getActiveEnderDragon();

                if (enderDragon == null) {
                    return;
                }

                Location dragonLoc = TheAssistantPlugin.getAPI()
                        .getNms()
                        .getDragonPart(enderDragon, NMS.DragonPart.BODY)
                        .clone()
                        .subtract(0,3,0);

                Location second_location = player.getLocation().clone();

                Vector from = new Vector(dragonLoc.getX(), dragonLoc.getY(), dragonLoc.getZ());
                Vector to = new Vector(second_location.getX(), second_location.getY(), second_location.getZ());
                Vector vector = to.subtract(from).normalize();

                Fireball projectile1;
                if (projectile.equals(ProjectileType.DRAGONBALL))
                    projectile1 = dragonLoc.getWorld().spawn(dragonLoc, DragonFireball.class);
                else
                    projectile1 = dragonLoc.getWorld().spawn(dragonLoc, Fireball.class);
                projectile1.setMetadata("DragonFireball", new FixedMetadataValue(TheDragon.getApi().getPlugin(), damage));
                projectile1.setDirection(vector);
            }
        },20L * i);
    }
}

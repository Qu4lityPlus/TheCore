package com.qualityplus.dragon.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.controller.DragonController;
import com.qualityplus.dragon.base.event.DragonRefreshEvent;
import com.qualityplus.dragon.base.event.DragonRefreshEvent.RefreshType;
import com.qualityplus.dragon.util.DragonVelocityUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.EntityEffect;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

@Component
public final class DragonControlListener implements Listener {
    private @Inject Box box;


    @EventHandler
    public void gameHandler(DragonRefreshEvent event) {
        DragonController controller = box.dragonService().getDragonController();

        if (event.getRefreshType() != DragonRefreshEvent.RefreshType.TICK) return;

        manageDragonController(controller);
    }

    private void manageDragonController(DragonController controller) {
        if (controller == null) return;

        if (controller.isAfk()) {
            controller.dragon().setPhase(EnderDragon.Phase.HOVER);
        } else {
            controller.target();
            controller.move();
        }
    }


    @EventHandler
    public void removeCurrentDragon(DragonRefreshEvent paramUpdateEvent) {
        if (paramUpdateEvent.getRefreshType() != RefreshType.SLOW) return;

        DragonController controller = box.dragonService().getDragonController();

        removeDragonIfAlive(controller);
    }

    private void removeDragonIfAlive(DragonController controller) {
        if (controller == null) return;

        if (!controller.dragon().isValid() || controller.dragon().isDead())
            controller.dragon().remove();
    }

    @EventHandler
    public void cancelDefaultTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof EnderDragon)) return;

        DragonController controller = box.dragonService().getDragonController();

        if (controller == null || !event.getEntity().equals(controller.dragon())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDragonDamage(EntityDamageByEntityEvent event) {
        DragonController controller = box.dragonService().getDragonController();

        if (controller == null) return;

        if (!(event.getEntity() instanceof EnderDragon)) return;

        if (!event.getEntity().equals(controller.dragon())) return;

        controller.targetSky();

        event.setCancelled(true);

        event.getEntity().playEffect(EntityEffect.HURT);
    }

    @EventHandler
    public void onPlayerDamagedByDragon(EntityDamageByEntityEvent event) {
        DragonController controller = box.dragonService().getDragonController();

        if (controller == null) return;

        if (!(event.getEntity() instanceof Player)) return;

        if (!(event.getDamager() instanceof EnderDragon) || !controller.dragon().equals(event.getDamager())) return;

        Player player = (Player) event.getEntity();

        event.setCancelled(true);

        player.playEffect(EntityEffect.HURT);

        DragonVelocityUtil.velocity(player, DragonVelocityUtil.getTrajectory(event.getDamager(), player), false, true, 1.0D, 0.0D, 0.6D, 2.0D);

        player.setNoDamageTicks(30);

        player.damage(event.getDamage() / 2.0D);
    }

}

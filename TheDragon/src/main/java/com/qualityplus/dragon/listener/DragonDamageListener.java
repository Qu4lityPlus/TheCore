package com.qualityplus.dragon.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.api.box.Box;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.ProjectileSource;

@Component
public final class DragonDamageListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void makeDragonShoot(final EntityTargetEvent e) {
        if (!isEnderDragon(e.getEntity())) {
            return;
        }

        final Entity dragon = e.getEntity();

        final Location location = dragon.getLocation();
        final Fireball fireball = dragon.getWorld().spawn(location, Fireball.class);
        fireball.setShooter((ProjectileSource) dragon);
        fireball.setVelocity(dragon.getLocation().getDirection().multiply(2));
        fireball.setIsIncendiary(false);
        fireball.setYield(0);
    }


    @EventHandler
    public void onDragonDestroyBlocks(final EntityChangeBlockEvent e) {
        if (!isEnderDragon(e.getEntity())) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onDragonExplodeBlocks(final EntityExplodeEvent e) {
        if (!isEnderDragon(e.getEntity())) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void stopDragonDamage(final EntityExplodeEvent event) {
        final Entity entity = event.getEntity();

        if (!isEnderDragon(entity) && !(entity instanceof EnderDragonPart)) {
            return;
        }

        event.setCancelled(true);
    }


    @EventHandler
    public void cancelDragonRegeneration(final EntityRegainHealthEvent e) {
        if (!isEnderDragon(e.getEntity())) {
            return;
        }

        final EnderDragon dragon = (EnderDragon) e.getEntity();

        if (dragon.getHealth() >= 2) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void finishGameOnDeath(final EntityDeathEvent e) {
        if (!isEnderDragon(e.getEntity())) {
            return;
        }

        box.game().finish();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplode(final EntityExplodeEvent e) {
        if (!(e.getEntity() instanceof Fireball)) {
            return;
        }

        final Fireball fireball = (Fireball) e.getEntity();

        if (!(fireball.getShooter() instanceof EnderDragon)) {
            return;
        }

        if (!box.game().isActive()) {
            return;
        }

        e.setCancelled(true);

        final Location location = fireball.getLocation().clone();

        fireball.remove();

        replaceForFakeExplosion(location);
    }

    private void replaceForFakeExplosion(final Location location) {
        if (location.getWorld() == null) {
            return;
        }

        location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 5, false, false);
    }


    @EventHandler
    public void onCrystalExplode(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof EnderCrystal)) {
            return;
        }

        if (!box.structures().isCrystal(e.getEntity())) {
            return;
        }

        if (!box.files().config().eventSettings.generalSettings.invincibleCrystals) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onDragonExplode(final EntityDamageEvent e) {
        if (!isEnderDragon(e.getEntity())) {
            return;
        }

        if (e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return;
        }

        e.setCancelled(true);
    }


    private boolean isEnderDragon(final Entity entity) {
        return entity instanceof EnderDragon;
    }
}
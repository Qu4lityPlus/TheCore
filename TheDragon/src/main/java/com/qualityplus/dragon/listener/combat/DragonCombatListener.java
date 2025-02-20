package com.qualityplus.dragon.listener.combat;

import com.qualityplus.assistant.lib.com.cryptomorin.xseries.XSound;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.dragon.TheDragon;
import com.qualityplus.dragon.api.box.Box;
import com.qualityplus.dragon.api.game.dragon.TheDragonEntity;
import com.qualityplus.dragon.base.game.dragon.TheDragonEntityImpl;
import com.qualityplus.dragon.util.DragonHealthUtil;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Optional;

@Component
public final class DragonCombatListener implements Listener {
    private @Inject Box box;

    @EventHandler
    public void removeDamageFromPlayers(final EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof EnderDragon) && !(e.getEntity() instanceof EnderDragonPart)) {
            return;
        }

        final EnderDragon enderDragon = box.dragonService().getActiveEnderDragon();

        if (!e.getEntity().getUniqueId().equals(enderDragon.getUniqueId())) {
            return;
        }

        box.users().removePlayersDamage(e.getAmount());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void addDamageToPlayers(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof EnderDragon)) {
            return;
        }

        if (event.getDamager() instanceof Fireball) {
            event.setCancelled(true);
        } else {

            final Player attacker = getDragonAttacker(event);

            Optional.ofNullable(attacker).ifPresent(player -> {
                double damage = event.getDamage();

                if (dragonIsDeath()) {
                    return;
                }

                addDamageToAttacker(player, damage);

                mangeDragonHealth(event, damage);
            });
        }
    }

    private void mangeDragonHealth(final EntityDamageByEntityEvent event, double damage) {
        final TheDragonEntity theDragonEntity = box.dragonService().getActiveDragon();

        final EnderDragon enderDragon = box.dragonService().getActiveEnderDragon();

        if (enderDragon.getHealth() <= 0) {
            return;
        }

        event.setCancelled(true);

        double dragonHealth = DragonHealthUtil.getHealthAfterDamage(theDragonEntity.getHealth(), enderDragon.getHealth(), damage);

        if (dragonHealth < 0) {
            box.game().finish();
        } else {

            enderDragon.setHealth(dragonHealth);

            Bukkit.getScheduler().runTaskLater(TheDragon.getApi().getPlugin(), () -> enderDragon.playEffect(EntityEffect.HURT), 1);
            enderDragon.getWorld().playSound(enderDragon.getLocation(), XSound.ENTITY_ENDER_DRAGON_HURT.parseSound(), 100.0F, 1.0F);
        }
    }

    private boolean dragonIsDeath() {
        TheDragonEntity theDragonEntity = box.dragonService().getActiveDragon();

        EnderDragon enderDragon = box.dragonService().getActiveEnderDragon();

        return !(theDragonEntity instanceof TheDragonEntityImpl) || enderDragon.getHealth() <= 0;
    }

    private Player getDragonAttacker(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player ? (Player) event.getDamager() : getPlayerIfIsShooter(event);
    }

    private Player getPlayerIfIsShooter(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Arrow && ((Arrow)event.getDamager()).getShooter() instanceof Player ?
                (Player) ((Arrow)event.getDamager()).getShooter() : null;
    }

    private void addDamageToAttacker(Player player, double damage) {
        box.users().addPlayerDamage(player, damage);
        box.users().setLast(player.getUniqueId());
    }
}

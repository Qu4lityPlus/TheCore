package com.qualityplus.minions.listener;

import com.qualityplus.minions.api.minion.MinionEntity;
import com.qualityplus.minions.base.minions.entity.tracker.MinionArmorStandTracker;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Collection;
import java.util.Optional;

@Component
public final class MinionDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof ArmorStand)) return;

        DamageCause cause = e.getCause();

        if (!(cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK) || cause.equals(DamageCause.PROJECTILE) || cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION))) return;

        ArmorStand armorStand = (ArmorStand)e.getEntity();

        Optional<MinionEntity> entity = MinionArmorStandTracker.getByID(armorStand.getUniqueId());

        if (!entity.isPresent()) return;

        e.setCancelled(true);

        armorStand.setFireTicks(0);
    }

    @EventHandler
    public void onExtend(BlockPistonExtendEvent e) {
        Block block = e.getBlock();
        if (isMinionNear(block))
            e.setCancelled(true);

        e.getBlocks().stream()
                .filter(this::isMinionNear)
                .findAny()
                .ifPresent(m -> e.setCancelled(true));
    }

    @EventHandler
    public void onRetract(BlockPistonRetractEvent e) {
        e.getBlocks().stream()
                .filter(this::isMinionNear)
                .findAny()
                .ifPresent(m -> e.setCancelled(true));
    }

    private boolean isMinionNear(Block b) {
        Location location = b.getLocation().clone().add(0.5D, 0.0D, 0.5D);
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 2.0D, 2.0D, 2.0D);

        return entities.stream()
                .anyMatch(entity -> entity instanceof ArmorStand && MinionArmorStandTracker.getByID(entity.getUniqueId()).isPresent());
    }
}

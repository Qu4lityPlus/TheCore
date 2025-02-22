package com.qualityplus.pets.listener;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import com.qualityplus.pets.api.box.Box;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.base.pet.entity.tracker.PetArmorStandTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public final class PetEntityListener implements Listener {
    private static final List<EntityDamageEvent.DamageCause> FIRE_DAMAGE_CAUSES = Arrays.asList(
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.HOT_FLOOR,
            EntityDamageEvent.DamageCause.LAVA
    );
    private @Inject Box box;

    @EventHandler
    public void onInteractWithPet(final PlayerInteractAtEntityEvent event) {
        final Entity entity = event.getRightClicked();
        if (!(entity instanceof ArmorStand)) {
            return;
        }

        final ArmorStand armorStand = (ArmorStand) entity;

        final Optional<PetEntity> petEntity = PetArmorStandTracker.getByID(armorStand.getUniqueId());
        if (petEntity.isEmpty()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPetGetFire(final EntityCombustEvent event) {
        final Entity entity = event.getEntity();

        if (!(entity instanceof ArmorStand)) {
            return;
        }

        final Optional<PetEntity> petEntity = PetArmorStandTracker.getByID(entity.getUniqueId());
        if (petEntity.isEmpty()) {
            return;
        }

        event.setCancelled(true);
        entity.setFireTicks(0);
    }
}
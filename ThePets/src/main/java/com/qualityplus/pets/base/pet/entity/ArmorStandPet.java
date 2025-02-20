package com.qualityplus.pets.base.pet.entity;

import com.qualityplus.assistant.util.armorstand.ArmorStandUtil;
import com.qualityplus.assistant.util.itemstack.ItemBuilder;
import com.qualityplus.pets.ThePets;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.entity.tracker.PetArmorStandTracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public final class ArmorStandPet extends MinecraftPet {
    private ArmorStand armorStand;
    private boolean loaded;

    private ArmorStandPet(UUID petUniqueId, UUID owner, Pet pet, boolean loaded) {
        super(petUniqueId, owner, pet);

        this.loaded = loaded;
    }

    public static ArmorStandPet create(UUID petUniqueId, UUID owner, Pet pet, boolean loaded) {
        return new ArmorStandPet(petUniqueId, owner, pet, loaded);
    }

    @Override
    public void load() {
        Optional.ofNullable(getNextLocation())
                .ifPresent(this::createArmorStand);

        loaded = true;
    }

    @Override
    public void unload() {
        Optional.ofNullable(armorStand)
                .filter(ArmorStandUtil::entityIsValid)
                .ifPresent(e -> {
                    final UUID uuid = e.getUniqueId();
                    final Location location = e.getLocation();
                    try {
                        location.getChunk().load();
                        location.getChunk().setForceLoaded(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    e.remove();
                    PetArmorStandTracker.unregisterEntity(uuid);
                });

        loaded = false;
    }

    @Override
    public void spawn() {
        super.spawn();

        if (loaded) {
            Optional.ofNullable(getNextLocation())
                    .ifPresent(this::createArmorStand);
        }
    }

    @Override
    public void deSpawn(DeSpawnReason deSpawnReason) {
        super.deSpawn(deSpawnReason);

        unload();
    }

    @Override
    public void update() {
        super.update();

        Optional.ofNullable(armorStand).ifPresent(stand -> stand.setCustomName(getDisplayName()));
    }

    @Override
    public void prepareToLevelUp() {
        super.removePotions();
        super.removeStats();
    }

    @Override
    public Location getSpawn() {
        return getNextLocation();
    }


    @Override
    public void followOwner() {
        Optional.ofNullable(getNextLocation())
                .ifPresent(this::teleportNotNull);
    }


    private static boolean entityIsValid(ArmorStand armorStand) {
        return armorStand != null && !armorStand.isDead();
    }

    private void teleportNotNull(Location location) {
        if (!entityIsValid(armorStand)) return;

        if (location == null) return;

        Optional.ofNullable(armorStand).ifPresent(a -> {
            if (a == null) {
                return;
            }

            if (a.isDead()) {
                return;
            }

            if (!a.getLocation().getChunk().isLoaded()) {
                return;
            }

            a.teleport(location);

        });
    }

    private void createArmorStand(Location location) {

        armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setCollidable(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setHelmet(getItemStack());

        PetArmorStandTracker.registerNewEntity(armorStand, this);

        update();
    }

    private ItemStack getItemStack() {
        return ItemBuilder.of()
                .amount(1)
                .displayName("")
                .lore(new ArrayList<>())
                .material(pet.getPetEntityOptions().getMaterial())
                .headData(pet.getPetEntityOptions().getTexture())
                .buildStack();
    }
}

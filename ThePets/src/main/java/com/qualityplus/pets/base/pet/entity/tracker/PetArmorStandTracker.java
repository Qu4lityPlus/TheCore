package com.qualityplus.pets.base.pet.entity.tracker;

import com.qualityplus.pets.api.pet.entity.PetEntity;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PetArmorStandTracker {
    private static final Map<UUID, PetEntity> TRACKED_ENTITIES = new HashMap<>();

    @ApiStatus.Internal
    public static void registerNewEntity(@NotNull final ArmorStand armorStand, PetEntity petEntity) {
        TRACKED_ENTITIES.put(armorStand.getUniqueId(), petEntity);
    }


    @ApiStatus.Internal
    public static void unregisterEntity(@NotNull final ArmorStand armorStand) {
        TRACKED_ENTITIES.remove(armorStand.getUniqueId());
    }

    @ApiStatus.Internal
    public static void unregisterEntity(@NotNull final UUID armorStand) {
        TRACKED_ENTITIES.remove(armorStand);
    }


    @NotNull
    public static Optional<PetEntity> getByID(@NotNull final UUID id) {
        return Optional.ofNullable(TRACKED_ENTITIES.get(id));
    }
}

package com.qualityplus.pets.base.pet.tracker;

import com.google.common.collect.ImmutableSet;
import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.api.service.PetFollowService;
import com.qualityplus.pets.api.service.PetParticleService;

import com.qualityplus.assistant.lib.eu.okaeri.platform.bukkit.annotation.Scheduled;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class PetEntityTracker {
    private static final Map<UUID, PetEntity> TRACKED_PET_ENTITIES = new HashMap<>();

    @ApiStatus.Internal
    public static void registerNewEntity(@NotNull final PetEntity petEntity) {
        TRACKED_PET_ENTITIES.put(petEntity.getPetUniqueId(), petEntity);
    }


    @ApiStatus.Internal
    public static void unregisterEntity(@NotNull final PetEntity petEntity) {
        TRACKED_PET_ENTITIES.remove(petEntity.getPetUniqueId());
    }

    @NotNull
    public static Optional<PetEntity> getByID(@NotNull final UUID id) {
        return Optional.ofNullable(TRACKED_PET_ENTITIES.get(id));
    }

    public static Set<PetEntity> values() {
        return ImmutableSet.copyOf(TRACKED_PET_ENTITIES.values());
    }

    public static Set<PetEntity> values(Predicate<PetEntity> filter) {
        return ImmutableSet.copyOf(TRACKED_PET_ENTITIES.values().stream().filter(filter).collect(Collectors.toList()));
    }

    @Component
    private static class TickScheduler{
        @Inject
        private PetFollowService petFollowService;
        @Inject
        private PetParticleService petParticleService;

        @Scheduled(rate = 1)
        private void tickAll() {
            TRACKED_PET_ENTITIES.entrySet().forEach(petFollowService::follow);
        }

        @Scheduled(rate = 20)
        private void tickAllParticles() {
            TRACKED_PET_ENTITIES.entrySet().forEach(petParticleService::spellParticle);
        }
    }

}

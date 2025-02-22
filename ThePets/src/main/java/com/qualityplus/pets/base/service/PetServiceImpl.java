package com.qualityplus.pets.base.service;

import com.qualityplus.pets.api.pet.Pets;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.api.service.PetService;
import com.qualityplus.pets.base.event.PetLevelUPEvent;
import com.qualityplus.pets.base.event.PetXPGainEvent;
import com.qualityplus.pets.base.pet.Pet;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import com.qualityplus.pets.persistance.data.PetData;
import com.qualityplus.assistant.lib.eu.okaeri.persistence.document.Document;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles processes related to Pets Cache
 */
@Component
public final class PetServiceImpl implements PetService {
    private final Map<UUID, PetData> dataMap = new HashMap<>();

    /**
     *
     * @param uuid Pet UUID
     * @return PetData based on UUID
     */
    @Override
    public Optional<PetData> getData(UUID uuid) {
        return Optional.ofNullable(dataMap.getOrDefault(uuid, null));
    }

    /**
     * Add a pet to cache
     *
     * @param data PetData database pet
     */
    @Override
    public void addData(PetData data) {
        dataMap.put(data.getUuid(), data);
    }

    /**
     * Remove a pet from cache
     *
     * @param data PetData database pet
     */
    @Override
    public void removeData(PetData data) {
        dataMap.remove(data.getUuid());
    }

    /**
     * Handle xp gain and level up process of a pet
     *
     * @param petData PetData database pet
     * @param toAdd   amount of xp to add
     */
    @Override
    public void addXp(PetData petData, double toAdd) {
        toAdd = Math.abs(toAdd * 1);

        PetXPGainEvent skillsXPGainEvent = new PetXPGainEvent(petData, toAdd);

        Bukkit.getPluginManager().callEvent(skillsXPGainEvent);

        if (skillsXPGainEvent.isCancelled()) return;

        toAdd = skillsXPGainEvent.getXp();

        Pet pet = Pets.getByID(petData.getPetId());

        int level = petData.getLevel();

        double maxXp = getMaxXp(pet, level + 1);

        double maxLevel = getMaxLevel(pet);

        petData.addXp(toAdd);

        double xp = petData.getXp();

        if (xp >= maxXp && level + 1 <= maxLevel) {
            PetEntityTracker.getByID(petData.getUuid()).ifPresent(PetEntity::prepareToLevelUp);

            double remaining = xp - maxXp;

            petData.setXp(0);

            petData.addLevel(1);

            PetLevelUPEvent levelUpEvent = new PetLevelUPEvent(petData, level + 1);

            Bukkit.getPluginManager().callEvent(levelUpEvent);

            if (remaining > 0) addXp(petData, remaining);
        }
    }

    /**
     * Saves all Data of created Pets
     */
    @Override
    public void saveAllData() {
        dataMap.values().forEach(Document::save);
    }


    /**
     *
     * @return Amount of loaded Pets
     */
    @Override
    public int loadedAmount() {
        return dataMap.size();
    }

    /**
     *
     * @param pet   Pet pet
     * @param level int level
     * @return Required xp to level up for @level
     */
    private double getMaxXp(Pet pet, int level) {
        return Optional.ofNullable(pet)
                .map(p -> p.getLevelRequirement(level))
                .orElse(1D);
    }

    /**
     *
     * @param pet Pet pet
     * @return Max level of a pet
     */
    private double getMaxLevel(Pet pet) {
        return Optional.ofNullable(pet)
                .map(Pet::getMaxLevel)
                .orElse(1);
    }
}

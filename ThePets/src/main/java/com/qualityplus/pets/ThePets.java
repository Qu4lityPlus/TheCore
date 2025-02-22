package com.qualityplus.pets;

import com.qualityplus.assistant.lib.eu.okaeri.injector.annotation.Inject;
import com.qualityplus.assistant.okaeri.OkaeriSilentPlugin;
import com.qualityplus.pets.api.ThePetsAPI;
import com.qualityplus.pets.api.pet.entity.PetEntity;
import com.qualityplus.pets.api.service.PetService;
import com.qualityplus.pets.api.service.UserPetService;
import com.qualityplus.pets.base.pet.tracker.PetEntityTracker;
import com.qualityplus.pets.persistance.PetRepository;
import com.qualityplus.pets.persistance.data.PetData;

import com.qualityplus.assistant.lib.eu.okaeri.platform.core.annotation.Scan;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.ExecutionPhase;
import com.qualityplus.assistant.lib.eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Scan(deep = true)
public final class ThePets extends OkaeriSilentPlugin {
    private static @Inject
    @Getter ThePetsAPI api;

    @Planned(ExecutionPhase.POST_SETUP)
    private void loadAllPets(@Inject Logger logger, @Inject PetRepository petRepository, @Inject PetService petService) {
        petRepository.findAll().forEach(petService::addData);

        logger.info(String.format("Plugin has loaded %s Pets from database!", petService.loadedAmount()));
    }

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void saveAllPets(@Inject Logger logger, @Inject PetService petService) {
        try {
            petService.saveAllData();
            logger.info(String.format("Plugin has saved %s pets to database!", petService.loadedAmount()));

        } catch (Exception e) {
            logger.log(Level.SEVERE, "There was an exception trying to save all pets!", e);

        }
    }

    @Planned(ExecutionPhase.PRE_SHUTDOWN)
    private void deSpawnPetIfItsSpawned(@Inject Logger logger, @Inject UserPetService userPetService) {
        AtomicInteger countDown = new AtomicInteger(0);

        userPetService.getAllKeys()
                .stream()
                .map(userPetService::getData)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(data -> {
                    final Optional<PetData> petData = data.getSpawnedPetData();

                    if (petData.isPresent()) {
                        final Optional<PetEntity> petEntity = PetEntityTracker.getByID(petData.get().getUuid());

                        petEntity.ifPresent(pet -> pet.deSpawn(PetEntity.DeSpawnReason.SERVER_TURNED_OFF));
                    }

                    data.save();

                    countDown.getAndIncrement();
                });

        logger.info(String.format("Plugin has saved %s users to database!", countDown.get()));
    }
}
